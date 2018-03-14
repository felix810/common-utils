package org.fy.lock.zookeeper;

/**
 * created by fy on 2018/3/14 15:34.
 * version:1.0
 * describe:
 */
public class Client {

    public static void main(String args[]) throws Exception{
        for(int i=0;i<10;i++){
            Thread testThread=new TestThread("thredd-"+i);
            testThread.start();
        }

        for(int i=0;i<10;i++){
            Thread thread=new TestTimeOutThread("timeout-thread-"+i);
            thread.start();
        }
    }
}
