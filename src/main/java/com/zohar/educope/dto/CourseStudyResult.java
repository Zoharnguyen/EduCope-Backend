package com.zohar.educope.dto;

import java.io.File;
import java.util.List;

public class CourseStudyResult {


    private List<File> testResults;
    private List<CourseStudent> courseStudents;

    public List<File> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<File> testResults) {
        this.testResults = testResults;
    }

    public List<CourseStudent> getCourseStudents() {
        return courseStudents;
    }

    public void setCourseStudents(List<CourseStudent> courseStudents) {
        this.courseStudents = courseStudents;
    }
}
