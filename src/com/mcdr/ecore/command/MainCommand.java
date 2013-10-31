package com.mcdr.ecore.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.mcdr.ecore.GlobalDataManager;
import com.mcdr.ecore.eCore;
import com.mcdr.ecore.eLogger;
import com.mcdr.ecore.player.PlayerManager;
import com.mcdr.ecore.player.eCorePlayer;

public abstract class MainCommand extends BaseCommand {
	private static final Map<String, Integer> TOGGLES = new HashMap<String, Integer>();
	static {
		TOGGLES.put("toggle", 0);
		TOGGLES.put("on", 1);
		TOGGLES.put("off", 2);
		TOGGLES.put("true", 1);
		TOGGLES.put("false", 2);
	}
	
	private static final double[][] tpCoords = {
		{-296.5, 27.0, -118.5, -45.0}, //Far Away
		{- 48.5, 33.0, -111.5, -90.0}, //Lava bridge
		{-109.5, 21.0,  139.5, 180.0}, //Control room
		{-109.5,  1.0,  146.5,   0.0}, //Control room switch
		{-124.5, 28.0,  141.5, -55.0}, //Control room platform
		{-202.5, 24.0,   76.5, -45.0}, //Cave stalker
		{-536.5, 65.0,   60.5, -90.0}  //Final battle
	};
	
	public static void process(){
		if(args.length<1)
			return;
		
		String arg = args[0].toLowerCase();
		if(eqIC(arg, "tp")){
			if(args.length<2 || !eqIC(sender.getName(), eCore.name))
				return;
			int tp = -1;
			try{
				tp = Integer.parseInt(args[1]);
			} catch(NumberFormatException e){
				return;
			}
			if(tp>=tpCoords.length || tp<0)
				return;
			Player him;
			if((him = Bukkit.getPlayer(eCore.name))!=null && eCore.getWorld()!=null){
				him.teleport(new Location(eCore.getWorld(), tpCoords[tp][0], tpCoords[tp][1], tpCoords[tp][2], (float) tpCoords[tp][3], 0f));
				processed = true;
			}
		} else if(orEqIC(arg, "join", "enter")){
			if(!(sender instanceof Player)) checkPermission("", false);
			if(GlobalDataManager.state.isFirst()){
				sender.sendMessage(ChatColor.DARK_BLUE + "[eCore] " + ChatColor.WHITE + "The event needs to be started first, please have a little patience. ;)");
			} else {
				eCorePlayer ePlayer = PlayerManager.getPlayer((Player) sender);
				if(ePlayer != null && !ePlayer.isInEventWorld()){
					((Player) sender).teleport(GlobalDataManager.state.getRespawn());
					sender.sendMessage(ChatColor.DARK_GRAY + eCore.name + ChatColor.DARK_RED + " Welcome in my world, where leaving unscathed is not an option.");
				}
			}
			processed = true;
		} else if(orEqIC(arg, "invbypass","inventorybypass", "ibp")){
			if(args.length<2){
				localBypass("");
			} else {
				arg = args[1].toLowerCase();
				if(TOGGLES.containsKey(arg)||eqIC(arg, "show")){
					localBypass(arg);
				} else if(orEqIC(arg, "global", "g")){
					if(args.length<3 || !TOGGLES.containsKey(args[2].toLowerCase())){
						globalBypass("");
						return;
					} else {
						globalBypass(args[2].toLowerCase());
					}
				} else if(orEqIC(arg, "local", "l")){
					if(args.length<3){
						localBypass("");
					} else if(!TOGGLES.containsKey(args[2].toLowerCase())){
						if(args.length<4||!TOGGLES.containsKey(args[3].toLowerCase())){
							localBypass("", args[2]);
						} else {
							localBypass(args[3], args[2]);
						}
					} else {
						localBypass(args[2]);
					}
				} else {
					if(args.length<3||!TOGGLES.containsKey(args[2].toLowerCase())){
						localBypass("", args[1]);
					} else {
						localBypass(args[2], args[1]);
					}
				}
			}
		} else if(orEqIC(arg, "commence", "start", "begin")){
			if(GlobalDataManager.state.isFirst() && sender instanceof Player && orEqIC(((Player) sender).getName(), "Erackron", "Kraeghnor")){
				processed = true;
				GlobalDataManager.advanceCheckpoint();
			}
		} else if(orEqIC(arg, "advance", "next", "nxt")){
			if(checkPermission("eCore.*", true) && !GlobalDataManager.state.isFirst()){
				if(!GlobalDataManager.advanceCheckpoint())
					sender.sendMessage(ChatColor.DARK_BLUE + "[eCore] " + ChatColor.WHITE + "The event needs to be started first");
			}
		} else if(orEqIC(arg, "stage", "state")){ 
			if(checkPermission("eCore.*", true))
				sender.sendMessage(ChatColor.DARK_BLUE + "[eCore] " + ChatColor.WHITE + "The current stage is: " + ChatColor.GRAY + GlobalDataManager.state + "(id: " + GlobalDataManager.state.getID() + ")" );
		} else if(eqIC(arg, "save")){
			if(!checkPermission("eCore.*", true))
				return;
			if(args.length<2){
				PlayerManager.saveAll();
				GlobalDataManager.save();
			} else {
				if(Bukkit.getPlayer(args[1])!=null)
					PlayerManager.getPlayer(Bukkit.getPlayer(args[1])).save();
			}
		} else if(eqIC(arg, "loglevel")){
			if(!checkPermission("eCore.*", true))
				return;
			if(args.length<2){
				sender.sendMessage(ChatColor.DARK_BLUE + "[eCore] " + ChatColor.WHITE + "The current Logging Level is: "+ ChatColor.GRAY +eLogger.getLogLevel());
			} else {
				try{
					int newLevel = Integer.parseInt(args[1]);
					GlobalDataManager.setLogLevel(newLevel);
					sender.sendMessage(ChatColor.DARK_BLUE + "[eCore] " + ChatColor.WHITE + "Set the Logging Level to: "+ ChatColor.GRAY +eLogger.getLogLevel());
				} catch (NumberFormatException e){
					sender.sendMessage("The supplied level is not an integer");
					return;
				}
			}
		}
	}
	
	public static void localBypass(String arg){
		if(sender instanceof Player)
			localBypass(arg, ((Player) sender).getName(), false);
		else
			checkPermission("", false);
	}
	public static void localBypass(String arg, String playerName){
		if(sender instanceof Player && ((Player) sender).getName().equalsIgnoreCase(playerName))
			localBypass(arg, playerName, false);
		else
			localBypass(arg, playerName, true);
	}
	public static void localBypass(String arg, String playerName, boolean adminPermNeeded){
		if(adminPermNeeded && !checkPermission("eCore.invBypass.admin", true))
			return;
		arg = arg.toLowerCase();
		
		eCorePlayer ePlayer = PlayerManager.getPlayer(playerName);
		if(ePlayer==null){
			sender.sendMessage(ChatColor.DARK_BLUE + "[eCore] " + ChatColor.WHITE + "The specified player ("+ChatColor.GRAY+playerName+ChatColor.WHITE+") doesn't exist.");
			return;
		}
		
		if(TOGGLES.containsKey(arg)){
			if(!adminPermNeeded && !checkPermission("eCore.invBypass.local.toggle", true))
				return;
			switch(TOGGLES.get(arg)){
				case 0:
					ePlayer.setLocalInvBypass(!ePlayer.hasInvBypass());
					break;
				case 1:
					ePlayer.setLocalInvBypass(true);
					break;
				case 2:
					ePlayer.setLocalInvBypass(false);
					break;
			}
			ePlayer.updateInv();
			sender.sendMessage(ChatColor.DARK_BLUE + "[eCore] " + ChatColor.WHITE + "Set the local invBypass of "+playerName+" to: " + (ePlayer.hasInvBypass()?"true":"false"));
			if(adminPermNeeded && ePlayer.getPlayer()!=null)
				ePlayer.getPlayer().sendMessage(ChatColor.DARK_BLUE + "[eCore] " + ChatColor.WHITE + "("+ChatColor.GRAY+sender.getName()+ChatColor.WHITE+") Set your invBypass to: " + (ePlayer.hasInvBypass()?"true":"false"));
		} else {
			if(!adminPermNeeded && !checkPermission("eCore.invBypass.local.use", true))
				return;
			sender.sendMessage(ChatColor.DARK_BLUE + "[eCore] " + ChatColor.WHITE + "The local invBypass of "+playerName+" currently is: " + (ePlayer.hasInvBypass()?"true":"false"));
		}
	}
	
	public static void globalBypass(String arg){
		arg = arg.toLowerCase();
		
		if(TOGGLES.containsKey(arg)){
			if(!checkPermission("eCore.invBypass.global.toggle", true))
				return;
			switch(TOGGLES.get(arg)){
				case 0:
					GlobalDataManager.invBypass = !GlobalDataManager.invBypass;
					break;
				case 1:
					GlobalDataManager.invBypass = true;
					break;
				case 2:
					GlobalDataManager.invBypass = false;
				break;
			}
			PlayerManager.updateAllInventories();
			sender.sendMessage(ChatColor.DARK_BLUE + "[eCore] " + ChatColor.WHITE + "Set the global invBypass to: " + (GlobalDataManager.invBypass?"true":"false"));
		} else {
			if(!checkPermission("eCore.invBypass.global.use", true))
				return;
			sender.sendMessage(ChatColor.DARK_BLUE + "[eCore] " + ChatColor.WHITE + "The global invBypass currently is: " + (GlobalDataManager.invBypass?"true":"false"));
		}
	}
	
	private static boolean eqIC(String arg1, String arg2){
		return arg1.equalsIgnoreCase(arg2);
	}
	
	private static boolean orEqIC(String arg1, String... args){
		for(String arg: args){
			if(arg1.equalsIgnoreCase(arg))
				return true;
		}
		return false;
	}
}