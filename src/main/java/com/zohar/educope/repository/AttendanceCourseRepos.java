package com.zohar.educope.repository;

import com.zohar.educope.entity.AttendanceCourse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AttendanceCourseRepos extends MongoRepository<AttendanceCourse, String> {

}
