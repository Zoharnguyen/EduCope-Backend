package com.zohar.educope.utils;

import org.bson.types.ObjectId;

public class UtilService {

    public static ObjectId convertStringToObjectId(String id) {
        return new ObjectId(id);
    }

}
