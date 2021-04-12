package com.zohar.educope.dto;

import java.util.List;

public class CourseStudent {

    private String courseStudentId;
    private UserProfile userProfile;
    private List<AdjustStudentAfterExam> adjustStudentAfterExam;

    public String getCourseStudentId() {
        return courseStudentId;
    }

    public void setCourseStudentId(String courseStudentId) {
        this.courseStudentId = courseStudentId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public List<AdjustStudentAfterExam> getAdjustStudentAfterExam() {
        return adjustStudentAfterExam;
    }

    public void setAdjustStudentAfterExam(List<AdjustStudentAfterExam> adjustStudentAfterExam) {
        this.adjustStudentAfterExam = adjustStudentAfterExam;
    }
}
