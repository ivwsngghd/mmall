package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.common.RedissonManager;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * 定时关闭订单，并且把库存更新回数据库中
 */

@Component
@Slf4j
public class CloseOrderTask {
    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RedissonManager redissonManager;

    /**
     * 无分布式锁版本
     */
//    @Scheduled(cron = "0 */1 * * * ?")  //每一分钟(每个一分钟的整数倍)
    public void closeOrderTaskV1() {
        log.info("关闭订单定时任务启动");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
//        iOrderService.closeOrder(hour);
        log.info("关闭订单定时任务完成");
    }

//    @Scheduled(cron = "0 */1 * * * ?")  //每一分钟(每个一分钟的整数倍)
    public void closeOrderTaskV2() {
        log.info("关闭订单定时任务启动");

        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));

        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeout));
        if (setnxResult != null && setnxResult.intValue() == 1) {
            //如果返回值是1，代表设置(锁获取)成功
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            log.info("没有获得分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("关闭订单定时任务完成");
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV3() {
        log.info("关闭订单定时任务启动");
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeout));

        if (setnxResult != null && setnxResult.intValue() == 1) {
            //如果返回值是1，代表设置(锁获取)成功
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            //未获取到锁，继续判断时间戳，看是否可以重置并获取到锁；
            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK); //获取旧值(用于查询能否获取)
            if (lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)){ //这个锁已经超时,可以尝试获取；[当前时间 > 旧值的时间(设置时的时间+超时时间) ]
                String getSetResult = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis() + lockTimeout));//拿取这个值
                //再次用当前时间戳getSet
                //返回给定的key旧值，通过旧值判断，是否可以获取锁
                //情况：当key没有旧值，即key不存在时，返回nil。 因此可以直接获取锁
                //set了一个新的value值，获取旧的值
                if (getSetResult == null || StringUtils.equals(lockValueStr, getSetResult)){   //如果获取到的值，但改变了，证明已经有其他服务器对其进行了操作；(不是最先的服务器)
                    //唯一获取到了锁的服务器就可以进入到这里
                    //乐观锁原理
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }else {
                    log.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            }else {
                log.info("没有获取到分布式锁:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
        }
        log.info("关闭订单定时任务完成");
    }

//    @Scheduled(cron = "0 */1 * * * ?")
//    public void closeOrderTaskV4(){
//        RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//
//        boolean getLock = false;
//        try {
//            if (getLock = lock.tryLock(0,5, TimeUnit.SECONDS)){
//                log.info("Redisson获取分布式锁:{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
//                Thread.sleep(1000);
//                int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","1"));
////                iOrderService.closeOrder(hour);
//            }else {
//                log.info("Redisson没有获取到分布式锁:{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
//            }
//        } catch (InterruptedException e) {
//            log.error("Redisson分布式锁获取异常",e);
//        }finally {
//            if (!getLock){
//                return;
//            }
//            lock.unlock();
//            log.info("Redisson分布式锁释放锁");
//        }
//    }

    private void closeOrder(String lockName) {
        // 若此时故障，未能锁延时。
        RedisShardedPoolUtil.expire(lockName, 30);//有效期为30秒，防止死锁；但要确保其他服务器均已经放弃锁的获取，否则该方法会再次执行；
        log.info("获取{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
        //确保删除任务在设定时间内完成，否则需要把锁延时；
        iOrderService.closeOrder(hour);
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{},ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        log.info("===================================");

    }

    //在正常关闭服务器的情况下，会提前运行这个方法
    @PreDestroy
    public void delLock(){
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }

}
