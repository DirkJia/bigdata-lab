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

import java.util.concurrent.locks.LockSupport;

/**
 * 三个线程分别打印 A，B，C，要求这三个线程一起运行，打印 n 次，输出形如“ABCABCABC....”的字符串
 *
 * @author heshijia
 * @since 2024/1/11
 */
public class PrintUseLockSupport {

    private static Thread threadA, threadB, threadC;

    public static void main(String[] args) {
        threadA = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                // 打印当前线程名称
                System.out.print(Thread.currentThread().getName());
                // 唤醒下一个线程
                LockSupport.unpark(threadB);
                // 当前线程阻塞
                LockSupport.park();
            }
        }, "A");
        threadB = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                // 先阻塞等待被唤醒
                LockSupport.park();
                System.out.print(Thread.currentThread().getName());
                // 唤醒下一个线程
                LockSupport.unpark(threadC);
            }
        }, "B");
        threadC = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                // 先阻塞等待被唤醒
                LockSupport.park();
                System.out.print(Thread.currentThread().getName());
                // 唤醒下一个线程
                LockSupport.unpark(threadA);
            }
        }, "C");
        threadA.start();
        threadB.start();
        threadC.start();
    }
}
