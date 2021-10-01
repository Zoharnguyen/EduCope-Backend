package com.zohar.educope.repository;

import com.zohar.educope.entity.AttendanceCourse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceCourseRepos extends MongoRepository<AttendanceCourse, String> {

}
