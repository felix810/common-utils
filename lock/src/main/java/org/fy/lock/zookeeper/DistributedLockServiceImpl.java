package org.fy.lock.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DistributedLockServiceImpl implements DistributedLockService {


    //todo 这里配置zk地址
    private String zookeeperConnectionString="";
    
    private int maxConn = 8;
    
    //todo 分布式锁专用zk节点 以/开头，并以/结尾
    private String LOCK_ZK_NODE = "";

    private CuratorFramework curatorFramework;

    private ThreadLocal<Map<String, InterProcessMutex>> threadLocalLock = new ThreadLocal<>();

    public DistributedLockServiceImpl() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        curatorFramework = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        curatorFramework.start();
    }

    @Override
    public void tryLock(String lockKey) throws Exception {
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, LOCK_ZK_NODE + lockKey);
        Map<String, InterProcessMutex> lockMap = threadLocalLock.get();
        if (lockMap == null) {
            lockMap = new HashMap<String, InterProcessMutex>();
        }
        lockMap.put(lockKey, lock);
        threadLocalLock.set(lockMap);
        lock.acquire();
    }

    @Override
    public boolean tryLock(String lockKey, long timeOut, TimeUnit timeUnit) throws Exception {
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, LOCK_ZK_NODE + lockKey);
        if (lock.acquire(timeOut, timeUnit)) {
            Map<String, InterProcessMutex> lockMap = threadLocalLock.get();
            if (lockMap == null) {
                lockMap = new HashMap<String, InterProcessMutex>();
            }
            lockMap.put(lockKey, lock);
            threadLocalLock.set(lockMap);
            return true;
        }
        return false;
    }

    @Override
    public void unLock(String lockKey) throws Exception {
        threadLocalLock.get().get(lockKey).release();
    }

}
