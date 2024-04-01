package com.schedular.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.simple.JSONObject;

public class ReadCsv {
	Map<String, Integer> columns = null;
	List<String> useridwithotspecialcharacters = null;
	DataFormatter data = null;
	Pattern pattern = null;
	Matcher matcher = null;
	Logger LOGGER = LogManager.getLogger(this.getClass());

	public List<String> readcsvfile(String csvlocation, String sheetname, JSONObject grxmldetails) {
		try {
			Workbook workBook = WorkbookFactory.create(new File(csvlocation));
			Sheet sheet = workBook.getSheet(sheetname);
			pattern = Pattern.compile("[^a-zA-Z0-9]");
			data = new DataFormatter();
			columns = new HashMap<>();
			useridwithotspecialcharacters = new ArrayList<String>();
			sheet.getRow(0).forEach(cell -> {
				columns.put(cell.getStringCellValue(), cell.getColumnIndex());
				if (columns.containsKey(grxmldetails.get("columnname").toString())) {
					for (int row = sheet.getFirstRowNum() + 1; row <= sheet.getLastRowNum(); row++) {
						String loginid = data.formatCellValue(sheet.getRow(row).getCell(columns.get(grxmldetails.get("columnname").toString()))).trim();
						matcher = pattern.matcher(loginid);
						boolean isStringContainsSpecialCharacter = false;
						isStringContainsSpecialCharacter = matcher.find();
						if (!isStringContainsSpecialCharacter) {
							useridwithotspecialcharacters.add(loginid);
						}
					}
				} else {
					LOGGER.warn("This Column Name" + grxmldetails.get("columnname").toString()
							+ " is not in the CSV file");
				}
			});
			workBook.close();
		} catch (IOException e) {
			LOGGER.error("IO Exception is Occured :" + StackTrace.getMessage(e));
		} catch (EncryptedDocumentException e) {
			LOGGER.error("EncryptedDocumentException is Occured :" + StackTrace.getMessage(e));
		} catch (Exception e) {
			LOGGER.error("Exception while using readexcelfile method" + StackTrace.getMessage(e));
		}

		return useridwithotspecialcharacters;

	}
}
