package com.zohar.educope.dto;

import com.zohar.educope.constant.CourseType;
import com.zohar.educope.constant.OfferType;
import java.util.List;

public class CourseDTO implements Comparable<CourseDTO> {

  private String id;

  private OfferType offerType;

  private String subject;

  private String level;

  private String formatLearning;

  private String salary;

  private String introductionAuthorOffer;

  private String note;

  private UserProfile profileAuthor;

  private ScheduleOffer scheduleOffer;

  private String preferAddress;

  private CourseType courseType;

  private List<CourseStatus> courseStatus;

  private List<CourseContract> courseContract;

  private CourseStudyResult courseStudyResult;

  private List<UserProfile> memberClassList;

  private String courseImage;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public OfferType getOfferType() {
    return offerType;
  }

  public void setOfferType(OfferType offerType) {
    this.offerType = offerType;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getFormatLearning() {
    return formatLearning;
  }

  public void setFormatLearning(String formatLearning) {
    this.formatLearning = formatLearning;
  }

  public String getSalary() {
    return salary;
  }

  public void setSalary(String salary) {
    this.salary = salary;
  }

  public String getIntroductionAuthorOffer() {
    return introductionAuthorOffer;
  }

  public void setIntroductionAuthorOffer(String introductionAuthorOffer) {
    this.introductionAuthorOffer = introductionAuthorOffer;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public UserProfile getProfileAuthor() {
    return profileAuthor;
  }

  public void setProfileAuthor(UserProfile profileAuthor) {
    this.profileAuthor = profileAuthor;
  }

  public ScheduleOffer getScheduleOffer() {
    return scheduleOffer;
  }

  public void setScheduleOffer(ScheduleOffer scheduleOffer) {
    this.scheduleOffer = scheduleOffer;
  }

  public String getPreferAddress() {
    return preferAddress;
  }

  public void setPreferAddress(String preferAddress) {
    this.preferAddress = preferAddress;
  }

  public CourseType getCourseType() {
    return courseType;
  }

  public void setCourseType(CourseType courseType) {
    this.courseType = courseType;
  }

  public List<CourseStatus> getCourseStatus() {
    return courseStatus;
  }

  public void setCourseStatus(List<CourseStatus> courseStatus) {
    this.courseStatus = courseStatus;
  }

  public List<CourseContract> getCourseContract() {
    return courseContract;
  }

  public void setCourseContract(List<CourseContract> courseContract) {
    this.courseContract = courseContract;
  }

  public CourseStudyResult getCourseStudyResult() {
    return courseStudyResult;
  }

  public void setCourseStudyResult(CourseStudyResult courseStudyResult) {
    this.courseStudyResult = courseStudyResult;
  }

  public List<UserProfile> getMemberClassList() {
    return memberClassList;
  }

  public void setMemberClassList(List<UserProfile> memberClassList) {
    this.memberClassList = memberClassList;
  }

  public String getCourseImage() {
    return courseImage;
  }

  public void setCourseImage(String courseImage) {
    this.courseImage = courseImage;
  }

  @Override
  public int compareTo(CourseDTO course) {
    if (this.getProfileAuthor().getRate() == null) {
      return 0;
    } else if (course.getProfileAuthor().getRate() == null
        || (this.getProfileAuthor().getRate() == null
        && course.getProfileAuthor().getRate() == null)) {
      return 1;
    } else {
      return this.getProfileAuthor().getRate().compareTo(course.getProfileAuthor().getRate());
    }
  }

}
