package com.mcdr.ecore.player;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.mcdr.ecore.config.Config;

public class PlayerDataManager extends Config {
	public static void load(){
		File f = LoadFile(DATAFOLDER + SEPERATOR + "eCore.dat", "com/mcdr/ecore/config/buttonconfig");
				
		if (f == null)
			return;
		
		YamlConfiguration yamlConfig = LoadConfig(f);
	}
}
