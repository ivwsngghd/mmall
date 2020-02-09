package com.mmall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 这个类用于包装异常信息；
 * 不把代码的详细异常信息直接返回到前端；
 * 提高了安全性
 */

@Slf4j
@Component
public class ExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        log.error("{} Exception",httpServletRequest.getRequestURI(),e); //异常信息写入日志；
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        modelAndView.addObject("status",ResponseCode.ERROR.getCode());
        modelAndView.addObject("msg","接口异常，详情请查看服务端日志的异常");
        modelAndView.addObject("data",e.toString());
        return modelAndView;
    }
}
