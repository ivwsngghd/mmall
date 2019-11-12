package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    int checkEmail(String email);

    User selectLogin(@Param("username") String username, @Param("password") String password);

    String selectQuestionByUsername(String username);

    int checkAnswer(@Param("username")String username,@Param("question")String question,@Param("answer")String answer);

    Integer updatePasswordByUsername(@Param("username")String username,@Param("md5Password")String md5Password);

    /**
     * 检查当前用户的密码是否存在(即输入的旧密码是否正确)
     * @param md5PasswordNew
     * @param userId
     * @return
     */
    int checkPassword(@Param("md5PasswordNew")String md5PasswordNew,@Param(value = "userId")Integer userId);

    int checkEmailByUserId(@Param("email")String email,@Param("userId")int userId);
}