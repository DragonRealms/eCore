package com.mcdr.ecore.task;

import com.mcdr.ecore.GlobalDataManager;
import com.mcdr.ecore.eLogger;
import com.mcdr.ecore.player.PlayerManager;

public class DataSaveTask extends BaseTask {

	@Override
	public void run() {
		PlayerManager.saveAll();
		GlobalDataManager.save();
		eLogger.d("Autosaving playerData...");
	}

}
