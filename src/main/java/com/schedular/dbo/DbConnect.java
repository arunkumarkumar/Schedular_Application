package com.schedular.dbo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.schedular.util.DatabaseUtilty;
import com.schedular.util.StackTrace;

public class DbConnect {
	Logger LOGGER = LogManager.getLogger(this.getClass());

	public List<String> insertHistory(List<String> dbrequests, JSONObject dbdetails,
			Map<Object, Object> keyandvalueorder, String storedprocedure) {
		List<String> failedRequests = new ArrayList<String>();
		Map<Integer, String> columntypes = new LinkedHashMap<>();
		try {
			Connection connect = DatabaseUtilty.getSqlDataSource(dbdetails).getConnection();
			if (connect != null) {
				LOGGER.info("DbConnection is Established for DbInsert process");
				CallableStatement preparedStatement = connect.prepareCall(storedprocedure);
				ParameterMetaData metaData = preparedStatement.getParameterMetaData();
				for (int requests = 0; requests < dbrequests.size(); requests++) {
					try {
						JSONObject jsonObject = (JSONObject) new JSONParser().parse(dbrequests.get(requests));
						int columnId = 0;
						for (Entry<Object, Object> entry : keyandvalueorder.entrySet()) {
							++columnId;
							columntypes.put(columnId, metaData.getParameterTypeName(columnId));
							if (columntypes.get(columnId).equalsIgnoreCase("int")) {
								preparedStatement.setInt(columnId,
										Integer.parseInt(jsonObject.get(entry.getKey()).toString()));
							} else {
								preparedStatement.setString(columnId,
										jsonObject.get(entry.getKey().toString()).toString());
							}
						}
						columntypes.clear();
						preparedStatement.addBatch();
					} catch (Exception e) {
						LOGGER.error("Exception while using DbConnect method:" + StackTrace.getMessage(e));
					}
				}
				int[] results = preparedStatement.executeBatch();
				for (int count = 0; count < results.length; count++) {
					if (results[count] == Statement.SUCCESS_NO_INFO || results[count] >= 0) {
						LOGGER.info("Successfully Inserted Request--> " + dbrequests.get(count));
					} else {
						LOGGER.info("Failed Inserted Request--> " + dbrequests.get(count));
						failedRequests.add(dbrequests.get(count));
					}
				}
				return failedRequests;

			} else {
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("Exception while using DbConnect method:" + StackTrace.getMessage(e));
		}
		return null;

	}
}
