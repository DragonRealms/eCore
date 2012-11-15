package com.mcdr.ecore;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.mcdr.ecore.listener.eCoreCreativeSignListener;
import com.mcdr.ecore.listener.eCorePlayerListener;
import com.mcdr.ecore.task.TaskManager;

public class eCore extends JavaPlugin {
	public static eCore instance;
	public static Logger logger;
	public static BukkitScheduler scheduler;
	public static PluginManager pm;
	public static Server server;
	
	private eCoreCreativeSignListener csl;

	
	public eCore(){
		instance = this;
		logger = Bukkit.getLogger();
		scheduler = Bukkit.getScheduler();
		server = Bukkit.getServer();
	}

	public void onEnable(){
		csl = new eCoreCreativeSignListener(this);
		pm = getServer().getPluginManager();
		pm.registerEvents(new eCorePlayerListener(), this);
		pm.registerEvents(csl, this);
		TaskManager.start();
	}
	
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sndr, Command cmd, String lbl, String[] args){
		if(cmd.getName().equalsIgnoreCase("kr")){
			if(sndr instanceof Player){
				if(args.length != 1){
					return false;
				}
				
				if(args[0].equalsIgnoreCase("csign")){
					
				}
			}
		}
		return true;
	}

}
