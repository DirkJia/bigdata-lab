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
 * 用两个线程，一个输出字母，一个输出数字，交替输出 1A2B3C4D...26Z
 *
 * @author heshijia
 * @since 2024/1/10
 */
public class PrintNumAndLetter {

    private Object LOCK = new Object();
    private char letter = 'A';
    private int num = 1;

    public void print(){
        synchronized(LOCK){
            for(int i=0; i<26;i++){
                if(Thread.currentThread().getName().equals("letter")){
                    System.out.print((char)(letter + i));
                    LOCK.notifyAll();
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if(Thread.currentThread().getName().equals("num")){
                    System.out.print(num + i);
                    LOCK.notifyAll();
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            LOCK.notifyAll();
        }

    }

    public static void main(String[] args) {
        PrintNumAndLetter printNumAndLetter = new PrintNumAndLetter();
        new Thread(()->printNumAndLetter.print(),"num").start();
        new Thread(()->printNumAndLetter.print(),"letter").start();
    }

}
