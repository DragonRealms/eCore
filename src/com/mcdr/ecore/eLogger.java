package com.mcdr.ecore;

import java.util.logging.Level;

public abstract class eLogger {
	private static int level = Level.INFO.intValue();
	
	public static Level getLogLevel() {
		return Level.parse(level+"");
	}
	
	public static void setLogLevel(int lvl){
		level = lvl;
	}
	
	public static void setLogLevel(Level level) {
		setLogLevel(level.intValue());
	}
	
	public static void log(Level level, String msg){
		if(shouldLog(level))
			eCore.logger.log(level, "[eCore] "+msg);
	}
	
	public static void info(String msg) {
		log(Level.INFO, msg);
	}
	
	public static void debug(String msg){
		if(shouldLog(700))
			info("(DEBUG) "+msg);
	}
	
	public static void warning(String msg){
		log(Level.WARNING, msg);
	}
	
	public static void severe(String msg){
		log(Level.SEVERE, msg);
	}
	
	private static boolean shouldLog(Level level){
		if(level.equals(Level.OFF))
			return false;
		return level.intValue()>=eLogger.level;
	}
	
	private static boolean shouldLog(int level){
		return shouldLog(Level.parse(""+level));
	}
	
	// Shortcuts
	
	public static void l(Level level, String msg){log(level, msg);}
	public static void i(String msg){info(msg);}
	public static void d(String msg){debug(msg);}
	public static void w(String msg){warning(msg);}
	public static void s(String msg){severe(msg);}
}
