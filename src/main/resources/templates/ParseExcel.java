package com.schedular.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

public class ParseExcel {
	Map<String, Integer> columns = new HashMap<>();
	LinkedHashSet<String> userid = new LinkedHashSet<String>();
	LinkedHashSet<String> useridwithotspecialcharacters = new LinkedHashSet<String>();
	DataFormatter data = null;
	Logger LOGGER = LogManager.getLogger(this.getClass());

	public LinkedHashSet<String> removeSpecialCharactors(HashSet<String> list) {
		Pattern pattern = null;
		Matcher matcher = null;
		boolean isStringContainsSpecialCharacter = false;

		pattern = Pattern.compile("[^a-zA-Z0-9]");
		try {
			for (String userid : list) {
				matcher = pattern.matcher(userid);

				isStringContainsSpecialCharacter = matcher.find();
				if (!isStringContainsSpecialCharacter) {
					useridwithotspecialcharacters.add(userid);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception while using removeSpecialCharacters Method: " + StackTrace.getMessage(e));
		}
		return useridwithotspecialcharacters;
	}
	public LinkedHashSet<String> parse(String excellocation, String sheetname) {
		try {
			Workbook workBook = WorkbookFactory.create(new File(excellocation));
			Sheet sheet = workBook.getSheet(sheetname);
			DataFormatter data = new DataFormatter();
			sheet.getRow(0).forEach(cell -> {
				columns.put(cell.getStringCellValue(), cell.getColumnIndex());
			});
			for (int column = 0; column < columns.size(); column++) {
				try {
					if (columns.containsKey("LoginId")) {
						for (int row = sheet.getFirstRowNum() + 1; row <= sheet.getLastRowNum(); row++) {
							try {
								String id = data.formatCellValue(sheet.getRow(row).getCell(columns.get("LoginId")))
										.trim();
								userid.add(id);

							} catch (Exception e) {
								LOGGER.error(e.getStackTrace());
							}
						}

					}

				} catch (Exception e) {
					LOGGER.error(e.getStackTrace());
				}
			}
			try {
				workBook.close();
			} catch (IOException e) {
				LOGGER.error("Exception while using Csv parse Method: " + StackTrace.getMessage(e));
			}
		} catch (EncryptedDocumentException e) {
			LOGGER.error("Exception while using Csv parse Method: " + StackTrace.getMessage(e));
		} catch (Exception e) {
			LOGGER.error("Exception while using Csv parse Method: " + StackTrace.getMessage(e));
		}

		return removeSpecialCharactors(userid);

	}
}
