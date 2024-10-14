
import org.quartz.*;

import java.util.Arrays;

public class Schedular {
    public static void main(String[] args) throws SchedulerException {

        SchedulerFactory schedulerFactory = new org.quartz.impl.StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        scheduler.start();

        // define the job and tie it to our HelloJob class
        JobDetail job
                = JobBuilder
                .newJob()
                .ofType(Hello.class)
                .withIdentity("myJob", "group1")
                .usingJobData("jobSays", "Hello World!")
                .usingJobData("myFloatValue", 3.141f)
                .build();

        // Trigger the job to run now, and then every 40 seconds
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("Hello-trigger-name", "group1")
                .forJob(job)
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInSeconds(1)
                        .withRepeatCount(5))
                .build();

        // Tell quartz to schedule the job using our trigger
        scheduler.scheduleJob(job, trigger);
    }
}
