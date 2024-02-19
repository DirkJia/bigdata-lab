package com.dirkjia.thread;

import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ProduceCustomTest {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10,
                10,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        Factory factory = new Factory(new LinkedBlockingQueue<String>(),10);
        executor.submit(new Produce(factory));
        executor.submit(new Consumer(factory));
        executor.submit(new Consumer(factory));


        executor.shutdown();
    }

    private static class Factory {
        private volatile int num = 0;
        private Queue<String> queue ;
        int size ;

        public Factory(LinkedBlockingQueue<String> queue, int size){
            this.queue=queue;
            this.size=size;
        }

        public void produce() {
            synchronized (queue) {
                if (num > size) {
                    try {
                        System.out.println("当前生产者线程正在等待 " + Thread.currentThread().getName());
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("当前生产者线程正在生产 " + Thread.currentThread().getName());
                    queue.offer("1");
                    num++;
                    queue.notifyAll();
                }
            }
        }

        public void consumer() {
            synchronized (queue) {
                if (num < 0) {
                    try {
                        System.out.println("当前消费者线程正在等待 " + Thread.currentThread().getName());
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("当前消费者线程正在消费 " + Thread.currentThread().getName());
                    queue.offer("1");
                    num--;
                    queue.notifyAll();
                }
            }
        }
    }

    private static class Produce implements Runnable {
        Factory factory;

        public Produce(Factory factory) {
            this.factory = factory;
        }

        @Override
        public void run() {
            while(!Thread.interrupted()){
                factory.produce();
            }
        }
    }

    private static class Consumer implements Runnable {
        Factory factory;

        public Consumer(Factory factory) {
            this.factory = factory;
        }

        @Override
        public void run() {
            while(!Thread.interrupted()){
                factory.consumer();
            }
        }
    }
}
