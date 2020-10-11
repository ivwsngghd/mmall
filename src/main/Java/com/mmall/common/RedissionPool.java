package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

/**
 * 碎片化RedisPool
 * 即配置针对多个RedisServer的连接工具,可把多个Redis节点合为一个使用；
 */
@Slf4j
public class RedisShardedPool {
    private static ShardedJedisPool pool = null;  //Jedis连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20")); //最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "20"));//在jedispool中最多的idle(空闲)状态的实例个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "20"));//在jedispool中最少的idle(空闲)状态的实例个数

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    private static String redis1Password = PropertiesUtil.getProperty("redis1.auth");

    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));
    private static String redis2Password = PropertiesUtil.getProperty("redis2.auth");


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

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip,redis1Port,1000*5);
        info1.setPassword(redis1Password);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip,redis2Port,1000*5);
        info2.setPassword(redis2Password);
        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        pool = new ShardedJedisPool(config, jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN); //一致性算法
    }

    static {
        initPool();
    }

    public static ShardedJedis getShardedJedis() {
        return pool.getResource();
    }

    /**
     * 该方法用于释放jedis连接实例的资源
     *
     * @param shardedJedis
     */
    public static void returnResource(ShardedJedis shardedJedis) {
        if (shardedJedis != null) {
            shardedJedis.close();  // pool.returnResource(shardedJedis);方法已弃用；释放资源使用该方法，无论是否损坏
        }
    }

    //增加该注释，在正常关闭程序的时候，顺便销毁池子，成功删除线程
    @PreDestroy
    private static void poolDestroy(){
        try {
            pool.destroy();
        }catch (Exception e){
            log.info("Redis池子关闭异常:{}",e);
        }
    }


    public static void main(String args[]){
        ShardedJedis jedis = pool.getResource();   //从打开的连接池中获取可用的连接
        for (int i = 0; i < 10; i++) {
            jedis.set("key"+i,"value"+i);
        }

        returnResource(jedis);

        pool.destroy();//临时调用，销毁连接池中所有的连接
        System.out.println("program is end");
    }
}
