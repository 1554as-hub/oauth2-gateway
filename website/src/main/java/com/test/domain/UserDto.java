package com.test.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    private String id;

    private String username;

    private String password;

    private String realname;

    private String phone;

    private String avatar;

    private String birthday;

    private String sex;

    private String createTime;

    private List<String> authorities;

}
