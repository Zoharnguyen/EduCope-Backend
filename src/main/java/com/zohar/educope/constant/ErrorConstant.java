package com.zohar.educope.constant;

import com.zohar.educope.dto.ErrorResponse;

public class ErrorConstant {

    public static final ErrorResponse Err001 = new ErrorResponse("Err001","Account doesn't exist in system");
    public static final ErrorResponse Err002 = new ErrorResponse("Err002", "Error when inserting data into mongodb");

}
