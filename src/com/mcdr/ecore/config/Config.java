package com.mcdr.ecore.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.YamlConfiguration;

import com.mcdr.ecore.eCore;
import com.mcdr.ecore.eLogger;

public abstract class Config {
	protected final static char SEPERATOR = File.separatorChar;
	protected final static String DATAFOLDER = eCore.in.getDataFolder().getPath();
	protected final static String DATAEXTENSION = ".dat";
	protected final static String CONFIGEXTENSION = ".yml";
	
	//Loading Files
	
	protected static File loadFile(String fileName, boolean inPlayersFolder) {
		File file = getFile(fileName, inPlayersFolder);
		
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	protected static File loadFile(String fileName, boolean inPlayersFolder, String resourcePath) {
		File file = getFile(fileName, inPlayersFolder);
		
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			
			try {
				InputStream inputStream = eCore.in.getResource(resourcePath);
				
				if (inputStream == null) {
					eLogger.s("Missing resource file: '" + resourcePath + "', please notify the plugin author");
					return null;
				}
				else {
					eLogger.i("Creating default config file: " + file.getName());
					streamToFile(inputStream, file);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return file;
	}
	
	protected static YamlConfiguration loadConfig(String fileName) {
		return loadConfig(fileName, null, false);
	}
	
	protected static YamlConfiguration loadConfig(String fileName, boolean inPlayersFolder) {
		return loadConfig(fileName, null, inPlayersFolder);
	}
	
	protected static YamlConfiguration loadConfig(String fileName, String resourcePath) {
		return loadConfig(fileName, resourcePath, false);
	}
	
	protected static YamlConfiguration loadConfig(String fileName, String resourcePath, boolean inPlayersFolder) {
		File file = resourcePath==null?loadFile(fileName, inPlayersFolder):loadFile(fileName, inPlayersFolder, resourcePath);
		YamlConfiguration yamlConfig = new YamlConfiguration();
		
		if (file == null){
			eLogger.s("Unable to load or create the " + fileName + DATAEXTENSION + " file. Is something wrong with your filesystem?");
			return null;
		}
		
		try {
			yamlConfig.load(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return yamlConfig;
	}
	
	//Saving Files
	
	protected static void saveConfig(YamlConfiguration yamlConfig, String fileName){
		saveConfig(yamlConfig, fileName, false);
	}
	
	protected static void saveConfig(YamlConfiguration yamlConfig, String fileName, boolean inPlayersFolder){
		try{
			yamlConfig.save(getFilePath(inPlayersFolder)+fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//Other methods
	
	public static String getFilePath(boolean inPlayersFolder){
		return DATAFOLDER + SEPERATOR + (inPlayersFolder?"Players" + SEPERATOR:"");
	}
	
	public static File getFile(String fileName, boolean inPlayersFolder){
		return new File(getFilePath(inPlayersFolder), fileName);
	}
	
	public static void streamToFile(InputStream resource, File file) throws Exception {
		OutputStream outputStream = new FileOutputStream(file);
		
		copy(resource, outputStream);
	}
	
	private static void copy(InputStream inputStream, OutputStream outputStream) throws Exception {
		int read = 0;
		byte[] bytes = new byte[1024];
		
		while ((read = inputStream.read(bytes)) != -1)
			outputStream.write(bytes, 0, read);
		
		inputStream.close();
		outputStream.close();
	}
	
	
}