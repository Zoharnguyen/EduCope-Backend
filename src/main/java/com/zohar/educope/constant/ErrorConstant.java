package com.zohar.educope.constant;

import com.zohar.educope.dto.ErrorResponse;

public class ErrorConstant {

    public static final ErrorResponse Err001 = new ErrorResponse("Err001","Account doesn't exist in system");
    public static final ErrorResponse Err002 = new ErrorResponse("Err002", "Error when inserting data into mongodb");
    public static final ErrorResponse Err003 = new ErrorResponse("Err003", "Find no user map to this userId");
    public static final ErrorResponse Err004 = new ErrorResponse("Err004", "Error when upload files");
    public static final ErrorResponse Err005 = new ErrorResponse("Err005", "Error when get file from mongodb");
    public static final ErrorResponse Err006 = new ErrorResponse("Err005", "Error when get offer document from mongodb");

}
