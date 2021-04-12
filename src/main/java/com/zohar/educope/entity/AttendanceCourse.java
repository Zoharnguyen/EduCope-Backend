package com.zohar.educope.entity;

import com.zohar.educope.constant.CourseRegisterStatus;
import com.zohar.educope.dto.UserProfile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(value = "attendanceCourse")
public class AttendanceCourse {

  @Id
  private String id;

  @Field(name = "courseId")
  private String courseId;

  @Field(name = "dateStudy")
  private String dateStudy;

  @Field(name = "attendanceCourseStatus")
  private CourseRegisterStatus attendanceCourseStatus;

  @Field(name = "timeStudy")
  private String timeStudy;

  @Field(name = "note")
  private String note;

  @Field(name = "userAttendance")
  private UserProfile userAttendance;

  @Field(name = "userConfirm")
  private UserProfile userConfirm;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCourseId() {
    return courseId;
  }

  public void setCourseId(String courseId) {
    this.courseId = courseId;
  }

  public String getDateStudy() {
    return dateStudy;
  }

  public void setDateStudy(String dateStudy) {
    this.dateStudy = dateStudy;
  }

  public CourseRegisterStatus getAttendanceCourseStatus() {
    return attendanceCourseStatus;
  }

  public void setAttendanceCourseStatus(
      CourseRegisterStatus attendanceCourseStatus) {
    this.attendanceCourseStatus = attendanceCourseStatus;
  }

  public String getTimeStudy() {
    return timeStudy;
  }

  public void setTimeStudy(String timeStudy) {
    this.timeStudy = timeStudy;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public UserProfile getUserAttendance() {
    return userAttendance;
  }

  public void setUserAttendance(UserProfile userAttendance) {
    this.userAttendance = userAttendance;
  }

  public UserProfile getUserConfirm() {
    return userConfirm;
  }

  public void setUserConfirm(UserProfile userConfirm) {
    this.userConfirm = userConfirm;
  }
}
