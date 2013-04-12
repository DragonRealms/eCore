package com.mcdr.ecore.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mcdr.ecore.eCore;

public abstract class BaseCommand {
	public static boolean processed;
	public static CommandSender sender;
	public static String[] args;
	public static String label;
	
	protected static boolean checkPermission(String permission, boolean consoleUsage) {		
		if (!consoleUsage && !(sender instanceof Player)) {
			sender.sendMessage(ChatColor.DARK_BLUE + "[eCore]" + ChatColor.WHITE + " This command doesn't support console usage.");
			processed = true;
			return false;
		}
		
		processed = false;
		
		if (!eCore.hasPermission(sender, permission)) {
			sender.sendMessage(ChatColor.DARK_BLUE + "[eCore] " + ChatColor.WHITE + "You don't have the permission for this command.");
			sender.sendMessage(ChatColor.GRAY + permission + ChatColor.WHITE + " is needed.");
			return false;
		}
		
		processed = true;
		return true;
	}
}
