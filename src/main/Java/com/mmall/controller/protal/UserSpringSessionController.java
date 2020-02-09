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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 该类用于学习使用Spring Session 实现代码无侵入式的Tomcat服务器分布式集群
 * 但Spring Session并不支持多个Redis的横向拓展；
 */

@Deprecated
@Controller
@RequestMapping("/user/springsession/")
public class UserSpringSessionController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            //在Redis中记录对应的SessionId，与之绑定的user数据；存活时间；
//            CookieUtil.writeLoginToken(httpServletResponse,session.getId());    //返回客户端已经设置的COOKIE
//            RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.objToString(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        CookieUtil.delLoginToken(httpServletRequest,httpServletResponse);
//        RedisShardedPoolUtil.del(loginToken);
        session.removeAttribute(Const.CURRENT_USER);

//        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> get_user_info(HttpSession session,HttpServletRequest httpServletRequest) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorByMessage("用户未登录，无法获取当前用户的信息");
    }

}
