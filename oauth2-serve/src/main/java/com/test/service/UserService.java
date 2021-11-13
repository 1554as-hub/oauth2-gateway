package com.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.entity.SecurityUser;

public interface UserService extends IService<SecurityUser> {

    SecurityUser getUserByUserName(String id);

}
