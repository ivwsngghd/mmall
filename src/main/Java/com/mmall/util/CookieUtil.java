package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
public class CookieUtil {

    //域名
//    private final static String COOKIE_DOMAIN = ".ivwsngghd.top";   //一级域名；也可写二级域名；可以理解为作用域
    private final static String COOKIE_DOMAIN = "localhost";   //一级域名；也可写二级域名；可以理解为作用域
    //针对功能
    private final static String COOKIE_NAME = "mmall_login_token";

    public static String readLoginToken(HttpServletRequest request){
        Cookie cks[] = request.getCookies();
        if(cks != null){
            for (Cookie ck : cks){
                log.info("read cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                if (StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    log.info("return cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }


    //X:domain=".happymmall.com"    注意分清域名、路径之间的等级划分，区分作用域
    //a:A.happymmall.com                    cookie:domain=A.happymmall.com;path="/"
    //b:B.happymmall.com                    cookie:domain=B.happymmall.com;path="/"
    //c:A.happymmall.com/test/cc            cookie:domain=A.happymmall.com;path="/test/cc"
    //d:A.happymmall.com/test/dd            cookie:domain=A.happymmall.com;path="/test/dd"
    //e:A.happymmall.com/test               cookie:domain=A.happymmall.com;path="/test"

    //a、b可以用X的cookie，e可以用c、d的；
    //注意cookie的domain设置


    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie ck = new Cookie(COOKIE_NAME,token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/"); //代表设置在根目录(全局)
        ck.setHttpOnly(true);   //禁止通过脚本访问cookie，提高安全性
        ck.setMaxAge(60 * 60 * 24 * 365);  //-1为永久；MaxAge如果不写，不会写入硬盘，只存在内存

        log.info("write cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());

        response.addCookie(ck);
    }

    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cks = request.getCookies();
        if (cks != null){
            for (Cookie ck : cks){
                if (StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setMaxAge(0); //代表删除此cookie
                    log.info("del cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
                    response.addCookie(ck);
                    return;
                }
            }
        }
    }

}
