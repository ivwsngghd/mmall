package com.mmall.common;

public class Const {
    public static final String CURRENT_USER = "currentUser";
    public final static String EMAIL = "email";
    public final static String USERNAME = "username";

    public interface Role{
        public final static int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1; //管理员
    }
}
