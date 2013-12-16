package com.amunna.weatherpro.datacollector.scheduler;

public interface TaskMBean {
    public int getErrorCount();

    public int getExecutionCount();

    public String getName();
}