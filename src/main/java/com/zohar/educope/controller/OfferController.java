package com.zohar.educope.controller;

import com.zohar.educope.constant.CourseRegisterStatus;
import com.zohar.educope.constant.CourseType;
import com.zohar.educope.constant.ErrorConstant;
import com.zohar.educope.constant.OfferType;
import com.zohar.educope.dto.CourseContractWrap;
import com.zohar.educope.dto.CourseDTO;
import com.zohar.educope.dto.CourseStatus;
import com.zohar.educope.dto.CourseStatusWrap;
import com.zohar.educope.entity.Course;
import com.zohar.educope.service.common.OfferService;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/offer")
public class OfferController {

  @Autowired
  OfferService offerService;

  @PostMapping("/create")
  public ResponseEntity createOffer(@RequestBody CourseDTO course) {
    Course courseResponse = offerService.createOffer(course);
    ResponseEntity responseEntity;
    if (courseResponse != null) {
      responseEntity = new ResponseEntity(courseResponse, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  @GetMapping("/get-list")
  public ResponseEntity getListOfferByType(
      @RequestParam(value = "offerType", required = true) OfferType offerType) {
    List<CourseDTO> offersResponse = offerService.getListOfferByType(offerType);
    ResponseEntity responseEntity;
    if (offersResponse != null) {
      responseEntity = new ResponseEntity(offersResponse, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  @GetMapping("/get-courses-by-type-and-subject")
  public ResponseEntity getListOfferByTypeAndSubject(
      @RequestParam(value = "offerType", required = true) OfferType offerType,
      @RequestParam(value = "subject", required = true) String subject,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    Set<CourseDTO> offersResponse = offerService
        .getListOfferByTypeAndSubject(offerType, subject, page, size);
    ResponseEntity responseEntity;
    if (!offersResponse.isEmpty()) {
      responseEntity = new ResponseEntity(offersResponse, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  @GetMapping("/get-course-by-id")
  public ResponseEntity getCourseById(
      @RequestParam(value = "courseId", required = true) String courseId) {
    CourseDTO offersResponse = offerService.getCourseById(courseId);
    ResponseEntity responseEntity;
    if (offersResponse != null) {
      responseEntity = new ResponseEntity(offersResponse, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err006, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  // Update entire of course
  @PatchMapping("/update")
  public ResponseEntity updateOffer(@RequestBody Course course) {
    Course courseResponse = offerService.updateOffer(course);
    ResponseEntity responseEntity;
    if (courseResponse != null) {
      responseEntity = new ResponseEntity(courseResponse, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  // Get list course status by courseStatusType and courseId
  @GetMapping("/get-course-status-by-type-and-course-id")
  public ResponseEntity getListCourseStatusByTypeAndCourseId(
      @RequestParam(value = "courseStatus", required = true) CourseRegisterStatus courseRegisterStatus,
      @RequestParam(value = "courseId", required = true) String courseId) {
    List<CourseStatus> offersResponse = offerService
        .getListCourseStatusByType(courseRegisterStatus, courseId);
    ResponseEntity responseEntity;
    if (offersResponse != null) {
      responseEntity = new ResponseEntity(offersResponse, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  // Update courseStatus of course
  @PatchMapping("/update-course-status")
  public ResponseEntity updateCourseStatus(@RequestBody CourseStatusWrap courseStatusWrap) {
    CourseStatusWrap response = offerService.updateCourseStatus(courseStatusWrap);
    ResponseEntity responseEntity;
    if (response != null) {
      responseEntity = new ResponseEntity(response, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  // Add new courseStatus for course
  @PostMapping("/create-course-status")
  public ResponseEntity createCourseStatus(@RequestBody CourseStatusWrap courseStatusWrap) {
    CourseStatusWrap response = offerService.createCourseStatus(courseStatusWrap);
    ResponseEntity responseEntity;
    if (response != null) {
      responseEntity = new ResponseEntity(response, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  // Add new contract for course
  @PostMapping("/create-course-contract")
  public ResponseEntity createCourseContract(@RequestBody CourseContractWrap courseContractWrap) {
    CourseContractWrap response = offerService.createCourseContract(courseContractWrap);
    ResponseEntity responseEntity;
    if (response != null) {
      responseEntity = new ResponseEntity(response, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  // Get list class of specific User.
  @GetMapping("/get-courses-by-course-type-and-author-id")
  public ResponseEntity getListClassByCourseTypeAndAuthorId(@RequestParam CourseType courseType,
      @RequestParam String authorId) {
    List<Course> response = offerService.getListClassByCourseTypeAndAuthorId(courseType, authorId);
    ResponseEntity responseEntity;
    if (!CollectionUtils.isEmpty(response)) {
      responseEntity = new ResponseEntity(response, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  // Get list class of specific User.
  @GetMapping("/get-contract-by-course-id")
  public ResponseEntity getContractByCourseId(@RequestParam String courseId) {
    CourseContractWrap response = offerService.getContractByCourseId(courseId);
    ResponseEntity responseEntity;
    if (!Objects.isNull(response)) {
      responseEntity = new ResponseEntity(response, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  // Update courseStatus of course
  @PatchMapping("/update-course-contract")
  public ResponseEntity updateCourseContract(@RequestBody CourseContractWrap courseContractWrap) {
    CourseContractWrap response = offerService.updateCourseContract(courseContractWrap);
    ResponseEntity responseEntity;
    if (response != null) {
      responseEntity = new ResponseEntity(response, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

}
