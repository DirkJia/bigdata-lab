package com.dirkjia.single;

/**
 * 懒汉模式，双重检查，
 */
public class LazySingle {

    //volatile 主内存可见,防止指令重排
    private static volatile LazySingle lazySingle;

    private LazySingle(){}

    public static LazySingle getLazySingle(){
        if(lazySingle == null){
            synchronized (LazySingle.class){
                if(lazySingle == null){
                    lazySingle = new LazySingle();
                }
            }
        }
        return lazySingle;
    }
}
