package com.schedular.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.schedular.util.Grxmltagsappend;
import com.schedular.util.LoadJsonConfig;
import com.schedular.util.ReadCsv;
import com.schedular.util.StackTrace;

public class GrxmlgeneratorScheduler implements Job {
	Logger LOGGER = LogManager.getLogger(this.getClass());
	FlatFileReadandWrite read=null;
	StringBuilder header = null;;
	StringBuilder footer=null;
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JSONArray propertyValues = LoadJsonConfig.xmlArray;
			String nameOfJob = context.getJobDetail().getKey().getName();
			JSONObject grxmldetails = (JSONObject) propertyValues
					.get(Character.getNumericValue(nameOfJob.charAt(nameOfJob.length() - 1)));
			grxmlgenerate(grxmldetails);
		} catch (Exception e) {
			LOGGER.error("Exception while using Grxmlgenerator Job Execute Method: "+StackTrace.getMessage(e));
		}
	}

	public void grxmlgenerate(JSONObject grxmldetails) {
		StringBuilder grxmlcontent;
		 header = new StringBuilder();
   		 footer= new StringBuilder();
		try {
			String date = "NA";
			Calendar cal  = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			SimpleDateFormat s = new SimpleDateFormat(grxmldetails.get("inputformatdate").toString());
			date = s.format(new Date(cal.getTimeInMillis()));
			read=new FlatFileReadandWrite();
			List<String> grxmlvalues=read.read(grxmldetails.get("xmltemplatelocation").toString());
			for(int grxml=0;grxml<grxmlvalues.size();grxml++) {
       			if(grxmlvalues.get(grxml).trim().equalsIgnoreCase("<one-of>")) {
       				for(int xmlheader=0;xmlheader<=grxml;xmlheader++) {
       					header.append(grxmlvalues.get(xmlheader)+"\r\n");
       				}
       			
       			if(header != null) {
       				for(int xmlfooter=grxml+1;xmlfooter<grxmlvalues.size();xmlfooter++) {
       					footer.append(grxmlvalues.get(xmlfooter)+"\r\n");
       				}
       			}
       			else {
       				LOGGER.info("Grxml template file  Header is Null");
       			}
       			}
       		}
			String csvlocation = grxmldetails.get("csvlocation").toString()+grxmldetails.get("csvfilename").toString() + date + ".xlsx";
			File csv = new File(csvlocation);
			LOGGER.info("Scheduling Time Started for Grxmlgenerator");
			if(csv.exists()) {
			grxmlcontent = new Grxmltagsappend().dataxmltagsappend(new ReadCsv().readcsvfile(csvlocation, grxmldetails.get("sheetname").toString(),grxmldetails));
			if(grxmlcontent==null) {
				LOGGER.info("Grxml Appended  Login id Tags are Null");
			}else {
			String content = header + grxmlcontent.toString() + footer;
			DateFormat dateformat = new SimpleDateFormat(grxmldetails.get("outputformatdate").toString());
			String uniquefilename = grxmldetails.get("outputfileName").toString() + dateformat.format(Calendar.getInstance().getTime())+ grxmldetails.get("extension");
			new GrxmlWrite().write(grxmldetails.get("grxmlgeneratedlocation").toString() + uniquefilename, content);
			LOGGER.info("Grxml File Generated for this Csv file"+csvlocation);
			LOGGER.info("Scheduler Application Ended for Grxmlgenerator");
			}
			}else {
				LOGGER.info("Csv file is not exist for this date:"+date);
			}

		} catch (Exception e) {
			LOGGER.error("Exception while using xmlGenerate Method: "+StackTrace.getMessage(e));
		}
	}

}
