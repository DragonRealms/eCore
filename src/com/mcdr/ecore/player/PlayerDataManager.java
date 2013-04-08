package com.mcdr.ecore.player;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import com.mcdr.ecore.config.Config;

public class PlayerDataManager extends Config {
	public static eCorePlayer loadPlayerData(eCorePlayer player){
		eCorePlayer ePlayer = new eCorePlayer();
		return ePlayer;
	}
	
	public static void savePlayerData(eCorePlayer player){
		
	}
	
	public static PlayerInventory loadInventory(Player player, boolean eventInv){
		File f = LoadFile(DATAFOLDER + SEPERATOR + "players" + SEPERATOR + player.getName().toLowerCase() + ".dat");
				
		if (f == null)
			return null;
		
		YamlConfiguration yamlConfig = LoadConfig(f);
		yamlConfig.get("temp");//TODO Actual loading of data
		return null;
	}
	
	public static void saveInventory(Player player, boolean eventInv){
		
	}
}
