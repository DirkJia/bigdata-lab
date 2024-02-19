package com.dirkjia.single;

public class StaticSingle {

    private static class InnerSingle {
        private static StaticSingle staticSingle = new StaticSingle();
    }

    public static StaticSingle getInstance(){
        return InnerSingle.staticSingle;
    }
}
