package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/manage/user")
public class UserManageController {
    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                //说明登陆的是管理员
                String identifyCookie = UUID.randomUUID().toString();
                CookieUtil.writeLoginToken(httpServletResponse, identifyCookie,Const.Role.ROLE_ADMIN);   //如果不想和protal共用，可以自己写一个cookiename；可以使用UUID，(越权问题已经通过拦截器解决)
                RedisShardedPoolUtil.setEx(identifyCookie, JsonUtil.objToString(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
                return response;
            } else {
                return ServerResponse.createByErrorByMessage("不是管理员，无法登录");
            }
        }
        return response;
    }

}
