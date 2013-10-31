package com.mcdr.ecore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import com.mcdr.ecore.config.Config;
import com.mcdr.ecore.event.eCoreEventManager;

public abstract class GlobalDataManager extends Config{
	public static eCoreStage state = eCoreStage.INACTIVE;
	public static Location respawn = state.getRespawn();
	public static boolean invBypass = false;
	private static boolean saveNeeded = false;
	
	public static boolean advanceCheckpoint(){
		switch(state){
			case FINISHED:
				return false;
			case INACTIVE:
				eCoreEventManager.startEvent();
				break;
			case PROUDOFSUBJECT:
				eCoreEventManager.loungeHint();
				break;
			case TELEPORTER_BEFORE:
				eCoreEventManager.temporaryRetreat();
				break;
			case DOWNTHEDRAIN:
				eCoreEventManager.finalRetreat();
				break;
			case WITHTHEFISHES:
				eCoreEventManager.spawnGiant();
				break;
			case DEJAVU:
				eCoreEventManager.rememberTheNukes();
			default:			
		}
		
		state = state.next();
		state.setRespawn();
		Bukkit.broadcast(ChatColor.DARK_BLUE + "[eCore] " + ChatColor.WHITE + "Advanced to checkpoint: "+ ChatColor.GRAY +state.toString()+"(id: "+state.getID()+")", "eCore.*");
		saveNeeded = true;
		return true;
	}
	
	public static void setLogLevel(int level){
		if(level==eLogger.getLogLevel())
			return;
		eLogger.setLogLevel(level);
		saveNeeded = true;
	}
	
	public static void load(){
		eLogger.d("Loading Global data");
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
			yamlConfig.set("LogLevel", eLogger.getLogLevel());
			saveNow = true;
		}
		
		eLogger.setLogLevel(yamlConfig.getInt("LogLevel", eLogger.getLogLevel()));
		state = eCoreStage.fromID(yamlConfig.getInt("Checkpoint", state.getID()));
		state.setRespawn();
		invBypass = yamlConfig.getBoolean("invBypass", invBypass);
		
		if(saveNow)
			saveConfig(yamlConfig, "eCore" + DATAEXTENSION);
	}
	
	public static void save() {
		if(!saveNeeded)
			return;
		
		eLogger.d("Saving Global data");
		
		YamlConfiguration yamlConfig = loadConfig("eCore" + DATAEXTENSION);
		yamlConfig.set("Checkpoint", state.getID());
		yamlConfig.set("invBypass", invBypass);
		yamlConfig.set("LogLevel", eLogger.getLogLevel());
		saveConfig(yamlConfig, "eCore" + DATAEXTENSION);
		
		saveNeeded = false;
	}
}
