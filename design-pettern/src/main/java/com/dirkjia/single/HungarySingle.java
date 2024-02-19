package com.dirkjia.single;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * 饿汉模式，使用readResolve方式避免单例中出现的序列换问题
 */
public class HungarySingle implements Serializable {

    private static final HungarySingle INSTANCE = new HungarySingle();

    private HungarySingle(){}

    public static HungarySingle getInstance(){
        return INSTANCE;
    }

    private Object readResolve() throws ObjectStreamException {
        // instead of the object we're on,
        // return the class variable INSTANCE
        return INSTANCE;
    }

}
