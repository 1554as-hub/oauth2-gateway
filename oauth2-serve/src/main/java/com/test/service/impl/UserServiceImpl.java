package com.test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.entity.SecurityUser;
import com.test.mapper.UserMapper;
import com.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper , SecurityUser> implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public SecurityUser getUserByUserName(String userName) {
        LambdaQueryWrapper<SecurityUser> queryWrapper = new QueryWrapper<SecurityUser>().lambda();
        queryWrapper.eq(SecurityUser::getUsername , userName);
        List<SecurityUser> users = userMapper.selectList(queryWrapper);
        if (users == null || users.size() == 0){
            return null;
        }

        if(users.size() > 1){
            throw new RuntimeException("数据库定义错误！");
        }
        return users.get(0);
    }

}
