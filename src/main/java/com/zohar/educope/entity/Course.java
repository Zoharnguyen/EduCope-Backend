package com.zohar.educope.entity;

import com.zohar.educope.constant.CourseType;
import com.zohar.educope.constant.OfferType;
import com.zohar.educope.dto.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "offer")
public class Course implements Comparable<Course> {

    @Id
    private String id;

    @Field(name = "offerType")
    private OfferType offerType;

    @Field(name = "subject")
    private String subject;

    @Field(name = "level")
    private String level;

    @Field(name = "formatLearning")
    private String formatLearning;

    @Field(name = "salary")
    private String salary;

    @Field(name = "introductionAuthorOffer")
    private String introductionAuthorOffer;

    @Field(name = "note")
    private String note;

    @Field(name = "profileAuthor")
    private UserProfile profileAuthor;

    @Field(name = "scheduleOffer")
    private ScheduleOffer scheduleOffer;

    @Field(name = "preferAddress")
    private String preferAddress;

    @Field(name = "courseType")
    private CourseType courseType;

    @Field(name = "courseStatus")
    private List<CourseStatus> courseStatusList;

    @Field(name = "courseContract")
    private List<CourseContract> courseContract;

    @Field(name = "courseStudyResult")
    private CourseStudyResult courseStudyResult;

    @Field(name = "memberClassList")
    private List<UserProfile> memberClassList;

    public List<UserProfile> getMemberClassList() {
        return memberClassList;
    }

    public void setMemberClassList(List<UserProfile> memberClassList) {
        this.memberClassList = memberClassList;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public List<CourseStatus> getCourseStatusList() {
        return courseStatusList;
    }

    public void setCourseStatusList(List<CourseStatus> courseStatusList) {
        this.courseStatusList = courseStatusList;
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

    public String getPreferAddress() {
        return preferAddress;
    }

    public void setPreferAddress(String preferAddress) {
        this.preferAddress = preferAddress;
    }

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

    @Override
    public int compareTo(Course course) {
        if (this.getProfileAuthor().getRate() == null) {
            return 0;
        } else if (course.getProfileAuthor().getRate() == null
                || (this.getProfileAuthor().getRate() == null && course.getProfileAuthor().getRate() == null)) {
            return 1;
        } else {
            return this.getProfileAuthor().getRate().compareTo(course.getProfileAuthor().getRate());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Course) {
            if (((Course) o).getId() != null
                    && this.getId() != null)
                return ((Course) o).getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
