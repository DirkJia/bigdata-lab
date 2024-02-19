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
 * 三个线程分别打印 A，B，C，要求这三个线程一起运行，打印 n 次，输出形如“ABCABCABC....”的字符串
 * 使用wait打印
 *
 * @author heshijia
 * @since 2023/12/25
 */
public class PrintUseWait {


    private int times;
    private int state;
    private static final Object LOCK = new Object();

    public PrintUseWait(int times){
        this.times = times;
    }

    public static void main(String[] args) {
        PrintUseWait printUseWait = new PrintUseWait(9);
        new Thread(()-> {
            try {
                printUseWait.print(0,"A");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()-> {
            try {
                printUseWait.print(1,"B");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()-> {
            try {
                printUseWait.print(2,"C");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void print(int id,String value) throws InterruptedException {

        for(int i=0;i<times;i++){
            synchronized(LOCK){
                while(state%3!=id) {
                    LOCK.wait();
                }
                state++;
                System.out.println(value);
                LOCK.notifyAll();
            }
        }

    }
}
