package com.mmall.controller.protal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(@RequestParam("username") String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            //在Redis中记录对应的SessionId，与之绑定的user数据；存活时间；
            CookieUtil.writeLoginToken(httpServletResponse,session.getId());    //返回客户端已经设置的COOKIE
            RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.objToString(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
//           正确设置方式？： RedisShardedPoolUtil.setEx(username, session.getId(),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);

        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        CookieUtil.delLoginToken(httpServletRequest,httpServletResponse);
        RedisShardedPoolUtil.del(loginToken);

//        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }


    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_user_info(HttpSession session,HttpServletRequest httpServletRequest) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorByMessage("用户未登录，无法获取当前用户信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.stringToObj(userJsonStr,User.class);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorByMessage("用户未登录，无法获取当前用户的信息");
    }


    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {

        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    //在登陆状态下的修改密码
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpServletRequest httpServletRequest, String passwordOld, String passwordNew) {
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorByMessage("用户未登录，无法获取当前用户信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.stringToObj(userJsonStr,User.class);
        if (user == null) {
            return ServerResponse.createByErrorByMessage("用户未登录");
        }

        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    /**
     * @return 返回更新后的user数据
     */
    @RequestMapping(value = "update_infomation.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_infomation(HttpServletRequest httpServletRequest,User userNewInfo){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorByMessage("用户未登录，无法获取当前用户信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.stringToObj(userJsonStr,User.class);
        if (user == null) {
            return ServerResponse.createByErrorByMessage("用户未登录");
        }

        //确保这些信息不可变
        userNewInfo.setId(user.getId());
        userNewInfo.setUsername(user.getUsername());

        ServerResponse<User> response = iUserService.updateInformation(userNewInfo);
        if (response.isSuccess()){
            RedisShardedPoolUtil.setEx(loginToken, JsonUtil.objToString(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return response;
    }


    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isEmpty(loginToken)){
            return  ServerResponse.createByErrorByMessage("用户未登录，无法获取当前用户信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.stringToObj(userJsonStr,User.class);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,需要强制登录status=10");
        }
        return iUserService.getInformation(user.getId());


    }


}
