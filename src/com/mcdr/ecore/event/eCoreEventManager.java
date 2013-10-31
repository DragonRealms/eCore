package com.mcdr.ecore.event;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.mcdr.corruption.CorruptionAPI;
import com.mcdr.ecore.eCore;
import com.mcdr.ecore.eLogger;
import com.mcdr.ecore.task.LiftHim;
import com.mcdr.ecore.util.legacy.ItemNames;

public abstract class eCoreEventManager {
	public static void startEvent() {
		eLogger.d("Running Start Event");
		Bukkit.broadcastMessage(ChatColor.DARK_RED+"Kraeghnor has invaded Area51!"+ChatColor.DARK_GRAY+"\nWEB Labs asks for your help. Join the fight with: "+ChatColor.WHITE+ChatColor.BOLD+ "/ecore join");
	}
	
	@SuppressWarnings("deprecation")
	public static void temporaryRetreat(){
		eLogger.d("Running Temporary Retreat Event");
		Location loc = new Location(eCore.getWorld(), -110, 21, 139);
		Block b = loc.getBlock();
		b.setTypeIdAndData(ItemNames.LEVER.getId(), (byte) 0x6, false);
		World w = loc.getWorld();
		w.strikeLightningEffect(loc);
		for(int i = 0;i<100;i++)
			for(double j = 0.0; j <= 1.0; j+=0.25)
				w.playEffect(loc.clone().add(0.0, j, 0.0),Effect.SMOKE, i, 16);
		Player him = Bukkit.getPlayer(eCore.name);
		him.teleport(new Location(eCore.getWorld(), -202.5, 24.0, 76.5, -45f, 0f));
		him.setHealth(him.getMaxHealth());
		broadcastToPlayers("You think this will be the end of it?\nTrust me, it has barely begun.");
	}
	
	public static void spawnGiant() {
		eLogger.d("Running Giant Spawn Event");
		Location spawnLocation = new Location(eCore.getWorld(), -211.0, 27.0, -27.0);
		boolean giantSpawned = false;
		
		if(eCore.corruptionEnabled){
			List<String> giants = CorruptionAPI.getBossNames().get(EntityType.GIANT);
			if(giants.size()>0){
				giantSpawned = CorruptionAPI.spawnBoss(spawnLocation.getBlock().getLocation(), giants.get(0))!=null;
			}
		}
		if(!giantSpawned){
			spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.GIANT);
			giantSpawned = true;
		}
		
	}

	public static void finalRetreat() {
		eLogger.d("Running Final Retreat Event");
		Player him = Bukkit.getPlayer(eCore.name);
		if(him==null)
			return;
		
		him.setGameMode(GameMode.CREATIVE);
		him.closeInventory();
		him.setFlying(true);
		him.setVelocity(new Vector(0,10,0));
		
		broadcastToPlayers("So it ends...");
		
		eCore.scheduler.scheduleSyncDelayedTask(eCore.in, new LiftHim(him), (long) (5 * 20));		
	}

	public static void loungeHint() {
		broadcastToPlayers("You need to spread your pressure over multiple points if you ever want to succeed.");
	}

	public static void rememberTheNukes() {
		broadcastToPlayers("Remember the nukes you fired?");
	}
	
	public static void broadcastToPlayers(String msg){
		broadcastToPlayers(ChatColor.DARK_GRAY+eCore.name+ChatColor.DARK_RED, msg);
	}
	
	public static void broadcastToPlayers(String prefix, String msg){
		List<Player> players = eCore.getWorld()!=null?eCore.getWorld().getPlayers():null;
		if(players==null)
			return;
		for(Player player: players){
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" "+msg));
		}
	}
}
