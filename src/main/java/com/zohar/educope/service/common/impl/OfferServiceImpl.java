package com.zohar.educope.service.common.impl;

import com.zohar.educope.constant.CourseRegisterStatus;
import com.zohar.educope.constant.CourseType;
import com.zohar.educope.constant.OfferType;
import com.zohar.educope.dto.CourseContract;
import com.zohar.educope.dto.CourseContractWrap;
import com.zohar.educope.dto.CourseStatus;
import com.zohar.educope.dto.CourseStatusWrap;
import com.zohar.educope.dto.UserProfile;
import com.zohar.educope.entity.Course;
import com.zohar.educope.repository.OfferRepos;
import com.zohar.educope.service.common.OfferService;
import com.zohar.educope.service.common.UserService;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class OfferServiceImpl implements OfferService {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  OfferRepos offerRepos;

  @Autowired
  UserService userService;

  @Autowired
  MongoTemplate mongoTemplate;

  @Override
  public Course createOffer(Course course) {
    Course courseResponse = null;
    String idAuthor = course.getProfileAuthor().getId();
    UserProfile userProfile = userService.getUserProfile(idAuthor);
    if (userProfile != null) {
      course.setProfileAuthor(userProfile);
    }
    try {
      courseResponse = offerRepos.insert(course);
    } catch (Exception e) {
      logger.error("Error when inserting data into mongodb " + e.getMessage());
    }
    return courseResponse;
  }

  @Override
  public List<Course> getListOfferByType(OfferType offerType) {
    List<Course> courses = offerRepos.findOffersByOfferType(offerType);
    Collections.sort(courses, Collections.reverseOrder());
    return courses;
  }

  @Override
  public Set<Course> getListOfferByTypeAndSubject(OfferType offerType, String subject, int page,
      int size) {
    Set<Course> courses = new HashSet<>();
    // Get all courses by offerType and subject with page and size
    Pageable pageable = PageRequest.of(page, size);
    List<Course> unchangeSubjectListCourses = offerRepos
        .findOffersByOfferTypeAndSubjectContainingIgnoreCase(offerType, subject, pageable);
    List<Course> unchangeSubjectListOffersInPageAndSize = getOfferByPageAndSize(
        unchangeSubjectListCourses, page, size);
    Collections.sort(unchangeSubjectListOffersInPageAndSize, Collections.reverseOrder());
    List<Course> changedSubjectListCourses = new ArrayList<>();
    if (unchangeSubjectListOffersInPageAndSize.size() < size) {
      int sizeSpace = size - unchangeSubjectListOffersInPageAndSize.size();
      Pageable pageableSpace = PageRequest.of(0, sizeSpace);
      // Get all courses by offerType and subject with page and size
      subject = nonAccentVietnamese(subject);
      changedSubjectListCourses = offerRepos
          .findOffersByOfferTypeAndSubjectContainingIgnoreCase(offerType, subject, pageable);
      Collections.sort(changedSubjectListCourses, Collections.reverseOrder());
    }
    // Merge two lists offer into set offer
    mergeListsOrderIntoSet(unchangeSubjectListOffersInPageAndSize, changedSubjectListCourses,
        courses);
    return courses;
  }

  @Override
  public Course getCourseById(String courseId) {
    Optional<Course> courseOptional = offerRepos.findById(courseId);
    if (courseOptional.isPresent()) {
      return courseOptional.get();
    }
    return null;
  }

  @Override
  public Course updateOffer(Course course) {
    Course oldCourse = null;
    try {
      oldCourse = offerRepos.findById(course.getId()).get();
      standardizeCourse(course);
      ModelMapper mapper = new ModelMapper();
      mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
      mapper.map(course, oldCourse);
      oldCourse = offerRepos.save(oldCourse);
    } catch (Exception e) {
      logger.error("Error when updating data into mongodb " + e.getMessage());
    }
    return oldCourse;
  }

  @Override
  public List<CourseStatus> getListCourseStatusByType(CourseRegisterStatus courseRegisterStatus,
      String courseId) {
    try {
      Query query = new Query();
      List<CourseStatus> courseStatusList = new ArrayList<>();
      Criteria criteria = Criteria.where("id").is(courseId);
      query.addCriteria(criteria);
      query.fields().include("id", "courseStatus");
      List<Course> courseList = mongoTemplate.find(query, Course.class);
      filterCourseStatusListByType(courseList, courseStatusList, courseRegisterStatus);
      if (!courseStatusList.isEmpty()) {
        return courseStatusList;
      }
    } catch (Exception e) {
      logger.error("Error when retrieving data from mongodb " + e.getMessage());
    }
    return null;
  }

  @Override
  public CourseStatusWrap updateCourseStatus(CourseStatusWrap courseStatusWrap) {
    try {
      Course course = offerRepos.findById(courseStatusWrap.getCourseId()).get();
      List<CourseStatus> courseStatusList = course.getCourseStatusList();
      List<CourseStatus> courseStatusListUpdate = new ArrayList<>();
      // Validate input courseStatusUpdate
      if (!validateCourseStatusUpdate(courseStatusWrap)) {
        return null;
      }
      // Update element courseStatus in course
      if (courseStatusList != null && !courseStatusList.isEmpty()) {
        for (CourseStatus courseStatus : courseStatusList) {
          if (courseStatusWrap.getCourseStatus().getCourseStatusId()
              .equals(courseStatus.getCourseStatusId())) {
            courseStatusListUpdate.add(courseStatusWrap.getCourseStatus());
            continue;
          }
          courseStatusListUpdate.add(courseStatus);
        }
      }
      course.setCourseStatusList(courseStatusListUpdate);
      offerRepos.save(course);
      return courseStatusWrap;
    } catch (Exception e) {
      logger.error("Error when updating data into mongodb " + e.getMessage());
    }
    return null;
  }

  @Override
  public CourseStatusWrap createCourseStatus(CourseStatusWrap courseStatusWrap) {
    try {
      Course course = offerRepos.findById(courseStatusWrap.getCourseId()).get();
      // Validate input courseStatusUpdate
      if (!validateCourseStatusCreate(courseStatusWrap)) {
        return null;
      }
      String courseStatusId = UUID.randomUUID().toString();
      courseStatusWrap.getCourseStatus().setCourseStatusId(courseStatusId);
      courseStatusWrap.getCourseStatus().setCourseRegisterStatus(CourseRegisterStatus.PENDING);
      // Add new courseStatus for course
      saveCourseStatusIntoCourse(course, courseStatusWrap.getCourseStatus());
      offerRepos.save(course);
      return courseStatusWrap;
    } catch (Exception e) {
      logger.error("Error when inserting data into mongodb " + e.getMessage());
    }
    return null;
  }

  @Override
  public CourseContractWrap createCourseContract(CourseContractWrap courseContractWrap) {
    try {
      Course course = offerRepos.findById(courseContractWrap.getCourseId()).get();
      // Validate input courseContractCreate
      if (!validateCourseContractCreate(courseContractWrap)) {
        return null;
      }
      String courseContractId = UUID.randomUUID().toString();
      courseContractWrap.getCourseContract().setCourseContractId(courseContractId);
      courseContractWrap.getCourseContract()
          .setDateRegistry(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
      courseContractWrap.getCourseContract().setCourseRegisterStatus(CourseRegisterStatus.PENDING);
      // Add new courseContract for course
      saveCourseContractIntoCourse(course, courseContractWrap.getCourseContract());
      offerRepos.save(course);
      return courseContractWrap;
    } catch (Exception e) {
      logger.error("Error when inserting data into mongodb " + e.getMessage());
    }
    return null;
  }

  @Override
  public List<Course> getListClassByCourseTypeAndAuthorId(CourseType courseType, String authorId) {
    try {
      Query query = new Query();
      Criteria criteria = Criteria.where("courseType").is(courseType).and("profileAuthor.id")
          .is(authorId);
      query.addCriteria(criteria);
      List<Course> courses = mongoTemplate.find(query, Course.class);
      if (!courses.isEmpty()) {
        return courses;
      }
    } catch (Exception e) {
      logger.error("Error when get data from mongodb " + e.getMessage());
    }
    return null;
  }

  private void saveCourseStatusIntoCourse(Course course, CourseStatus courseStatus) {
    if (course.getCourseStatusList() != null) {
      course.getCourseStatusList().add(courseStatus);
    } else {
      course.setCourseStatusList(new ArrayList<CourseStatus>(
          Arrays.asList(courseStatus)));
    }
  }

  private void saveCourseContractIntoCourse(Course course, CourseContract courseContract) {
    if (course.getCourseContract() != null) {
      course.getCourseContract().add(courseContract);
    } else {
      course.setCourseContract(new ArrayList<CourseContract>(
          Arrays.asList(courseContract)));
    }
  }

  private boolean validateCourseStatusUpdate(CourseStatusWrap courseStatusWrap) {
    if (courseStatusWrap.getCourseStatus() == null) {
      return false;
    }
    if ("".equals(courseStatusWrap.getCourseStatus().getCourseStatusId())) {
      return false;
    }
    return true;
  }

  private boolean validateCourseStatusCreate(CourseStatusWrap courseStatusWrap) {
    if (courseStatusWrap.getCourseStatus() == null) {
      return false;
    }
    return true;
  }

  private boolean validateCourseContractCreate(CourseContractWrap courseContractWrap) {
    if (courseContractWrap.getCourseContract() == null) {
      return false;
    }
    return true;
  }

  private void standardizeCourse(Course course) {
    if ("".equals(course.getSubject())) {
      course.setSubject(null);
    }
    if ("".equals(course.getLevel())) {
      course.setLevel(null);
    }
    if ("".equals(course.getFormatLearning())) {
      course.setFormatLearning(null);
    }
    if ("".equals(course.getSalary())) {
      course.setSalary(null);
    }
    if ("".equals(course.getIntroductionAuthorOffer())) {
      course.setIntroductionAuthorOffer(null);
    }
    if ("".equals(course.getNote())) {
      course.setNote(null);
    }
    if ("".equals(course.getPreferAddress())) {
      course.setPreferAddress(null);
    }
    if (course.getProfileAuthor() != null && "".equals(course.getProfileAuthor().getId())) {
      course.setProfileAuthor(null);
    }
    if (course.getScheduleOffer() != null && "".equals(course.getScheduleOffer().getDetail()) && ""
        .equals(course.getScheduleOffer().getOverview())) {
      course.setScheduleOffer(null);
    }
    if (course.getCourseStatusList() != null && course.getCourseStatusList().isEmpty()) {
      course.setCourseStatusList(null);
    }
    if (course.getCourseContract() != null && course.getCourseContract().isEmpty()) {
      course.setCourseContract(null);
    }
  }

  private void filterCourseStatusListByType(List<Course> courseList,
      List<CourseStatus> courseStatusList, CourseRegisterStatus courseRegisterStatus) {
    if (!courseList.isEmpty() && courseList.get(0).getCourseStatusList() != null) {
      courseList.get(0).getCourseStatusList().stream().forEach(courseStatus -> {
        if (courseRegisterStatus.equals(courseStatus.getCourseRegisterStatus())) {
          courseStatusList.add(courseStatus);
        }
      });
    }
  }

  private List<Course> getOfferByPageAndSize(List<Course> unchangeSubjectListCourses, int page,
      int size) {
    List<Course> courses = new ArrayList<>();
    int startPoint = page * size;
    int count = startPoint;
    while (unchangeSubjectListCourses.size() > startPoint && count < unchangeSubjectListCourses
        .size()) {
      courses.add(unchangeSubjectListCourses.get(count));
      count++;
    }
    return courses;
  }

  private String nonAccentVietnamese(String wordVietnamese) {
    wordVietnamese = Normalizer.normalize(wordVietnamese, Normalizer.Form.NFKD);
    wordVietnamese = wordVietnamese.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    return wordVietnamese;
  }

  private void mergeListsOrderIntoSet(List<Course> unchangeSubjectListOffersInPageAndSize,
      List<Course> changedSubjectListCourses, Set<Course> courses) {
    unchangeSubjectListOffersInPageAndSize.forEach(offer -> {
      courses.add(offer);
    });
    changedSubjectListCourses.forEach(offer -> {
      courses.add(offer);
    });
  }

}
