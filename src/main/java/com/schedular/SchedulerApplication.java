package com.schedular;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.schedular.util.LoadJsonConfig;
import com.schedular.util.SchedulingJob;
import com.schedular.util.StackTrace;

@SpringBootApplication
@EnableScheduling
public class SchedulerApplication {
	static Logger LOGGER = LogManager.getLogger(SchedulerApplication.class);
    static Scheduler scheduler;
	static SchedulingJob scheduling = null;

	public static void main(String[] args)  {
		try {
			SpringApplication.run(SchedulerApplication.class, args);
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			LOGGER.info("Scheduler Application Started");
			LoadJsonConfig.load();
			scheduling = new SchedulingJob();
		    scheduling.apiconnect(scheduler);
			scheduling.dbconnect(scheduler);
			scheduling.csvtogrxml(scheduler);
		} catch (Exception e) {
           LOGGER.error("Exception While Using SchedulerApplication :"+StackTrace.getMessage(e));
		}
	}

}
