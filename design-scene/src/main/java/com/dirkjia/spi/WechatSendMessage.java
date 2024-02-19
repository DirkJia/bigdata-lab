package com.dirkjia.spi;

public class WechatSendMessage implements SendMessage{
    @Override
    public void sendMessage(String msg) {
        System.out.println(msg + " ---------weixin is using !!!");
    }
}
