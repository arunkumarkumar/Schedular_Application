package com.schedular.util;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

public class DatabaseUtilty {
	static BasicDataSource sqlDataSource = null;
	static Logger LOGGER = LogManager.getLogger(DatabaseUtilty.class);

	public static BasicDataSource getSqlDataSource(JSONObject dbdetails) {
		try {
			sqlDataSource = new BasicDataSource();
			sqlDataSource.setDriverClassName(ApplicationConstants.driver);
			sqlDataSource.setUrl(dbdetails.get("sqlserverurl").toString());
			sqlDataSource.setUsername(dbdetails.get("sqlserverusername").toString());
			sqlDataSource.setPassword(dbdetails.get("sqlserverpassword").toString());
			sqlDataSource.setMinIdle(Integer.parseInt((dbdetails.get("minIdle").toString())));
			sqlDataSource.setMaxIdle(Integer.parseInt((dbdetails.get("maxIdle").toString())));
			sqlDataSource.setMaxOpenPreparedStatements(
					Integer.parseInt((dbdetails.get("maxpreparedstatement").toString())));
			return sqlDataSource;
		} catch (Exception ex) {
			LOGGER.error("Exception while using getSqlDataSource Method: " + StackTrace.getMessage(ex));
			return null;
		}
	}

}
