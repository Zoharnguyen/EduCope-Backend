package com.zohar.educope.controller;

import com.zohar.educope.constant.CourseRegisterStatus;
import com.zohar.educope.constant.ErrorConstant;
import com.zohar.educope.dto.CourseStatus;
import com.zohar.educope.dto.CourseStatusWrap;
import com.zohar.educope.entity.AttendanceCourse;
import com.zohar.educope.service.common.AttendanceCourseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course-attendance")
public class AttendanceCourseController {

  @Autowired
  private AttendanceCourseService attendanceCourseService;

  // Add new attendanceCourse for course
  @PostMapping("/create")
  public ResponseEntity createAttendanceCourse(@RequestBody AttendanceCourse attendanceCourse) {
    AttendanceCourse response = attendanceCourseService.createAttendanceCourse(attendanceCourse);
    ResponseEntity responseEntity;
    if (response != null) {
      responseEntity = new ResponseEntity(response, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  // Get list attendanceCourse by attendanceType and courseId
  @GetMapping("/get-attendance-course-by-type-and-course-id")
  public ResponseEntity getAttendanceCourseListByTypeAndCourseId(
      @RequestParam(value = "attendanceCourseStatus", required = true) CourseRegisterStatus attendanceCourseStatus,
      @RequestParam(value = "courseId", required = true) String courseId) {
    List<AttendanceCourse> response = attendanceCourseService
        .getAttendanceCourseListByTypeAndCourseId(attendanceCourseStatus, courseId);
    ResponseEntity responseEntity;
    if (response != null) {
      responseEntity = new ResponseEntity(response, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

  // Update courseStatus of course
  @PatchMapping("/update-attendance-status")
  public ResponseEntity updateAttendanceStatus(@RequestBody AttendanceCourse attendanceCourse) {
    AttendanceCourse response = attendanceCourseService.updateAttendanceStatus(attendanceCourse);
    ResponseEntity responseEntity;
    if (response != null) {
      responseEntity = new ResponseEntity(response, HttpStatus.OK);
    } else {
      responseEntity = new ResponseEntity(ErrorConstant.Err002, HttpStatus.BAD_REQUEST);
    }
    return responseEntity;
  }

}
