package com.schedular.service;

import java.io.File;
import java.io.FileWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.schedular.util.StackTrace;

public class GrxmlWrite {
	Logger LOGGER = LogManager.getLogger(this.getClass());

	public void write(String fileLocation, String content) {
		try {
			FileWriter filewritter = new FileWriter(new File(fileLocation));
			filewritter.write(content);
			filewritter.close();
		} catch (Exception e) {
			LOGGER.error("Exception while using Grxml write method: "+StackTrace.getMessage(e));
		}
	}

}
