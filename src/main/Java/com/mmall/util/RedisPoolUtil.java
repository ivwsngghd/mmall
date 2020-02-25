package com.mmall.util;


import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 *  用于操作Redis连接池的工具
 *  使用Sharded优化为分布式；
 *  @deprecated
 */
@Slf4j
public class RedisPoolUtil {
    public static String set(String key,String value){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
            RedisPool.returnResource(jedis);
            return null;
        }

        RedisPool.returnResource(jedis); //释放资源
        return result;
    }

    /**
     *
     * @param key
     * @param value
     * @param exTime 存放的时间，单位是秒
     * @return
     */
    public static String setEx(String key,String value,int exTime){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} error",key,value,e);
            RedisPool.returnResource(jedis);
            return null;
        }

        RedisPool.returnResource(jedis); //释放资源
        return result;
    }

    /**
     * 该方法用于设置key的有效期，单位是秒
     * 设置成功返回1，否则0
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime){
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key,exTime);      //设置成功返回1，失败返回0； key不存在返回0
        } catch (Exception e) {
            log.error("expire key:{} error",key,e);
            RedisPool.returnResource(jedis);
            return null;
        }

        RedisPool.returnResource(jedis); //释放资源
        return result;

    }

    public static String get(String key){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("set key:{} error",key,e);
            RedisPool.returnResource(jedis);
            return null;
        }

        RedisPool.returnResource(jedis); //释放资源
        return result;

    }

    /**
     * 删除对应的key，成功返回1，失败返回0
     * @param key
     * @return
     */
    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error",key,e);
            RedisPool.returnResource(jedis);
            return null;
        }

        RedisPool.returnResource(jedis); //释放资源
        return result;
    }

}
