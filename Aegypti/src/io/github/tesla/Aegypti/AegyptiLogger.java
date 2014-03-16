package io.github.tesla.Aegypti;

import java.util.logging.Logger;

public class AegyptiLogger {
	private static Logger logger = null;
	public static Logger getLogger() {
		if (logger==null) {
			logger = Logger.getLogger(AegyptiDriver.class.getName());
		}
		return logger;
	} 
}
