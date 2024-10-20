
import org.quartz.*;
import org.quartz.impl.calendar.HolidayCalendar;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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


        HolidayCalendar cal = new HolidayCalendar();
        Date date = new Date();

        Calendar day1 = Calendar.getInstance();
        day1.setTime(date);
        day1.add(Calendar.DATE, 1);

        Calendar day2 = Calendar.getInstance();
        day2.setTime(date);
        day2.add(Calendar.DATE, 2);

        cal.addExcludedDate( day1.getTime() );
        cal.addExcludedDate( day2.getTime() );

        scheduler.addCalendar("HolidayCalendar", cal , false, false);

        Trigger trigger1 = TriggerBuilder
                .newTrigger()
                .forJob(job)
                .withIdentity("holiday-trigger-1")
                .modifiedByCalendar("HolidayCalendar")
                .withSchedule(DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().onEveryDay())
                .build();

        Trigger trigger2 = TriggerBuilder
                .newTrigger()
                .forJob(job)
                .withIdentity("holiday-trigger-2")
                .modifiedByCalendar("HolidayCalendar")
                .withSchedule(DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().onMondayThroughFriday())
                .build();

        scheduler.scheduleJob(job, trigger2);
    }
}
