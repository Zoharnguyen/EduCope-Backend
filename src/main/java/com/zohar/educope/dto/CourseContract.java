package com.zohar.educope.dto;

import com.zohar.educope.constant.CourseRegisterStatus;

public class CourseContract {

  private String courseContractId;
  private String subject;
  private String salary;
  private String paymentDate;
  private String formatLearning;
  private String note;
  private UserProfile userProfileCreate;
  private UserProfile userProfileRegistry;
  private String dateRegistry;
  private CourseRegisterStatus courseRegisterStatus;

  public String getCourseContractId() {
    return courseContractId;
  }

  public void setCourseContractId(String courseContractId) {
    this.courseContractId = courseContractId;
  }

  public CourseRegisterStatus getCourseRegisterStatus() {
    return courseRegisterStatus;
  }

  public void setCourseRegisterStatus(CourseRegisterStatus courseRegisterStatus) {
    this.courseRegisterStatus = courseRegisterStatus;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getSalary() {
    return salary;
  }

  public void setSalary(String salary) {
    this.salary = salary;
  }

  public String getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(String paymentDate) {
    this.paymentDate = paymentDate;
  }

  public String getFormatLearning() {
    return formatLearning;
  }

  public void setFormatLearning(String formatLearning) {
    this.formatLearning = formatLearning;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public UserProfile getUserProfileCreate() {
    return userProfileCreate;
  }

  public void setUserProfileCreate(UserProfile userProfileCreate) {
    this.userProfileCreate = userProfileCreate;
  }

  public UserProfile getUserProfileRegistry() {
    return userProfileRegistry;
  }

  public void setUserProfileRegistry(UserProfile userProfileRegistry) {
    this.userProfileRegistry = userProfileRegistry;
  }

  public String getDateRegistry() {
    return dateRegistry;
  }

  public void setDateRegistry(String dateRegistry) {
    this.dateRegistry = dateRegistry;
  }

}
