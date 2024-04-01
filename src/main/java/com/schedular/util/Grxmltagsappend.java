package com.schedular.util;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Grxmltagsappend {
	Logger LOGGER = LogManager.getLogger(this.getClass());
	StringBuilder line =null;
	public  StringBuilder dataxmltagsappend(List<String> list)  {
		try {
			line = new StringBuilder();
			String content = "";
			for (String loginid : list) {
				char[] characters = loginid.toCharArray();
				for (int character = 0; character <= loginid.length(); character++) {
					if (character == 0) {
						line.append("          ");
						content = ApplicationConstants.starttag + characters[character];
						line.append(content);
					} else {
						if (character == loginid.length()) {
							content = ApplicationConstants.endtag + ApplicationConstants.slash + loginid + ApplicationConstants.slash+ ApplicationConstants.end;
							line.append(content);
						} else {
							content = ApplicationConstants.middletag + characters[character];
							line.append(content);
						}
					}
				}
				line.append("\n");
			}
			return line;
		} catch (Exception e) {
			LOGGER.error("Exception while using Grxml prepare Method: "+StackTrace.getMessage(e));
		}
		return null;
	}

}
