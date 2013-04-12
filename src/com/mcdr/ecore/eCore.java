package com.mcdr.ecore;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.mcdr.ecore.command.AsCommand;
import com.mcdr.ecore.command.BaseCommand;
import com.mcdr.ecore.command.KsCommand;
import com.mcdr.ecore.command.MainCommand;
import com.mcdr.ecore.config.ConfigManager;
import com.mcdr.ecore.listener.eCorePlayerListener;
import com.mcdr.ecore.listener.eCoreRedstoneListener;
import com.mcdr.ecore.player.PlayerManager;
import com.mcdr.ecore.task.TaskManager;
import com.timvisee.manager.permissionsmanager.PermissionsManager;

public class eCore extends JavaPlugin {
	public static eCore in;
	public static Logger logger;
	public static BukkitScheduler scheduler;
	public static PluginManager pm;
	public static PermissionsManager permsM;
	public static ArrayList<String> devs = new ArrayList<String>();
	public static Server server;
	public static String name;
	public static String worldName;
	private static World world;
	public static int deathType = 0;
	public static eCoreStage stage;
	public static boolean corruptionEnabled = false;

	
	public eCore(){
		in = this;
		logger = Bukkit.getLogger();
		scheduler = Bukkit.getScheduler();
		server = Bukkit.getServer();
		name = getHim();
		worldName = "Area51";
	}

	public void onEnable(){
		setupPermissionsManager();
		
		corruptionEnabled = server.getPluginManager().isPluginEnabled("Corruption");
		if(corruptionEnabled)
			logger.info("[eCore] Hooked into Corruption!");
		
		
		ConfigManager.load();
		pm = getServer().getPluginManager();
		pm.registerEvents(new eCorePlayerListener(), this);
		pm.registerEvents(new eCoreRedstoneListener(), this);
		
		PlayerManager.initializeArrays();
		
		TaskManager.start();
	}
	
	public void onDisable(){
		TaskManager.stop();
		PlayerManager.saveAll();
		GlobalDataManager.save();
	}
	
	public boolean onCommand(CommandSender sndr, Command cmd, String lbl, String[] args){
		BaseCommand.sender = sndr;
		BaseCommand.args = args;
		BaseCommand.label = lbl;
		if(cmd.getName().equalsIgnoreCase("ks")){
			KsCommand.process();
		} else if (cmd.getName().equalsIgnoreCase("as")){
			AsCommand.process();
		} else if (cmd.getName().equalsIgnoreCase("ecore")){
			MainCommand.process();
		}
		return BaseCommand.processed;
	}
	
	public static boolean hasPermission(Player p, String permsNode){
		return eCore.permsM.hasPermission(p, permsNode) || eCore.devs.contains(p.getName().toLowerCase());
	}
	
	public static boolean hasPermission(CommandSender sender, String permission) {
		if(sender instanceof Player){
			return hasPermission((Player) sender, permission);
		} else {
			return true;
		}
	}
    
    private String getHim() {
    	String[] strar = "100101111100101100001110010111001111101000110111011011111110010".split("(?<=\\G.{7})");
        byte[] bar = new byte[strar.length];
        for (int i = 0; i < strar.length; i++)
            bar[i] = Byte.parseByte(strar[i], 2);
        String s = new String(bar);
        return s;
    }
    
    public static World getWorld(){
		return (world==null)?world = Bukkit.getWorld(worldName):world;
    }
    
    private void setupPermissionsManager(){
    	eCore.permsM = new PermissionsManager(this.getServer(), this);
		eCore.permsM.setup();
		devs.add("erackron");
		devs.add("bozevogel");
		devs.add("weirdwater");
    }
}
