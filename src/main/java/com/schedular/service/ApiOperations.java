package com.schedular.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.schedular.util.StackTrace;

public class ApiOperations {
	Logger LOGGER = LogManager.getLogger(this.getClass());
	HttpClient httpClient = null;
	FlatFileReadandWrite write = null;

	public void sendRequests(List<String> requests, String starNik_File, JSONObject jsonArrayParse) {
		try {
			write = new FlatFileReadandWrite();
			for (int apiattempt = 0; apiattempt < 4; apiattempt++) {
				if (!requests.isEmpty()) {
					List<String> failedrequests = new ArrayList<>();
					for (int requestId = 0; requestId < requests.size(); requestId++) {
						if (!apiConnection(requests.get(requestId), jsonArrayParse)) {
							failedrequests.add(requests.get(requestId));
						} else {
							LOGGER.info("Its is success Response");
						}
					}
					requests.clear();
					requests.addAll(failedrequests);
				} else {
					LOGGER.info("Starnik flatfile is empty");
				}

			}
			write.writeFile(requests, starNik_File);
		} catch (Exception e) {
			LOGGER.error("Exception while using sendRequests method: " + StackTrace.getMessage(e));
		}
	}

	public boolean apiConnection(String requestBody, JSONObject jsonArrayParse) {
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(requestBody);
			httpClient = new HttpClient();
			JSONObject receviedpostCallHistoryResponse = httpClient.httpResponse(jsonObject.get("ucid").toString(),
					(JSONObject) jsonObject.get("StarnikUpdateRequest"), jsonArrayParse);
			if (receviedpostCallHistoryResponse == null) {
				LOGGER.info(
						"RESPONSE ----> CONNECTION TIMEOUT" + "  ," + "ucid" + "=" + jsonObject.get("ucid").toString());
			} else {
				if (receviedpostCallHistoryResponse.get("responseCode").toString() != null
						&& receviedpostCallHistoryResponse.get("responseCode").toString().equalsIgnoreCase("000")) {
					LOGGER.info("SUCCESS RESPONSE  AND RESPONSE CODE IS ----> "
							+ receviedpostCallHistoryResponse.get("responseCode").toString() + "  ," + "ucid" + "="
							+ jsonObject.get("ucid").toString());
					LOGGER.info(receviedpostCallHistoryResponse.get("responseBody").toString());
					return true;

				} else if (receviedpostCallHistoryResponse.get("responseCode").toString() != "000") {
					LOGGER.info("FAILURE RESPONSE  AND RESPONSE CODE IS ----> "
							+ receviedpostCallHistoryResponse.get("responseCode").toString() + "  ," + "ucid" + "="
							+ jsonObject.get("ucid").toString());

					LOGGER.info(receviedpostCallHistoryResponse.get("message").toString() + "FOR THIS REQUEST"
							+ (JSONObject) jsonObject.get("StarnikUpdateRequest"));
					return false;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception while using apiConnection Method: " + StackTrace.getMessage(e));
		}
		return false;
	}

	public void setApiNeedValues(String starNik_File, JSONObject jsonArrayParse) {
		try {
			write = new FlatFileReadandWrite();
			List<String> Allrequests = write.read(starNik_File);
			sendRequests(Allrequests, starNik_File, jsonArrayParse);

		} catch (Exception e) {
			LOGGER.error("Exception while using setApiNeedValues Method: " + StackTrace.getMessage(e));
		}

	}
}
