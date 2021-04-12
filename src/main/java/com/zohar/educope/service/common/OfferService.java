package com.zohar.educope.service.common;

import com.zohar.educope.constant.CourseRegisterStatus;
import com.zohar.educope.constant.CourseType;
import com.zohar.educope.constant.OfferType;
import com.zohar.educope.dto.CourseContractWrap;
import com.zohar.educope.dto.CourseStatus;
import com.zohar.educope.dto.CourseStatusWrap;
import com.zohar.educope.entity.Course;
import java.util.List;
import java.util.Set;

public interface OfferService {

  Course createOffer(Course course);

  List<Course> getListOfferByType(OfferType offerType);

  Set<Course> getListOfferByTypeAndSubject(OfferType offerType, String subject, int page, int size);

  Course getCourseById(String courseId);

  Course updateOffer(Course course);

  List<CourseStatus> getListCourseStatusByType(CourseRegisterStatus courseRegisterStatus,
      String courseId);

  CourseStatusWrap updateCourseStatus(CourseStatusWrap courseStatusWrap);

  CourseStatusWrap createCourseStatus(CourseStatusWrap courseStatusWrap);

  CourseContractWrap createCourseContract(CourseContractWrap courseContractWrap);

  List<Course> getListClassByCourseTypeAndAuthorId(CourseType courseType, String userId);

}
