package com.dirkjia.observer;

import java.util.Observable;

public class Subject extends Observable {

    public void makeChange(){
        setChanged();
        notifyObservers();
    }
}
