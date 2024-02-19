package com.dirkjia.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchTest {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(new LastRunner(countDownLatch)).start();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(new MyRunner(countDownLatch));
        executor.submit(new MyRunner(countDownLatch));
        executor.shutdown();
    }


    public static class MyRunner implements Runnable {
        private CountDownLatch countDownLatch;

        public MyRunner(CountDownLatch countDownLatch){
            this.countDownLatch=countDownLatch;
        }

        @Override
        public void run() {
            System.out.println("当前线程正在初始化：" + Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("通知 lastRunner 执行");
            countDownLatch.countDown();
        }
    }

    public static class LastRunner implements Runnable {
        private CountDownLatch countDownLatch;
        public LastRunner(CountDownLatch countDownLatch){
            this.countDownLatch = countDownLatch;
        }
        @Override
        public void run() {
            Thread.currentThread().setName("lastRunner");
            System.out.println("LastRunner 线程正在初始化：");
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("LastRunner 线程开始执行");
        }
    }
}
