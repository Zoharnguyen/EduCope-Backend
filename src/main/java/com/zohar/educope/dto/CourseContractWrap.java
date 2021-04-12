package com.zohar.educope.dto;

public class CourseContractWrap {

  private String courseId;

  private CourseContract courseContract;

  public String getCourseId() {
    return courseId;
  }

  public void setCourseId(String courseId) {
    this.courseId = courseId;
  }

  public CourseContract getCourseContract() {
    return courseContract;
  }

  public void setCourseContract(CourseContract courseContract) {
    this.courseContract = courseContract;
  }
}
