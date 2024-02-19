package com.dirkjia.thread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,
                10,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        Factory factory = new Factory(new LinkedList<>(),10);
        threadPoolExecutor.submit(new Produce(factory));
        threadPoolExecutor.submit(new Consumer(factory));
        threadPoolExecutor.submit(new Produce(factory));
        threadPoolExecutor.submit(new Consumer(factory));
    }

    private static class Factory{
        private Queue queue;
        private int size;
        private volatile int num = 0;
        private Lock lock = new ReentrantLock();
        private Condition condition = lock.newCondition();

        public Factory(Queue<String> queue, int size){
            this.queue=queue;
            this.size=size;
        }

        public void produce(){
            lock.lock();
            try{
                if(num>size){
                    System.out.println("当前生产者线程正在等待：" + Thread.currentThread().getName());
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("当前生产者线程正在生产：" + Thread.currentThread().getName());
                    queue.offer("1");
                    num++;
                    condition.signalAll();
                }
            }finally {
                lock.unlock();
            }
        }

        public void consumer(){
            lock.lock();
            try{
                if(num<0){
                    System.out.println("当前生产者线程正在等待：" + Thread.currentThread().getName());
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("当前生产者线程正在生产：" + Thread.currentThread().getName());
                    queue.poll();
                    condition.signalAll();
                }
            }finally {
                lock.unlock();
            }
        }
    }

    private static class Produce implements Runnable {
        private Factory factory;
        public Produce(Factory factory){
            this.factory=factory;
        }
        @Override
        public void run() {
            while(true){
                factory.produce();
            }
        }
    }

    private static class Consumer implements Runnable {
        private Factory factory;
        public Consumer(Factory factory){
            this.factory = factory;
        }
        @Override
        public void run() {
            while(true){
                factory.consumer();
            }
        }
    }

}
