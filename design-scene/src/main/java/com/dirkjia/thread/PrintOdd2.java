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

/**
 * 两个线程交替打印奇数和偶数
 *
 * @author heshijia
 * @since 2024/1/10
 */
public class PrintOdd2 {

    private Object LOCK = new Object();
    private volatile int current = 0;
    private int limit ;

    public PrintOdd2(int limit){
        this.limit=limit;
    }

    public void print(){
        synchronized(LOCK){
            while(current<limit){
                try{
                    System.out.println(Thread.currentThread().getName() + " : " + current);
                    current++;
                    LOCK.notifyAll();
                    LOCK.wait();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            LOCK.notifyAll();
        }
    }

    public static void main(String[] args) {
        PrintOdd2 printOdd = new PrintOdd2(10);
        new Thread(()->printOdd.print()).start();
        new Thread(()->printOdd.print()).start();
    }
}
