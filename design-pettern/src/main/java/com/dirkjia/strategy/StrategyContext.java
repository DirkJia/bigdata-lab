package com.dirkjia.strategy;

public class StrategyContext {

    private Strategy strategy;

    public void choice(StrategyType strategyType){
        switch (strategyType){
            case STRATEGYA:
                strategy = new Strategy.StrategyA();
                break;
            case STRATEGYB:
                strategy = new Strategy.StrategyB();
                break;
                default:
                    throw new RuntimeException("暂不支持此策略");
        }
    }

    public void exec(){
        strategy.exec();
    }


    public static void main(String[] args) {
        StrategyContext strategyContext = new StrategyContext();

        strategyContext.choice(StrategyType.STRATEGYA);
        strategyContext.exec();

        strategyContext.choice(StrategyType.STRATEGYB);
        strategyContext.exec();
    }

}
