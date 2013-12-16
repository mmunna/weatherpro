package com.amunna.weatherpro.dataprocessor.scheduler;

public interface TaskMBean {
    public int getErrorCount();

    public int getExecutionCount();

    public String getName();
}