package com.schedular.util;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LoadJsonConfig {
	public static JSONArray flatFileArray;
	public static JSONArray xmlArray;
	public static JSONArray dbArray;
	public static JSONArray dbdetails;
	static Logger LOGGER = LogManager.getLogger(LoadJsonConfig.class);
	static JSONParser jsonParser = null;
	public static void load() {
		try {
			jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(ApplicationConstants.externaljson));
			JSONObject flatFile = (JSONObject) jsonObject.get("FlatFileLocation");
			JSONObject xml = (JSONObject) jsonObject.get("GrxmlGenerator");
			JSONObject db = (JSONObject) jsonObject.get("DbQuery");
			ApplicationConstants.flatFileJob = flatFile.get("nameOfJob").toString();
			ApplicationConstants.flatFileGroup = flatFile.get("nameOfGroup").toString();
			ApplicationConstants.flatFileTrigger = flatFile.get("nameOfTrigger").toString();
			ApplicationConstants.xmlJob = xml.get("nameOfJob").toString();
			ApplicationConstants.xmlGroup = xml.get("nameOfGroup").toString();
			ApplicationConstants.xmlTrigger = xml.get("nameOfTrigger").toString();
			flatFileArray = (JSONArray) flatFile.get("FlatFile");
			xmlArray = (JSONArray) xml.get("Xml");
			dbArray = (JSONArray) db.get("DbDetails");
			LOGGER.info("External Config file Loaded ");
		} catch (FileNotFoundException e) {
			LOGGER.error("FileNotFoundException has Occurred : " + StackTrace.getMessage(e));
		} catch (Exception e) {
			LOGGER.error("Exception while using LoadProperty: " + StackTrace.getMessage(e));

		}
	}
}
