package com.dirkjia.strategy;

public abstract class Strategy {

    protected abstract void exec();

    public static class StrategyA extends Strategy{

        @Override
        protected void exec() {
            System.out.println("执行策略A");
        }
    }

    public static class StrategyB extends Strategy{

        @Override
        protected void exec() {
            System.out.println("执行策略B");
        }
    }

}
