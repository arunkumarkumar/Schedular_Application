package com.schedular.service;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import com.schedular.dbo.DbConnect;
import com.schedular.util.StackTrace;

public class DbService {
	Logger LOGGER = LogManager.getLogger(this.getClass());
	FlatFileReadandWrite write = null;
	DbConnect dao = null;

	public void dbservice(String filePath, JSONObject dbdetails, String storedprocedure) {
		try {
			write = new FlatFileReadandWrite();
			dao = new DbConnect();
			List<String> queries = write.read(filePath);
			Map<Object, Object> keyandvalue = write.readValuesFromJSON(filePath);
			if (queries != null) {
				if (!queries.isEmpty()) {
					List<String> failedRequests = dao.insertHistory(queries, dbdetails, keyandvalue,storedprocedure);
					if (failedRequests != null) {
						if (!failedRequests.isEmpty()) {
							write.writeFile(failedRequests, filePath);
						} else {
							LOGGER.info("All DbRequests are Successfully inserted");
							write.writeFile(failedRequests, filePath);
						}
					}
				} else {
					LOGGER.info("Dbrequests are empty in the file");
				}
			} else {
				LOGGER.warn("Dbrequests are null");
			}
		} catch (Exception e) {
			LOGGER.error("Exception while using DbService method:" + StackTrace.getMessage(e));
		}
	}
}
