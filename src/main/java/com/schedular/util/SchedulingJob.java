package com.schedular.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.schedular.service.DbInsertScheduler;
import com.schedular.service.GrxmlgeneratorScheduler;
import com.schedular.service.ApiScheduler;

public class SchedulingJob {
	Logger LOGGER = LogManager.getLogger(this.getClass());

	public Trigger createCronTrigger(String cron, String trig, String group) {
		Trigger triggerNew = null;
		try {
			triggerNew = TriggerBuilder.newTrigger().withIdentity(trig, group)
					.withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
		} catch (Exception e) {
			LOGGER.error("Exception while using createCronTrigger Method:" + StackTrace.getMessage(e));
		}
		return triggerNew;
	}
	public void apiconnect(Scheduler scheduler) {
		try {
			JSONArray propertyValues = LoadJsonConfig.flatFileArray;
			for (int job = 0; job < propertyValues.size(); job++) {
				try {
					JSONObject jsonArrayParse = (JSONObject) propertyValues.get(job);
					JobDetail jobInstance = JobBuilder.newJob(ApiScheduler.class)
							.withIdentity(ApplicationConstants.flatFileJob + job,
									ApplicationConstants.flatFileGroup + job)
							.build();
					Trigger triggerNew = createCronTrigger((String) jsonArrayParse.get("cronExpression"),
							ApplicationConstants.flatFileTrigger + (job + 1),
							ApplicationConstants.flatFileGroup + (job + 1));
					scheduler.scheduleJob(jobInstance, triggerNew);
				} catch (Exception e) {
					LOGGER.error("Exception while using starnikapi Schedule  Method:" + StackTrace.getMessage(e));
				}
			}

		} catch (Exception e) {
			LOGGER.error("Exception while using starnikapi Schedule  Method:" + StackTrace.getMessage(e));
		}

	}

	public void dbconnect(Scheduler scheduler) {
		try {
			JSONArray propertyValues = LoadJsonConfig.dbArray;
			for (int croncount = 0; croncount < propertyValues.size(); croncount++) {
				try {
					JSONObject jsonArrayParse = (JSONObject) propertyValues.get(croncount);
					ApplicationConstants.job=croncount;
					ApplicationConstants.dbJob = jsonArrayParse.get("nameOfJob").toString();
					ApplicationConstants.dbGroup = jsonArrayParse.get("nameOfGroup").toString();
					ApplicationConstants.dbTrigger = jsonArrayParse.get("nameOfTrigger").toString();
					JSONArray dbdtails = (JSONArray) jsonArrayParse.get("dbflatfiles");
					LoadJsonConfig.dbdetails = dbdtails;
					for (int job = 0; job < dbdtails.size(); job++) {
						try {
							JSONObject filenamesp = (JSONObject) dbdtails.get(job);
							JobDetail jobInstance = JobBuilder.newJob(DbInsertScheduler.class)
									.withIdentity(ApplicationConstants.dbJob + job, ApplicationConstants.dbGroup + job)
									.build();
							Trigger triggerNew = createCronTrigger((String) filenamesp.get("cronExpression"),
									ApplicationConstants.dbTrigger + (job + 1), ApplicationConstants.dbGroup + (job + 1));
							scheduler.scheduleJob(jobInstance, triggerNew);
						} catch (Exception e) {
							LOGGER.error("Exception while using dbconnect Schedule method: " + StackTrace.getMessage(e));
						}
					}
				} catch (Exception e) {
					LOGGER.error("Exception while using dbconnect Schedule method: " + StackTrace.getMessage(e));
				}
			}

		} catch (Exception e) {
			LOGGER.error("Exception while using dbconnect Schedule Job: " + StackTrace.getMessage(e));
		}
	}

	public void csvtogrxml(Scheduler scheduler) {
		try {
			JSONArray propertyValues = LoadJsonConfig.xmlArray;
			for (int job = 0; job < propertyValues.size(); job++) {
				try {
					JSONObject jsonArrayParse = (JSONObject) propertyValues.get(job);
					JobDetail jobInstance = JobBuilder.newJob(GrxmlgeneratorScheduler.class).withIdentity(
							ApplicationConstants.xmlJob + job, ApplicationConstants.xmlGroup + job).build();
					Trigger triggerNew = createCronTrigger((String) jsonArrayParse.get("cronExpression"),
							ApplicationConstants.xmlTrigger + (job + 1),
							ApplicationConstants.xmlGroup + (job + 1));
					scheduler.scheduleJob(jobInstance, triggerNew);
				} catch (Exception e) {
					LOGGER.error("Exception while using  csvtogrxml Schedule method: " + StackTrace.getMessage(e));
				}
			}

		} catch (Exception e) {
			LOGGER.error("Exception while using  csvtogrxml Schedule method: " + StackTrace.getMessage(e));
		}

	}
}
