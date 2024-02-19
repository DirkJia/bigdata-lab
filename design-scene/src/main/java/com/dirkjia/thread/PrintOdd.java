/*
 * Copyright (c) 2022 Terminus, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dirkjia.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * 两个线程交替打印奇数和偶数
 *
 * @author heshijia
 * @since 2024/1/10
 */
public class PrintOdd {

    private int times;
    private int count;
    private ReentrantLock lock = new ReentrantLock();

    public PrintOdd(int times) {
        this.times = times;
    }

    public void print(int flag){
        for(;count<times;){
            lock.lock();
            try{
                if(count%2==flag){
                    System.out.println(Thread.currentThread().getName() + ": " + count);
                    count++;
                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {

                lock.unlock();
            }

        }
    }


    public static void main(String[] args) {
        PrintOdd printOdd = new PrintOdd(10);
        new Thread(()->printOdd.print(0)).start();
        new Thread(()->printOdd.print(1)).start();
    }

}
