package com.mcdr.ecore.config;

import com.mcdr.ecore.GlobalDataManager;

public abstract class ConfigManager {
	public static void load() {
		ButtonConfig.load();
		GlobalDataManager.load();
	}
}

