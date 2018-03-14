package org.fy.lock.zookeeper;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * created by fy on 2018/3/14 15:56.
 * version:1.0
 * describe:
 */
public class TestTimeOutThread extends Thread{

    private String thredName;

    public TestTimeOutThread(String string){
        this.thredName=string;
    }

    @Override
    public void run() {
        try {
            DistributedLockService distributedLockService=new DistributedLockServiceImpl();

            System.out.println(this.thredName+"尝试加锁");

            //设置5秒时间，超过5秒则放弃加锁
            boolean flag=distributedLockService.tryLock("test-timeout",5, TimeUnit.SECONDS);

            if(flag){
                Random random=new Random();

                int time= random.nextInt(10);

                System.out.println(this.thredName+"加锁成功,休眠"+time+"秒钟");
                //随机休眠一段时间
                Thread.sleep(time*1000);

                distributedLockService.unLock("test-timeout");

                System.out.println(this.thredName+"释放锁");
            }else{
                System.out.println(this.thredName+"尝试加锁超时，失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
