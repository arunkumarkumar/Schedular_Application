package com.schedular.service;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.schedular.util.LoadJsonConfig;
import com.schedular.util.StackTrace;

public class ApiScheduler implements Job {
	Logger LOGGER = LogManager.getLogger(this.getClass());
	ApiOperations api =null;
	public void execute(JobExecutionContext context) {
		try {
			JSONArray propertyValues = LoadJsonConfig.flatFileArray;
			String nameOfJob = context.getJobDetail().getKey().getName();
			JSONObject jsonArrayParse = (JSONObject) propertyValues.get(Character.getNumericValue(nameOfJob.charAt(nameOfJob.length()-1)));
			apiflatfile(jsonArrayParse);
		} catch (Exception e) {
			LOGGER.error("Exception while using Starnik Job Execute method: "+StackTrace.getMessage(e));
		}
	}

	public void apiflatfile(JSONObject jsonArrayParse) {

		try {
			LOGGER.info("Scheduling Time Started for Starnik Api Flat file");
			File flatfilelocation = new File(jsonArrayParse.get("fileLocation").toString());
			if (flatfilelocation.canRead()) {
				File[] listoffiles = flatfilelocation.listFiles();
				for (int files = 0; files < listoffiles.length; files++) {
					if (listoffiles[files].getName().contains(jsonArrayParse.get("fileName").toString())) {
						LOGGER.info("Starnik Flatfile Exists "+listoffiles[files]);
						fileExists(listoffiles[files].toString(), jsonArrayParse);
					}
				}
			}
			else {
				LOGGER.warn("This file path location failed to read"+jsonArrayParse.get("fileLocation").toString());
			}
		} catch (Exception e) {
			LOGGER.error("Exception while using starnikFile Method: "+StackTrace.getMessage(e));
		}
	}

	public void fileExists(String starNikFile, JSONObject jsonArrayParse ) {
		try {
			api = new ApiOperations();
			api.setApiNeedValues(starNikFile, jsonArrayParse);
			LOGGER.info("Scheduler Application Ended for Starnik Api Flatfile");

		} catch (Exception e) {
			LOGGER.error("Exception while using fileexists method: "+StackTrace.getMessage(e));
		}

	}

}
