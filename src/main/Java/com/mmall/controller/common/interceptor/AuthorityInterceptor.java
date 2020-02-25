package com.mmall.controller.common.interceptor;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {
        log.info("preHandle:");
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();//simpleName不包含包名
        //上面两个参数用于灵活放行指定类，指定的方法，无需通过特定mapping在xml配置放行，更灵活，但代码量多；
        /*
        if (StringUtils.equals(className, "UserManageController") && StringUtils.equals(methodName,"login")){
            // 立刻放行
            return true;
        }
        */

        //解析参数，具体的参数key以及value是什么，打印日志
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = httpServletRequest.getParameterMap();       //从请求中获取参数Map
        for (Object object : paramMap.entrySet()) {
            Map.Entry entry = (Map.Entry) object;    //获取键值对
            String mapKey = (String) entry.getKey();    //获取键
            String mapValue = StringUtils.EMPTY;        //用于获取值

            //request这个参数的map,里面的value返回的是一个String[];
            Object obj = entry.getValue();      //返回了一个String数组
            if (obj instanceof String[]) {
                String strs[] = (String[]) obj;  //把返回的value数组强转成String[]数组
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue); // 键 = 值对(数组)
        }
        //成功获取了用户信息之后，使用RedisUtil工具进行权限登陆操作
        User user = null;
        String loginToken = CookieUtil.manageReadLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.stringToObj(userJsonStr, User.class);
        }

        //如果user是空，或者user不是管理员，则需要重新登陆；
        //返回false，不接下来执行
        if (user == null || (user.getRole() != Const.Role.ROLE_ADMIN)) {
            //脱离了ServerResponse类的通常封装返回方法
            httpServletResponse.reset();//这里要添加reset,否则会报异常 getWriter() has alread been called for this response.
            httpServletResponse.setCharacterEncoding("UTF-8");  //编码
            httpServletResponse.setContentType("application/json;charset=UTF-8");//必须设置成json格式

            PrintWriter out;
            try {
                out = httpServletResponse.getWriter();
            } catch (IOException e) {
                log.info("interceptor error :{}",e);
                return false;
            }

            if (user == null){
                out.print(JsonUtil.objToString(ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"拦截器拦截，用户未登录")));
            }else {
                out.print(JsonUtil.objToString(ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"拦截器拦截，用户无权限")));
            }
            out.flush();
            out.close();//关闭流操作
            return false;   //即不会执行Controller里面的方法
        }

        return true;   //false会终止操作，true才是继续进行处理
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("afterCompletion");
    }

}

/*
 *   在JDK 8，interface的接口方法是可以添加default关键字，然后写方法体{}的；
 *   也就是这些方法可以默认default里面的方法，而不是必须实现；
 * */
