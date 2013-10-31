package com.mcdr.ecore.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.mcdr.ecore.eCore;
import com.mcdr.ecore.eLogger;

public abstract class PlayerManager {
	private static List<eCorePlayer> onlinePlayers = new ArrayList<eCorePlayer>(), offlinePlayers = new ArrayList<eCorePlayer>();
	
	public static void initializeArrays() {		
		Player[] players = Bukkit.getOnlinePlayers();
		for (Player player : players)
			addPlayer(player);
		
		OfflinePlayer[] offlinePlayers = PlayerDataManager.getPlayersFromFiles();
		for (OfflinePlayer player : offlinePlayers){
			if(!onlinePlayers.contains(player.getPlayer()))
				addPlayer(player);
		}
	}

	public static eCorePlayer addPlayer(Player player) {
		if(player.getName().equalsIgnoreCase(eCore.name))
			return null;
		
		for (eCorePlayer ePlayer : offlinePlayers) {
			if (ePlayer.getName().equals(player.getName())) {
				ePlayer.setPlayer(player);
				onlinePlayers.add(ePlayer);
				offlinePlayers.remove(ePlayer);
				ePlayer.updateInv();
				eLogger.d("Added "+player.getName()+" to onlinePlayers (from cache)");
				return ePlayer;
			}
		}
		
		eCorePlayer ePlayer = PlayerDataManager.loadPlayerData(player);
		onlinePlayers.add(ePlayer);
		ePlayer.updateInv();
		eLogger.d("Added "+player.getName()+" to onlinePlayers (from file)");
		return ePlayer;
	}
	
	public static eCorePlayer addPlayer(OfflinePlayer player){
		if(player.getName().equalsIgnoreCase(eCore.name))
			return null;
		
		eCorePlayer ePlayer = PlayerDataManager.loadPlayerData(player);
		offlinePlayers.add(ePlayer);
		eLogger.d("Added "+player.getName()+" to offlinePlayers");
		return ePlayer;
	}
		
	public static void removePlayer(Player player) {
		for (eCorePlayer ePlayer : onlinePlayers) {
			if (ePlayer.getName().equals(player.getName())) {
				onlinePlayers.remove(ePlayer);
				offlinePlayers.add(ePlayer);
				ePlayer.updateInv();
				eLogger.d("Moved "+player.getName()+" to offlinePlayers cache");
				return;
			}
		}
	}
	
	public static eCorePlayer getPlayer(Player player) {
		return getPlayer(player.getName());
	}
	
	public static eCorePlayer getPlayer(String playerName){
		for (eCorePlayer ePlayer : onlinePlayers) {
			if (ePlayer.getName().equalsIgnoreCase(playerName))
				return ePlayer;
		}
		for (eCorePlayer ePlayer : offlinePlayers){
			if (ePlayer.getName().equalsIgnoreCase(playerName))
				return ePlayer;
		}
		
		
		return null;
	}
	
	public static List<eCorePlayer> getOnlinePlayers() {
		return onlinePlayers;
	}
	
	public static List<eCorePlayer> getOfflinePlayers() {
		return offlinePlayers;
	}
	
	public static void saveAll(){
		for(eCorePlayer ePlayer: onlinePlayers){
			if(ePlayer.isSaveNeeded())
				ePlayer.save();
		}
		
		for(eCorePlayer ePlayer: offlinePlayers){
			if(ePlayer.isSaveNeeded())
				ePlayer.save();
		}
	}

	public static void updateAllInventories() {
		for(eCorePlayer ePlayer: onlinePlayers){
			ePlayer.updateInv();
		}
	}
}
