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
 *
 * 三个线程分别打印 A，B，C，要求这三个线程一起运行，打印 n 次，输出形如“ABCABCABC....”的字符串
 *
 * @author heshijia
 * @since 2023/12/25
 */
public class PrintUseLock {

    private int times;
    private int state;
    private ReentrantLock lock = new ReentrantLock();

    public PrintUseLock(int times){
        this.times = times;
    }

    public void print(int id,String value){

        for(int i=0;i<times;){
            lock.lock();
            try{
                if(state%3==id){
                    state++;
                    i++;
                    System.out.println(value);
                }
            } finally{
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {

        PrintUseLock printUseLock = new PrintUseLock(9);
        new Thread(() -> printUseLock.print(0,"A")).start();

        new Thread(()-> printUseLock.print(1,"B")).start();

        new Thread(()-> printUseLock.print(2,"C")).start();
    }

}
