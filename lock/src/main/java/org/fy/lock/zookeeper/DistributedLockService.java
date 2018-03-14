package org.fy.lock.zookeeper;



import java.util.concurrent.TimeUnit;

/**
 * created by fy on 2018/3/14 15:34.
 * version:1.0
 * describe: 分布式锁服务
 */
public interface DistributedLockService {

    void tryLock(String lockKey) throws Exception;

    boolean tryLock(String lockKey, long timeOut, TimeUnit timeUnit) throws Exception;

    void unLock(String lockKey) throws Exception;
}
