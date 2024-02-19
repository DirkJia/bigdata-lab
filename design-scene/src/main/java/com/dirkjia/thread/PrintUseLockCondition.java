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

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个线程分别打印 A，B，C，要求这三个线程一起运行，打印 n 次，输出形如“ABCABCABC....”的字符串
 *
 * @author heshijia
 * @since 2024/1/10
 */
public class PrintUseLockCondition {

    private int state;
    private int limit;

    public PrintUseLockCondition(int limit ){
        this.limit = limit;
    }

    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition conditionA = lock.newCondition();
    private static final Condition conditionB = lock.newCondition();
    private static final Condition conditionC = lock.newCondition();

    private void print(int flag,String value,Condition current,Condition next){
        for(int i=0;i<limit;){
            lock.lock();
            try{
                while(state%3!=flag){
                    current.await();
                }
                System.out.println(Thread.currentThread().getName() + ": " + value);
                i++;
                state++;
                next.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        PrintUseLockCondition pc = new PrintUseLockCondition(10);
        new Thread(()-> pc.print(0,"A", conditionA, conditionB)).start();
        new Thread(()-> pc.print(1,"B", conditionB, conditionC)).start();
        new Thread(()-> pc.print(2,"C", conditionC, conditionA)).start();
    }
}
