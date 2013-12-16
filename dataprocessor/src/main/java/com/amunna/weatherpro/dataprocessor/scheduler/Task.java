package com.amunna.weatherpro.dataprocessor.scheduler;

import com.google.common.base.Throwables;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Task implements Job, TaskMBean {
    public State status = State.DONE;

    public static enum State {
        ERROR, RUNNING, DONE
    }

    private static final Logger logger = LoggerFactory.getLogger(Task.class);
    private final AtomicInteger errors = new AtomicInteger();
    private final AtomicInteger executions = new AtomicInteger();

    protected Task() {
        this(ManagementFactory.getPlatformMBeanServer());
    }

    protected Task(MBeanServer mBeanServer) {
        // TODO: don't do mbean registration here
        String mbeanName = "com.amunna.weatherpro.datacollect.scheduler:type=" + this.getClass().getName();
        try {
            mBeanServer.registerMBean(this, new ObjectName(mbeanName));
            initialize();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }


    /**
     * This method has to be implemented and cannot throw any exception.
     */
    public void initialize() throws ExecutionException {
        // nothing to initialize
    }

    public abstract void execute() throws Exception;

    /**
     * Main method to execute a task
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        executions.incrementAndGet();
        try {
            if (status == State.RUNNING) {
                return;
            }
            status = State.RUNNING;
            execute();

        } catch (Exception e) {
            status = State.ERROR;
            logger.error("Couldn't execute the task because of " + e.getMessage(), e);
            errors.incrementAndGet();
        } catch (Throwable e) {
            status = State.ERROR;
            logger.error("Couldn't execute the task because of " + e.getMessage(), e);
            errors.incrementAndGet();
        }
        if (status != State.ERROR) {
            status = State.DONE;
        }
    }

    public State state() {
        return status;
    }

    public int getErrorCount() {
        return errors.get();
    }

    public int getExecutionCount() {
        return executions.get();
    }

    public abstract String getName();


    public JobDetail getJobDetail(){
        JobDetail jobDetail = JobBuilder.newJob(getClass())
                .withIdentity("weatherpro-scheduler", getName())
                .build();
        return jobDetail;
    }

    public Trigger getCronTimeTrigger(){
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("weatherpro-scheduler", getTriggerName())
                .withSchedule(CronScheduleBuilder.cronSchedule(getCronTime()))
                .build();
        return trigger;
    }

    public String getCronTime() {
        return null;
    }

    public abstract String getTriggerName();


    public Trigger getTriggerToStartNowAndRepeatInHours() {
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("weatherpro-trigger", getTriggerName())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(getIntervalInHours()).repeatForever().withMisfireHandlingInstructionFireNow())
                .build();
        return trigger;
    }

    public Trigger getTriggerToStartNowAndRepeatInMinutes() {
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("weatherpro-trigger", getTriggerName())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(getIntervalInMinutes()).repeatForever().withMisfireHandlingInstructionFireNow())
                .build();
        return trigger;
    }

    //Override this method in subclass if using repeated interval
    public int getIntervalInHours() {
        return 1;
    }

    //Override this method in subclass if using repeated interval
    public int getIntervalInMinutes() {
        return 1;
    }
}