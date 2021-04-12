package com.zohar.educope.service.common;

import com.zohar.educope.dto.TokenResponse;
import com.zohar.educope.dto.UserBasic;
import com.zohar.educope.entity.User;

public interface UserService {

    public User createUser(User user);

    public TokenResponse login(UserBasic userBasic);

}
