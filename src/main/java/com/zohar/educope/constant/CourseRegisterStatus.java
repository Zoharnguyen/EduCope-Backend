package com.zohar.educope.constant;

public enum CourseRegisterStatus {

    PENDING("Pending"),
    ACCEPT("Accept"),
    REJECT("Reject");

    private String value;

    CourseRegisterStatus(String value) {
        this.value = value;
    }

}
