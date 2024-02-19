package com.dirkjia.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

public class SpiTest {
    public static void main(String[] args) {
        ServiceLoader<SendMessage> serviceLoader = ServiceLoader.load(SendMessage.class);
        Iterator<SendMessage> iterator = serviceLoader.iterator();
        while(iterator.hasNext()) {
            SendMessage smg = iterator.next();
            smg.sendMessage("spi test  : ");
        }
    }
}
