package com.zohar.educope.service.security;

import com.zohar.educope.entity.User;
import com.zohar.educope.repository.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JWTUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepos userRepos;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepos.findByGmail(username);
        UserDetails userDetails = null;
        if(user != null){
            userDetails = new org.springframework.security.core.userdetails.User(user.getGmail(), user.getPassword(), new ArrayList<>());
        } else {
            userDetails = new org.springframework.security.core.userdetails.User("","",new ArrayList<>());
        }
        return userDetails;
    }
}
