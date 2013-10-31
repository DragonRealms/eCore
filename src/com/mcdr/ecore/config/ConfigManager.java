package com.mcdr.ecore.config;

import com.mcdr.ecore.GlobalDataManager;
import com.mcdr.ecore.player.PlayerManager;

public abstract class ConfigManager {
	public static void load() {
		PlayerManager.initializeArrays();
		GlobalDataManager.load();
	}
}

