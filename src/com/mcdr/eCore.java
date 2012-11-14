package com.mcdr;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcdr.listener.CreativeSignListener;

public class eCore extends JavaPlugin {

	public void onEnable(){
		CreativeSignListener csl = new CreativeSignListener(this);
		
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(csl, this);
	}
	
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sndr, Command cmd, String lbl, String[] args){
		if(cmd.equals("kraeghnor")){
			if(sndr instanceof Player){
				if(args.length != 1){
					return false;
				}
				
				if(args[0] == "creativesign"){
					
				}
			}
		}
		return true;		
	}

}
