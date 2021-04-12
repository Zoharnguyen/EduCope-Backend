package com.zohar.educope.service.common.impl;

import com.zohar.educope.constant.CourseRegisterStatus;
import com.zohar.educope.entity.AttendanceCourse;
import com.zohar.educope.repository.AttendanceCourseRepos;
import com.zohar.educope.service.common.AttendanceCourseService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AttendanceCourseServiceImpl implements AttendanceCourseService {

  @Autowired
  private AttendanceCourseRepos attendanceCourseRepos;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public AttendanceCourse createAttendanceCourse(AttendanceCourse attendanceCourse) {
    try {
      // Validate input attendanceCourse
      if (!validateAttendanceCourse(attendanceCourse)) {
        return null;
      }
      attendanceCourse.setDateStudy(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
      attendanceCourse.setAttendanceCourseStatus(CourseRegisterStatus.PENDING);
      return attendanceCourseRepos.insert(attendanceCourse);
    } catch (Exception e) {
      log.error("Error when inserting data into mongodb " + e.getMessage());
    }
    return null;
  }

  @Override
  public List<AttendanceCourse> getAttendanceCourseListByTypeAndCourseId(
      CourseRegisterStatus attendanceCourseStatus, String courseId) {
    try {
      Query query = new Query();
      Criteria criteria = Criteria.where("courseId").is(courseId).and("attendanceCourseStatus")
          .is(attendanceCourseStatus);
      query.addCriteria(criteria);
      List<AttendanceCourse> attendanceCourseList = mongoTemplate
          .find(query, AttendanceCourse.class);
      if (!attendanceCourseList.isEmpty()) {
        return attendanceCourseList;
      }
    } catch (Exception e) {
      log.error("Error when retrieving data from mongodb " + e.getMessage());
    }
    return null;
  }

  @Override
  public AttendanceCourse updateAttendanceStatus(AttendanceCourse attendanceCourse) {
    try {
      Optional<AttendanceCourse> attendanceCourseDBOptional = attendanceCourseRepos.findById(attendanceCourse.getId());
      if(attendanceCourseDBOptional.isPresent()) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(attendanceCourse, attendanceCourseDBOptional.get());
        return attendanceCourseRepos.save(attendanceCourseDBOptional.get());
      }
    } catch (Exception e) {
      log.error("Error when retrieving data from mongodb " + e.getMessage());
    }
    return null;
  }

  private boolean validateAttendanceCourse(AttendanceCourse attendanceCourse) {
    return true;
  }

}
