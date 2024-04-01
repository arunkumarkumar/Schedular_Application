package com.schedular.service;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.schedular.util.ApplicationConstants;
import com.schedular.util.LoadJsonConfig;
import com.schedular.util.StackTrace;

public class DbInsertScheduler implements Job {
	Logger LOGGER = LogManager.getLogger(this.getClass());
	DbService service = null;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JSONArray propertyValues = LoadJsonConfig.dbArray;
			JSONArray fileandsp = LoadJsonConfig.dbdetails;
			String nameOfJob = context.getJobDetail().getKey().getName();
			JSONObject dbdetails = (JSONObject) propertyValues.get(ApplicationConstants.job);
			JSONObject filenamesp = (JSONObject) fileandsp.get(Character.getNumericValue(nameOfJob.charAt(nameOfJob.length() - 1)));
			dbinsert(dbdetails, filenamesp);
		} catch (Exception e) {
			LOGGER.error("Exception while using DbJobExecution method:" + StackTrace.getMessage(e));
		}

	}

	public void dbinsert(JSONObject dbdetails, JSONObject filenamesp) {
		try {
			LOGGER.info("Scheduling Time Started for Db Name:" + dbdetails.get("db").toString());
			service = new DbService();
			File flatfilepath = new File(dbdetails.get("flatFilePath").toString());
			if (flatfilepath.canRead()) {
				File[] files = flatfilepath.listFiles();
				for (int file = 0; file < files.length; file++) {
					if (files[file].getName().contains(filenamesp.get("fileName").toString())) {
						LOGGER.info("This Process is " + files[file].getName() + " and flat file is :"+ files[file].toString());
						service.dbservice(files[file].toString(), dbdetails,filenamesp.get("storedprocedure").toString());
					}
				}
				LOGGER.info("Scheduler Application Ended for Dbinsert :" + dbdetails.get("db").toString());
			} else {
				LOGGER.warn("This file path location failed to read" + dbdetails.get("flatFilePath").toString());
			}

		} catch (Exception e) {
			LOGGER.error("Exception while using dbinsert method:" + StackTrace.getMessage(e));
		}
	}
}
