package com.zohar.educope.repository;

import com.zohar.educope.constant.OfferType;
import com.zohar.educope.entity.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepos extends MongoRepository<Course, String> {

    List<Course> findOffersByOfferType(OfferType offerType);

    List<Course> findOffersByOfferTypeAndSubjectContainingIgnoreCase(OfferType offerType, String subject, Pageable pageable);

}
