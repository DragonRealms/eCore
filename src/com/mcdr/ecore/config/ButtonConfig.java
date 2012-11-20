package com.mcdr.ecore.config;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class ButtonConfig extends Config{

	public ButtonConfig() {
		
	}
	
	public static void load(){
		File f = LoadFile("plugins/eCore/buttonconfig.yml", "com/mcdr/ecore/config/buttonconfig");
				
		if (f == null)
			return;
		
		YamlConfiguration yamlConfig = LoadConfig(f);
		
		LoadButtons(yamlConfig);
	}
	
	private static void LoadButtons(YamlConfiguration yamlConfig){
		
	}

}
