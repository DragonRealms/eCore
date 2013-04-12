package com.mcdr.ecore.command;

import org.bukkit.ChatColor;

import com.mcdr.ecore.eCore;

public abstract class KsCommand extends BaseCommand{

	public static void process(){
		if(!checkPermission("eCore.ks", true))
			return;
		
		String output = "&8"+eCore.name+"&4";
		if(args.length < 1){
			processed = false;
			return;
		}
		for(String arg: args)
			output += " "+arg;
		output = ChatColor.translateAlternateColorCodes('&', output);
		eCore.server.broadcastMessage(output);
	}

}
