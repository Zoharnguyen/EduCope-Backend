package com.zohar.educope.repository;

import com.zohar.educope.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepos extends MongoRepository<User, String> {

    User findByGmailAndPassword(String gmail, String password);

    User findByGmail(String gmail);

}
