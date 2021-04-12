package com.zohar.educope.service.common.impl;

import com.zohar.educope.dto.TokenResponse;
import com.zohar.educope.dto.UserBasic;
import com.zohar.educope.entity.User;
import com.zohar.educope.repository.UserRepos;
import com.zohar.educope.service.security.JWTService;
import com.zohar.educope.service.security.JWTUserDetailsService;
import com.zohar.educope.service.security.PasswordSecurity;
import com.zohar.educope.service.common.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepos userRepos;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private JWTUserDetailsService jwtUserDetailsService;

    @Override
    public User createUser(User user) {
        PasswordSecurity passwordSecurity = new PasswordSecurity();
        user.setPassword(passwordSecurity.hashPasword(user.getPassword()));
        User userResponse = null;
        try {
            userResponse = userRepos.insert(user);
        } catch (Exception e) {
            logger.error("Error when inserting data into mongodb " + e.getMessage());
        }
        return userResponse;
    }

    @Override
    public TokenResponse login(UserBasic userBasic) {
        TokenResponse tokenResponse = null;
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        // Get UserDetails by username
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userBasic.getGmail());
        if(bCrypt.matches(userBasic.getPassword(), userDetails.getPassword())) {
            tokenResponse = new TokenResponse(jwtService.generateToken(userDetails));
        }
        return tokenResponse;
    }

}
