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
 * 通过 N 个线程顺序循环打印从 0 至 100
 *
 * @author heshijia
 * @since 2024/1/11
 */
public class PrintNumUseSemaphore {

    private int limit ;
    private int res;

    public PrintNumUseSemaphore(int limit){
        this.limit = limit;
    }

    private void print(Semaphore current,Semaphore last){
        for(;res<limit;){
            try {
                last.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ": " + res++);
            current.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int threadNum = 3;
        Semaphore[] semaphores = new Semaphore[threadNum];
        for(int i=0;i<threadNum;i++){
            semaphores[i]=new Semaphore(1);
            if(i!=threadNum-1){
                semaphores[i].acquire();
            }
        }
        PrintNumUseSemaphore pnus = new PrintNumUseSemaphore(10);
        for(int i=0;i<threadNum;i++){
            Semaphore current = semaphores[i];
            Semaphore last = i==0?semaphores[threadNum-1]:semaphores[i-1];
            new Thread(()->pnus.print(current,last)).start();
        }
    }
}
