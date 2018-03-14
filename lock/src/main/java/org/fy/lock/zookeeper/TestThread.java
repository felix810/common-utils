package org.fy.lock.zookeeper;

/**
 * created by fy on 2018/3/14 15:45.
 * version:1.0
 * describe:
 */
public class TestThread extends Thread{

    private String thredName;

    public TestThread(String string){
        this.thredName=string;
    }

    @Override
    public void run() {
        try {
            DistributedLockService distributedLockService=new DistributedLockServiceImpl();

            System.out.println(this.thredName+"尝试加锁");

            distributedLockService.tryLock("test");

            System.out.println(this.thredName+"加锁成功,休眠1秒钟");

            Thread.sleep(1000);

            distributedLockService.unLock("test");

            System.out.println(this.thredName+"释放锁");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
