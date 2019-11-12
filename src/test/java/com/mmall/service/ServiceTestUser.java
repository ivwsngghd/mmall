package com.mmall.service;

import com.mmall.BaseTest;
import com.mmall.dao.UserMapper;
import com.mmall.util.MD5Util;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ServiceTestUser extends BaseTest {
    @Autowired
    private UserMapper userMapper;


    @Test
    public void testUpdateMD5Password(){
        String username = "admin";
        String passwordNew = "admin";
        String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
        int rowCount = userMapper.updatePasswordByUsername(username, md5Password);

        System.out.println(rowCount);

    }

}
