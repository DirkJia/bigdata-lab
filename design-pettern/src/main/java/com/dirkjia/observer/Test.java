package com.dirkjia.observer;

public class Test {

    public static void main(String[] args) {
        Subject subject = new Subject();
        RealObserver realObserver1 = new RealObserver();
        RealObserver realObserver2 = new RealObserver();
        subject.addObserver(realObserver1);
        subject.addObserver(realObserver2);
        subject.makeChange();
    }
}
