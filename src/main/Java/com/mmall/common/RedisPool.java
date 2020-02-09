package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @deprecated
 */
public class RedisPool {
    private static JedisPool pool = null;  //Jedis连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20")); //最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "20"));//在jedispool中最多的idle(空闲)状态的实例个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "20"));//在jedispool中最少的idle(空闲)状态的实例个数

    private static String redisIp = PropertiesUtil.getProperty("redis.ip");
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));
    private static String password = PropertiesUtil.getProperty("redis.auth");


    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true")); //在borrow一个jedis实力的时候，是否要进行验证操作，如果赋值true,则得到的jedis可用
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true")); //表示在还的时候，是否需要进行验证操作，如果赋值为true，则表示返回的jedis可用

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);//连接耗尽时，是否阻塞；

        //打开Redis连接池服务
        pool = new JedisPool(config, redisIp, redisPort, 1000 * 5, password);

    }

    static {
        initPool();
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    /**
     * 该方法用于释放jedis连接实例的资源
     *
     * @param jedis
     */
    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();  // pool.returnResource(jedis);方法已弃用；释放资源使用该方法，无论是否损坏
        }
    }

    public static void main(String args[]){
        Jedis jedis = pool.getResource();   //从打开的连接池中获取可用的连接
        jedis.set("key1","233333");
//        jedis.auth(password);
        returnResource(jedis);

        pool.destroy();//临时调用，销毁连接池中所有的连接
        System.out.println("program is end");
    }
}
