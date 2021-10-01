package com.zohar.educope.service.common.impl;

import com.zohar.educope.constant.CourseRegisterStatus;
import com.zohar.educope.constant.CourseType;
import com.zohar.educope.constant.OfferType;
import com.zohar.educope.dto.CourseContract;
import com.zohar.educope.dto.CourseContractWrap;
import com.zohar.educope.dto.CourseDTO;
import com.zohar.educope.dto.CourseStatus;
import com.zohar.educope.dto.CourseStatusWrap;
import com.zohar.educope.dto.LocalFile;
import com.zohar.educope.dto.UserProfile;
import com.zohar.educope.entity.Course;
import com.zohar.educope.repository.OfferRepos;
import com.zohar.educope.service.common.FileService;
import com.zohar.educope.service.common.OfferService;
import com.zohar.educope.service.common.UserService;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class OfferServiceImpl implements OfferService {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private static List<String> randomListImageId = new ArrayList<>(
      List.of("614e8975a123b945c5fa7a80", "614e8ae6a123b945c5fa7aa0"));

  @Autowired
  OfferRepos offerRepos;

  @Autowired
  UserService userService;

  @Autowired
  MongoTemplate mongoTemplate;

  @Autowired
  FileService fileService;

  @Override
  public Course createOffer(CourseDTO course) {
    Course courseResponse = null;
    String idAuthor = course.getProfileAuthor().getId();
    UserProfile userProfile = userService.getUserProfile(idAuthor, true);
    if (userProfile != null) {
      //Get and insert image to course.
      if (!StringUtils.isEmpty(userProfile.getUrlImageProfile())) {
        try {
          LocalFile imageFile = fileService.getFileById(userProfile.getUrlImageProfile());
          String contentImageFile = Base64.getEncoder().encodeToString(imageFile.getFile());
          userProfile.setUrlImageProfile(contentImageFile);
        } catch (Exception e) {
          logger.error("Error when retrieve and insert image to course", e);
        }
      }
      course.setProfileAuthor(userProfile);
    }
    // Insert random image for course.
    if (!CollectionUtils.isEmpty(randomListImageId)) {
      int index = getRandomInRange(0, randomListImageId.size());
      logger.debug("ImageId will be inserted into course: {}", randomListImageId.get(index));
      LocalFile imageFile = fileService.getFileById(randomListImageId.get(index));
      String contentImageFile = Base64.getEncoder().encodeToString(imageFile.getFile());
      course.setCourseImage(contentImageFile);
    }
    try {
      courseResponse = offerRepos.insert(convertCourseDTOToCourse(course));
    } catch (Exception e) {
      logger.error("Error when inserting data into mongodb " + e.getMessage());
    }
    return courseResponse;
  }

  @Override
  public List<CourseDTO> getListOfferByType(OfferType offerType) {
    List<Course> courses = offerRepos.findOffersByOfferType(offerType);
    List<CourseDTO> courseDTOS = new ArrayList<>();
    // Convert List course to List courseDTO
    if (!CollectionUtils.isEmpty(courses)) {
      courses.stream().forEach(course -> {
        courseDTOS.add(convertCourseToCourseDTO(course));
      });
    }
    Collections.sort(courseDTOS, Collections.reverseOrder());
    return courseDTOS;
  }

  @Override
  public Set<CourseDTO> getListOfferByTypeAndSubject(OfferType offerType, String subject, int page,
      int size) {
    Set<CourseDTO> courseDTOS = new HashSet<>();
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
    if (!CollectionUtils.isEmpty(courses)) {
      courses.stream().forEach(course -> {
        courseDTOS.add(convertCourseToCourseDTO(course));
      });
    }
    return courseDTOS;
  }

  @Override
  public CourseDTO getCourseById(String courseId) {
    Optional<Course> courseOptional = offerRepos.findById(courseId);
    if (courseOptional.isPresent()) {
      CourseDTO courseDTO = convertCourseToCourseDTO(courseOptional.get());
      return courseDTO;
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
        // Get information of userProfile for courseStatus
        for (int i = 0; i < courseStatusList.size(); i++) {
          courseStatusList.get(i)
              .setUserProfile(getUserProfileForCourseStatus(courseStatusList.get(i)));
        }
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
      List<CourseStatus> courseStatusList = course.getCourseStatus();
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
      course.setCourseStatus(courseStatusListUpdate);
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
      // Get list course of user who own this course or registered this course
      Criteria criteria = Criteria.where("courseType").is(courseType)
          .orOperator(Criteria.where("profileAuthor.id").is(authorId),
              Criteria.where("courseStatus.userProfile.id").is(authorId)
          );

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

  @Override
  public CourseContractWrap getContractByCourseId(String courseId) {
    try {
      Optional<Course> courseOptional = offerRepos.findById(courseId);
      if (courseOptional.isPresent()) {
        CourseContractWrap courseContractWrap = new CourseContractWrap();
        courseContractWrap.setCourseId(courseId);
        // Get first courseContract.
        courseContractWrap.setCourseContract(courseOptional.get().getCourseContract().get(0));
        return courseContractWrap;
      }
    } catch (Exception e) {
      logger.error("Error when get data from mongodb " + e.getMessage());
    }
    return null;
  }

  @Override
  public CourseContractWrap updateCourseContract(CourseContractWrap courseContractWrap) {
    try {
      Course course = offerRepos.findById(courseContractWrap.getCourseId()).get();
      List<CourseContract> courseContractList = course.getCourseContract();
      List<CourseContract> courseContractListUpdate = new ArrayList<>();
      // Validate input courseStatusUpdate
      if (!validateCourseContractCreate(courseContractWrap)) {
        return null;
      }
      // Update element courseStatus in course
      if (courseContractList != null && !courseContractList.isEmpty()) {
        for (CourseContract courseContract : courseContractList) {
          if (courseContractWrap.getCourseContract().getCourseContractId()
              .equals(courseContract.getCourseContractId())) {
            courseContractListUpdate.add(courseContractWrap.getCourseContract());
            if (course.getCourseType() != CourseType.LEARNING
                && courseContractWrap.getCourseContract().getCourseRegisterStatus()
                == CourseRegisterStatus.ACCEPT) {
              course.setCourseType(CourseType.LEARNING);
              if (CollectionUtils.isEmpty(course.getMemberClassList())) {
                course.setMemberClassList(new ArrayList<>());
              }
              // Add user in courseContract to memList
              course.getMemberClassList()
                  .add(courseContractWrap.getCourseContract().getUserProfileRegistry());
              course.getMemberClassList()
                  .add(courseContractWrap.getCourseContract().getUserProfileCreate());
            }
            continue;
          }
          courseContractListUpdate.add(courseContract);
        }
      }
      course.setCourseContract(courseContractListUpdate);
      offerRepos.save(course);
      return courseContractWrap;
    } catch (Exception e) {
      logger.error("Error when updating data into mongodb " + e.getMessage());
    }
    return null;
  }

  private void saveCourseStatusIntoCourse(Course course, CourseStatus courseStatus) {
    if (course.getCourseStatus() != null) {
      course.getCourseStatus().add(courseStatus);
    } else {
      course.setCourseStatus(new ArrayList<CourseStatus>(
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
    if (course.getProfileAuthorId() != null && "".equals(course.getProfileAuthorId())) {
      course.setProfileAuthorId(null);
    }
    if (course.getScheduleOffer() != null && "".equals(course.getScheduleOffer().getDetail()) && ""
        .equals(course.getScheduleOffer().getOverview())) {
      course.setScheduleOffer(null);
    }
    if (course.getCourseStatus() != null && course.getCourseStatus().isEmpty()) {
      course.setCourseStatus(null);
    }
    if (course.getCourseContract() != null && course.getCourseContract().isEmpty()) {
      course.setCourseContract(null);
    }
  }

  private void filterCourseStatusListByType(List<Course> courseList,
      List<CourseStatus> courseStatusList, CourseRegisterStatus courseRegisterStatus) {
    if (!courseList.isEmpty() && courseList.get(0).getCourseStatus() != null) {
      courseList.get(0).getCourseStatus().stream().forEach(courseStatus -> {
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

  /**
   * Random int in range[min,max)
   */
  private int getRandomInRange(int min, int max) {
    Random random = new Random();
    return random.ints(min, max)
        .findFirst()
        .getAsInt();
  }

  private Course convertCourseDTOToCourse(CourseDTO courseDTO) {
    Course course = new Course();
    if (Objects.nonNull(courseDTO)) {
      ModelMapper mapper = new ModelMapper();
      mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
      // Mapper similar name fields of courseDTO to course
      mapper.map(courseDTO, course);
      // Add authorID from courseDTO to course
      if (Objects.nonNull(courseDTO.getProfileAuthor())) {
        course.setProfileAuthorId(courseDTO.getProfileAuthor().getId());
      }
    }
    return course;
  }

  private CourseDTO convertCourseToCourseDTO(Course course) {
    CourseDTO courseDTO = new CourseDTO();
    if (Objects.nonNull(course)) {
      ModelMapper mapper = new ModelMapper();
      mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
      // Mapper similar name fields of courseDTO to course
      mapper.map(course, courseDTO);
      // Add authorProfile from authorID of course to courseDTO
      if (Objects.nonNull(course.getProfileAuthorId())) {
        UserProfile userProfile = userService.getUserProfile(course.getProfileAuthorId(), true);
        courseDTO.setProfileAuthor(userProfile);
      }
    }
    return courseDTO;
  }

  private UserProfile getUserProfileForCourseStatus(CourseStatus courseStatus) {
    if (Objects.nonNull(courseStatus)) {
      if (Objects.nonNull(courseStatus.getUserProfile()) && !StringUtils.isEmpty(
          courseStatus.getUserProfile().getId())) {
        return userService.getUserProfile(courseStatus.getUserProfile().getId(), true);
      }
      return courseStatus.getUserProfile();
    }
    return null;
  }

}
