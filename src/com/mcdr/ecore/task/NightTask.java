package com.mcdr.ecore.task;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.mcdr.ecore.eCore;

public class NightTask extends BaseTask{
	private World world = null;

	@Override
	public void run() {
		if(world==null)
			if((world = Bukkit.getWorld(eCore.worldname))==null)
				return;
		world.setTime(18000L);
		
		if(world.getWeatherDuration()<40 || !world.hasStorm() || !world.isThundering()){
			world.setStorm(true);
			world.setThundering(true);
		}
	}

}
