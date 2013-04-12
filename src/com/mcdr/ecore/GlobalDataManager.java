package com.mcdr.ecore;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mcdr.ecore.config.Config;

public abstract class GlobalDataManager extends Config{
	public static eCoreStage state = eCoreStage.INACTIVE;
	public static Location respawn = state.getRespawn();
	public static boolean invBypass = false;
	private static boolean saveNeeded = false;
	
	public static void load(){
		boolean saveNow = false;
		YamlConfiguration yamlConfig = loadConfig("eCore" + DATAEXTENSION);
				
		if(!yamlConfig.isSet("Checkpoint")){
			yamlConfig.set("Checkpoint", state.getID());
			saveNow = true;
		}
		if(!yamlConfig.isSet("invBypass")){
			yamlConfig.set("invBypass", invBypass);
			saveNow = true;
		}
		if(!yamlConfig.isSet("LogLevel")){
			yamlConfig.set("LogLevel", eLogger.getLogLevel().intValue());
			saveNow = true;
		}
		
		eLogger.setLogLevel(yamlConfig.getInt("LogLevel", eLogger.getLogLevel().intValue()));
		state = eCoreStage.fromID(yamlConfig.getInt("Checkpoint", state.getID()));
		state.setRespawn();
		invBypass = yamlConfig.getBoolean("invBypass", invBypass);
		
		if(saveNow)
			saveConfig(yamlConfig, "eCore" + DATAEXTENSION);
	}
	
	public static void advanceCheckpoint(){
		if(state.isLast())
			return;
		state = state.next();
		state.setRespawn();
		saveNeeded = true;
	}
	
	public static void save() {
		if(!saveNeeded)
			return;
		
		YamlConfiguration yamlConfig = loadConfig("eCore" + DATAEXTENSION);
		yamlConfig.set("Checkpoint", state.getID());
		yamlConfig.set("invBypass", invBypass);
		yamlConfig.set("LogLevel", eLogger.getLogLevel().intValue());
		saveConfig(yamlConfig, "eCore" + DATAEXTENSION);
		
		saveNeeded = false;
	}
}
