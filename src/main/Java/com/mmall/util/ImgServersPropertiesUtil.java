package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class ImgServersPropertiesUtil {

    //org.slf4j.Logger
    private static Logger logger = LoggerFactory.getLogger(ImgServersPropertiesUtil.class);

    //java.util.Properties;
    private static Properties props;

    //该代码块用于类加载的时候，自动进行初始化加载一次；
    static {
        String fileName = "mmall.imgServers";
        props = new Properties();
        try {
            props.load(new InputStreamReader(ImgServersPropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
        } catch (IOException e) {
            logger.error("配置文件读取异常",e);
        }
    }

    public static String[] getProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        String result[] = value.split("&");

        return result;
    }

    public static String[] getProperty(String key,String defaultValue){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            value = defaultValue;
        }
        String result[] = value.split("&");
        return result;
    }




}
