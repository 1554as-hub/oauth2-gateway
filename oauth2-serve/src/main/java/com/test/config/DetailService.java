package com.test.config;


import com.test.entity.SecurityUser;
import com.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("detailService")
public class DetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        SecurityUser userDetail = userService.getUserByUserName(s);

        if(userDetail == null){
            throw  new UsernameNotFoundException("用户不存在异常");
        }

        String[] roles = {"admin" , "user"};
        userDetail.setAuthorities(authorityList(roles));
        return userDetail;
    }


    public List<SimpleGrantedAuthority> authorityList(String[] roles) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles){
            authorities.add(new SimpleGrantedAuthority(role.toUpperCase()));
        }
        return authorities;
    }

}
