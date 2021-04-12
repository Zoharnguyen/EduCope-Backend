package com.zohar.educope.dto;

import com.zohar.educope.constant.CourseRegisterStatus;

public class CourseStatus {

    private String courseStatusId;
    private UserProfile userProfile;
    private CourseRegisterStatus courseRegisterStatus;
    private String reason;

    public String getCourseStatusId() {
        return courseStatusId;
    }

    public void setCourseStatusId(String courseStatusId) {
        this.courseStatusId = courseStatusId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public CourseRegisterStatus getCourseRegisterStatus() {
        return courseRegisterStatus;
    }

    public void setCourseRegisterStatus(CourseRegisterStatus courseRegisterStatus) {
        this.courseRegisterStatus = courseRegisterStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
