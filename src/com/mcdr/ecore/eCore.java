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

import com.mcdr.ecore.config.ConfigManager;
import com.mcdr.ecore.listener.eCorePlayerListener;
import com.mcdr.ecore.listener.eCoreRedstoneListener;
import com.mcdr.ecore.task.TaskManager;

public class eCore extends JavaPlugin {
	public static eCore instance;
	public static Logger logger;
	public static BukkitScheduler scheduler;
	public static PluginManager pm;
	public static Server server;
<<<<<<< HEAD
	public static String name = "Kraeghnor";
	
	private eCoreButtonListener csl;
=======
	public static String name;
>>>>>>> origin/SignChange

	
	public eCore(){
		instance = this;
		logger = Bukkit.getLogger();
		scheduler = Bukkit.getScheduler();
		server = Bukkit.getServer();
		name = getHim();
	}

	public void onEnable(){
		ConfigManager.Load();
		pm = getServer().getPluginManager();
		pm.registerEvents(new eCorePlayerListener(), this);
		pm.registerEvents(new eCoreRedstoneListener(), this);
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
				
				
			}
		}
		return true;
	}
    
    private String getHim() {
    	String[] strar = "100101111100101100001110010111001111101000110111011011111110010".split("(?<=\\G.{7})");
        byte[] bar = new byte[strar.length];
        for (int i = 0; i < strar.length; i++)
            bar[i] = Byte.parseByte(strar[i], 2);
        String s = new String(bar);
        return s;
    }
}
