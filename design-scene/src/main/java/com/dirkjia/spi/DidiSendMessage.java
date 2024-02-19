package com.dirkjia.spi;

public class DidiSendMessage implements SendMessage{
    @Override
    public void sendMessage(String msg) {
        System.out.println(msg + " ---------didi is using ！！！");
    }
}
