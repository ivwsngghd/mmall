package com.mmall.util;


import com.mmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 *  用于操作Redis连接池的工具
 */
@Slf4j
public class RedisShardedPoolUtil {
    public static String set(String key,String value){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
            RedisShardedPool.returnResource(jedis);
            return null;
        }

        RedisShardedPool.returnResource(jedis); //释放资源
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
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} error",key,value,e);
            RedisShardedPool.returnResource(jedis);
            return null;
        }

        RedisShardedPool.returnResource(jedis); //释放资源
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
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.expire(key,exTime);      //设置成功返回1，失败返回0； key不存在返回0
        } catch (Exception e) {
            log.error("expire key:{} error",key,e);
            RedisShardedPool.returnResource(jedis);
            return null;
        }

        RedisShardedPool.returnResource(jedis); //释放资源
        return result;

    }

    public static String get(String key){
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("set key:{} error",key,e);
            RedisShardedPool.returnResource(jedis);
            return null;
        }

        RedisShardedPool.returnResource(jedis); //释放资源
        return result;

    }

    /**
     * 删除对应的key，成功返回1，失败返回0
     * @param key
     * @return
     */
    public static Long del(String key){
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getShardedJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error",key,e);
            RedisShardedPool.returnResource(jedis);
            return null;
        }

        RedisShardedPool.returnResource(jedis); //释放资源
        return result;
    }

    public static void main(String args[]){
        ShardedJedis jedis = RedisShardedPool.getShardedJedis();

        RedisShardedPoolUtil.set("keyTest","value");

        String value = RedisShardedPoolUtil.get("keyTest");

        RedisShardedPoolUtil.setEx("keyex","valueEx",60*10);

        RedisShardedPoolUtil.expire("keyTest",60*20);

        RedisShardedPoolUtil.del("keyTest");

        System.out.println("end");

    }

}
