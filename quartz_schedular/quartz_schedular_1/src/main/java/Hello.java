import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;

import java.util.ArrayList;
import java.util.Date;

public class Hello implements Job {

    String jobSays;
    float myFloatValue;

    @Override
    public void execute(JobExecutionContext context) {

        JobKey key = context.getJobDetail().getKey();

//        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        JobDataMap dataMap = context.getMergedJobDataMap();

        System.out.println("Hello from the Job !" + context.getFireTime()
                + " " + key + " " + jobSays + " " + myFloatValue);
    }

    public void setJobSays(String jobSays) {
        this.jobSays = jobSays;
    }

    public void setMyFloatValue(float myFloatValue) {
        myFloatValue = myFloatValue;
    }

}
