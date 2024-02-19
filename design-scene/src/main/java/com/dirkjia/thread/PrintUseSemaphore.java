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

import java.util.concurrent.Semaphore;

/**
 * Semaphore 三个线程分别打印 A，B，C，要求这三个线程一起运行，打印 n 次，输出形如“ABCABCABC....”的字符串
 * @author heshijia
 * @since 2024/1/10
 */
public class PrintUseSemaphore {

    private int limit ;

    private static final Semaphore sA = new Semaphore(1);
    private static final Semaphore sB = new Semaphore(0);
    private static final Semaphore sC = new Semaphore(0);

    public PrintUseSemaphore(int limit){
        this.limit = limit;
    }

    private void print(String value,Semaphore current,Semaphore next){
        for(int i = 0; i < limit; i++){
            try {
                current.acquire();
                System.out.println(Thread.currentThread().getName() + ": " + value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            next.release();
        }
    }

    public static void main(String[] args) {
        PrintUseSemaphore ps = new PrintUseSemaphore(10);
        new Thread(()->ps.print("A",sA,sB)).start();
        new Thread(()->ps.print("B",sB,sC)).start();
        new Thread(()->ps.print("C",sC,sA)).start();
    }

}
