package com.mcdr.ecore.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.YamlConfiguration;

import com.mcdr.ecore.eCore;

public abstract class Config {
	protected final static char SEPERATOR = File.separatorChar;
	protected final static String DATAFOLDER = eCore.in.getDataFolder().getPath();
	
	protected static File LoadFile(String filePath, String resourcePath) {
		File file = new File(filePath);
		
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			
			try {
				InputStream inputStream = eCore.in.getResource(resourcePath);
				
				if (inputStream == null) {
					eCore.logger.severe("[eCore] Missing resource file: '" + resourcePath + "', please notify the plugin author");
					return null;
				}
				else {
					eCore.logger.info("[eCore] Creating default config file: " + file.getName());
					StreamToFile(inputStream, file);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return file;
	}
	
	protected static YamlConfiguration LoadConfig(File file) {
		YamlConfiguration yamlConfig = new YamlConfiguration();
		
		try {
			yamlConfig.load(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return yamlConfig;
	}
	
	public static void StreamToFile(InputStream resource, File file) throws Exception {
		OutputStream outputStream = new FileOutputStream(file);
		
		Copy(resource, outputStream);
	}
	
	private static void Copy(InputStream inputStream, OutputStream outputStream) throws Exception {
		int read = 0;
		byte[] bytes = new byte[1024];
		
		while ((read = inputStream.read(bytes)) != -1)
			outputStream.write(bytes, 0, read);
		
		inputStream.close();
		outputStream.close();
	}
}