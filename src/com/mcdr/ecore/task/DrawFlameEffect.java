package com.mcdr.ecore.task;

import org.bukkit.Effect;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.mcdr.ecore.eCore;

public class DrawFlameEffect extends BaseTask {

	@Override
	public void run() {
		try{
			Player player = eCore.server.getPlayer("Kraeghnor");
			World world = player.getWorld();
			world.playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
		} catch(NullPointerException e){}
	}

}
