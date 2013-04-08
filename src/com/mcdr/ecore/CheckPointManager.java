package com.mcdr.ecore;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.mcdr.ecore.config.Config;

public class CheckPointManager extends Config{
	public static eCoreStage state = eCoreStage.INACTIVE;
	
	public static void load(){
		File f = LoadFile(DATAFOLDER + SEPERATOR + "eCore.dat", "com/mcdr/ecore/config/buttonconfig");
				
		if (f == null)
			return;
		
		YamlConfiguration yamlConfig = LoadConfig(f);
		yamlConfig.get("temp");//TODO Actual loading of data
	}
	
}
