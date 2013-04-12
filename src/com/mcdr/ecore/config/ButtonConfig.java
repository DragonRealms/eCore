package com.mcdr.ecore.config;

import org.bukkit.configuration.file.YamlConfiguration;

public class ButtonConfig extends Config{

	public ButtonConfig() {
		
	}
	
	public static void load(){	
		YamlConfiguration yamlConfig = loadConfig("buttonconfig"+CONFIGEXTENSION, "com/mcdr/ecore/config/buttonconfig"+CONFIGEXTENSION);
		LoadButtons(yamlConfig);
	}
	
	private static void LoadButtons(YamlConfiguration yamlConfig){
		
	}

}
