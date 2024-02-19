package com.dirkjia.thread;


import java.util.Random;
import java.util.concurrent.*;

public class CyclicBarrierFTest {


    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(3,
                Integer.MAX_VALUE,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        poolExecutor.submit(new MyRunner(cyclicBarrier));
        poolExecutor.submit(new MyRunner(cyclicBarrier));
        poolExecutor.submit(new MyRunner(cyclicBarrier));
        poolExecutor.shutdown();
    }

    public static class MyRunner implements Runnable {
        private CyclicBarrier cyclicBarrier;
        public MyRunner(CyclicBarrier cyclicBarrier){
            this.cyclicBarrier=cyclicBarrier;
        }
        @Override
        public void run() {
            long sleeptime = new Random().nextInt(4)*1000;
            System.out.println("当前线程正在初始化 " + Thread.currentThread().getName() + " 休眠时间 " + sleeptime);
            try {
                Thread.sleep(sleeptime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("当前线程开始执行 " + Thread.currentThread().getName() + "时间是：" + System.currentTimeMillis());
        }
    }
}
