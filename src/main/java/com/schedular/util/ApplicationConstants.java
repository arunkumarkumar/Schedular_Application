package com.schedular.util;

import java.io.File;

public class ApplicationConstants {
	public static String externaljson = new File(System.getProperty("java.class.path")).getAbsoluteFile()
			.getParentFile().getAbsolutePath().concat("\\Config.json");
	public static String flatFileJob;
	public static String flatFileGroup;
	public static String flatFileTrigger;
	public static String xmlJob;
	public static String xmlGroup;
	public static String xmlTrigger;
	public static String dbJob;
	public static String dbGroup;
	public static String dbTrigger;
	public static String starttag = "<item><item>";
	public static String middletag = "</item><item>";
	public static String endtag = "</item><tag> out = ";
	public static String end = "; </tag></item>";
	public static String slash = ("\"");
	public static int job;
	public static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

}
