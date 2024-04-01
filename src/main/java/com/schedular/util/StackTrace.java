package com.schedular.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTrace {

	public static String getMessage(Exception ex) {
		StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
	}

}
