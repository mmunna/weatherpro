package com.amunna.weatherpro.datacollector.support;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WoeIDQueue {

    private final Queue<String> woeidQueue = new ConcurrentLinkedQueue<String>();

    public List<String> poll(int limit) {
        final List<String> woeidList = Lists.newArrayList();
        for (int i=0; i<5; i++) {
            woeidList.add(woeidQueue.poll());
        }
        return woeidList;
    }

    public void push(String woeid) {
        woeidQueue.add(woeid);
    }
}
