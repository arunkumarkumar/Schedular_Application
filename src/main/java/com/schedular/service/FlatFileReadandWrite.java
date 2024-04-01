package com.schedular.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedular.util.StackTrace;

public class FlatFileReadandWrite {
	Logger LOGGER = LogManager.getLogger(this.getClass());
	List<String> queries = null;
	Map<Object, Object> valuesMap=null;
	ObjectMapper objectMapper =null;
	public void writeFile(List<String> requests, String file) {
		try {
			if (requests.size() > 0) {
				try (BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(file))) {
					for (String line : requests) {
						bufferWriter.write(line);
						bufferWriter.newLine();
					}
					bufferWriter.close();
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
					LOGGER.error(e.getStackTrace());
				}
			} else if (requests.size() == 0) {
				File write = new File(file);
				write.delete();
				LOGGER.info("Flat File Deleted Successfully");

			}

		} catch (Exception e) {
			LOGGER.error("Exception while using writefile method: "+StackTrace.getMessage(e));
		}
	}

	public List<String> read(String filePath) {
		 queries = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				queries.add(line);
			}
		} catch (Exception e) {
			LOGGER.error("Exception while using read file method: "+StackTrace.getMessage(e));
			return queries;
		}
		return queries;
	}

	public Map<Object, Object> readValuesFromJSON(String filePath) {
		try {
		ObjectMapper objectMapper = new ObjectMapper();
		valuesMap = new HashMap<>();
			valuesMap = objectMapper.readValue(new File(filePath), new TypeReference<Map<Object, Object>>() {
			});
		} catch (IOException e) {
			LOGGER.error(StackTrace.getMessage(e));
		}

		return valuesMap;

	}

}
