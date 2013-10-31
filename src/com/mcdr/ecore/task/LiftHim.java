package com.mcdr.ecore.task;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.mcdr.ecore.eCore;

public class LiftHim extends Thread {
	Player him;

	public LiftHim(Player him) {
		this.him = him;
	}

	@Override
	public void run() {
		Location l = him.getLocation();
		eCore.getWorld().createExplosion(l.getX(), l.getY(), l.getZ(), 100F, false, false);
		him.setVelocity(new Vector(0,1,0));
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick "+eCore.name + " Because Valorah demands it.");
	}

}
