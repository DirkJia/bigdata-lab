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
 *
 * 用两个线程，一个输出字母，一个输出数字，交替输出 1A2B3C4D...26Z
 *
 * @author heshijia
 * @since 2024/1/11
 */
public class PrintNumAndLetterUseLockSupport {

    private static Thread numThread, letterThread;

    public static void main(String[] args) {
        numThread = new Thread(()->{
            for(int i=0;i<26;i++){
                LockSupport.park();
                System.out.println(Thread.currentThread().getName() + ": " + i);
                LockSupport.unpark(letterThread);
            }
        });
        letterThread = new Thread(()->{
            for(int i=0;i<26;i++){
                LockSupport.park();
                System.out.println(Thread.currentThread().getName() + ": " + (char)('A'+i));
                LockSupport.unpark(numThread);
            }
        });
        numThread.start();
        letterThread.start();

    }

}
