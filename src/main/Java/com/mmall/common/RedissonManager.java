package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@Slf4j
@Deprecated
public class RedissonManager {
    private Config config = new Config();
    private Redisson redisson = null;

    public Redisson getRedisson(){
        return redisson;
    }

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    private static String redis1Password = PropertiesUtil.getProperty("redis1.auth");

    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));
    private static String redis2Password = PropertiesUtil.getProperty("redis2.auth");

    @PostConstruct
    private void init() {
        try {
            config.useSingleServer().setAddress("redis://"+redis1Ip+":"+redis1Port).setPassword(redis1Password);
            redisson = (Redisson) Redisson.create(config);
            log.info("初始化Redisson完成");
        }catch (Exception e){
            log.error("redisson init error:",e);
        }
    }

    @PreDestroy
    private void destroy(){
        redisson.shutdown();
    }

}
