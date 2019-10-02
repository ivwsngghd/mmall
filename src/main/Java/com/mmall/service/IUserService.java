package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface IUserService {

    ServerResponse<User> login(String username, String password);

    public ServerResponse<String> register(User user);

    /**
     * 可复用的验证信息是否不为空；
     *
     * @param str
     * @param type Const.CURRENT_USER、Const.EMAIL、Const.USERNAME
     * @return
     */
    public ServerResponse<String> checkValid(String str, String type);

    /**
     * 查询该用户是否存在，已经对应的密码找回安全问题
     *
     * @param username
     * @return
     */
    public ServerResponse selectQuestion(String username);

    /**
     * 检查答案是否正确，如果正确，生成一个限时的token存放在本地，并且把token返回给客户端，通过这个token来进行密码的修改；
     *
     * @param username 目标用户
     * @param question 找回密码的安全问题
     * @param answer   问题的答案
     * @return
     */
    public ServerResponse<String> checkAnswer(String username, String question, String answer);

    /**
     * 利用回答问题正确后生成的token来进行用户的身份验证
     * 并且进行修改密码
     *
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    /**
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    /**
     * 用于更新用户的信息
     * @param user
     * @return
     */
    public ServerResponse<User> updateInformation(User user);

    /**
     * 用于获取用户的信息
     * @param userId
     * @return
     */
    public ServerResponse<User> getInformation(Integer userId);

    /**
     * 用于校验是否为管理员权限
     * @param user
     * @return
     */
    public ServerResponse checkAdminRole(User user);



}