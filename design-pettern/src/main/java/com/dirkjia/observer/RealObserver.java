package com.dirkjia.observer;

import java.util.Observable;
import java.util.Observer;

public class RealObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("调用了---------" + arg + this.hashCode());
    }
}
