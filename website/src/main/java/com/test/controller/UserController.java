package com.test.controller;

import com.test.domain.UserDto;
import com.test.domain.holder.LoginuserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private LoginuserHolder loginuserHolder;

    @GetMapping("/user/currentUser")
    public UserDto currentUser() {
        return loginuserHolder.getCurrentUser();
    }


}
