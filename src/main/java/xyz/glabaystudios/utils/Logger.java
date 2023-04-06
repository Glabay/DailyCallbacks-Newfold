package xyz.glabaystudios.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Logger {

	static Logger logger;

	private String loggerLevel = "INFO";

	public static Logger getLogger() {
		if (logger == null) logger = new Logger();
		return logger;
	}

	public static Logger getLogger(String loggingLevel) {
		if (logger == null) logger = new Logger();
		logger.loggerLevel = loggingLevel.toUpperCase();
		return logger;
	}

	public void printStringMessage(String message) {
		printWithTimeStamp(message);
	}

	public void printStringMessageFormatted(String message, Object... objects) {
		printWithTimeStamp(String.format(message, objects));
	}

	private void printWithTimeStamp(String message) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		if (Objects.equals(loggerLevel, "ERROR"))
			System.err.printf("[%s] [ ERROR ] %s%n", dtf.format(now), message);
		else if (Objects.equals(loggerLevel, "WARN"))
			System.out.printf("[%s] [ WARN ] %s%n", dtf.format(now), message);
		else if (Objects.equals(loggerLevel, "NULL"))
			System.err.printf("[%s] [ NULL ] %s%n", dtf.format(now), message);
		else
			System.out.printf("[%s] [ INFO ] %s%n", dtf.format(now), message);
	}
}
