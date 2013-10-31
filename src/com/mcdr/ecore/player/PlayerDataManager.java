package com.mcdr.ecore.player;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.mcdr.ecore.eLogger;
import com.mcdr.ecore.config.Config;
import com.mcdr.ecore.util.legacy.GameModes;

public abstract class PlayerDataManager extends Config {
	
	public static OfflinePlayer[] getPlayersFromFiles(){
		File playersFolder = new File(DATAFOLDER+SEPERATOR+"Players");
		String[] playerFileNames = playersFolder.list(new FilenameFilter(){public boolean accept(File directory, String fileName){return fileName.endsWith(".dat");}});
		int amount = playerFileNames==null?0:playerFileNames.length;
		OfflinePlayer[] players = new OfflinePlayer[amount];
		for(int i = 0; i<amount; i++){
			players[i] = Bukkit.getOfflinePlayer(playerFileNames[i].replace(".dat", ""));
		}
		return players;
	}
	
	public static eCorePlayer loadPlayerData(OfflinePlayer player){
		eLogger.d("Loading "+player.getName()+"'s PlayerData");
		String playerName = player.getName().toLowerCase();
		YamlConfiguration yamlConfig = loadConfig(playerName + DATAEXTENSION, true);
		eCoreInventory eCoreInv = loadInventory(player, yamlConfig, true), normalInv = loadInventory(player, yamlConfig, false);

		if(!yamlConfig.isSet("EventInvEquipped"))
			yamlConfig.set("EventInvEquipped", false);
		if(!yamlConfig.isSet("invBypass"))
			yamlConfig.set("invBypass", false);
		if(!yamlConfig.isSet("Score"))
			yamlConfig.set("Score", 0);
		if(!yamlConfig.isSet("Name"))
			yamlConfig.set("Name", player.getName());
		
		boolean eventInv = yamlConfig.getBoolean("EventInvEquipped", false);
		boolean invBypass = yamlConfig.getBoolean("invBypass", false);
		int score = yamlConfig.getInt("Score", 0);
		GameMode nonEventMode = GameModes.getById(yamlConfig.getInt("NonEventGameMode", 0));
		
		eCorePlayer ePlayer = new eCorePlayer(player, eCoreInv, normalInv, eventInv, score, invBypass);
		
		ePlayer.setDiedInEventWorld(yamlConfig.getBoolean("DiedInEventWorld", false));
		ePlayer.setNonEventMode(nonEventMode);
		
		eLogger.d("Loaded "+player.getName()+"'s data");
		
		saveConfig(yamlConfig, playerName + DATAEXTENSION, true);
		return ePlayer;
	}
	
	public static void savePlayerData(eCorePlayer ePlayer){
		eLogger.d("Saving "+ePlayer.getName()+"'s PlayerData");
		String playerName = ePlayer.getName().toLowerCase();
		YamlConfiguration yamlConfig = loadConfig(playerName + DATAEXTENSION, true);
		
		yamlConfig.set("Name", ePlayer.getName());
		yamlConfig.set("EventInvEquipped", ePlayer.usesEventInv());
		yamlConfig.set("invBypass", ePlayer.hasLocalInvBypass());
		yamlConfig.set("Score", ePlayer.getScore());
		yamlConfig.set("DiedInEventWorld", ePlayer.hasDiedInEventWorld()?true:null);
		yamlConfig.set("NonEventGameMode", GameModes.getIdByName(ePlayer.getNonEventMode().toString()));
		
		saveInventory(yamlConfig, ePlayer, true);
		saveInventory(yamlConfig, ePlayer, false);
		
		saveConfig(yamlConfig, playerName + DATAEXTENSION, true);
	}
	
	private static eCoreInventory loadInventory(OfflinePlayer player, YamlConfiguration yamlConfig, boolean eventInv){
		String configPath = eventInv?"eCoreInv.":"normalInv.", tempPath;
		eCoreInventory inv; int amount; List<ItemStack> tempItems = new ArrayList<ItemStack>();
		Map<String, ItemStack[]> serialized = new HashMap<String, ItemStack[]>();
		if(!yamlConfig.isSet(configPath)){
			if(player.isOnline())
				inv = eventInv?eCoreInventory.createNewInventory(player):new eCoreInventory(player.getPlayer());
			else 
				inv = eCoreInventory.createNewInventory(player);
			saveInventory(yamlConfig, inv, eventInv);
		} else {
			for(String temp: new String[]{"contents", "armorContents"}){
				tempPath = configPath+temp+".";
				amount = yamlConfig.getInt(tempPath+"amount");
				tempItems.clear();
				for(int i = 0; i<amount; i++){
					tempItems.add(yamlConfig.getItemStack(tempPath+"slot"+i, new ItemStack(Material.AIR)));
				}
				serialized.put(temp, tempItems.toArray(new ItemStack[tempItems.size()]));
			}
			serialized.put(yamlConfig.getString("Name"), null);
			inv = eCoreInventory.deserialize(player, serialized);
		}
		return inv;
	}

	private static void saveInventory(YamlConfiguration yamlConfig, eCorePlayer ePlayer, boolean eventInv) {
		eCoreInventory inv = ePlayer.getInventory(eventInv);
		if(eventInv==ePlayer.usesEventInv())
			inv.update();
		saveInventory(yamlConfig, inv, eventInv);
	}
	
	private static void saveInventory(YamlConfiguration yamlConfig, eCoreInventory inv, boolean eventInv){
		String configPath = eventInv?"eCoreInv.":"normalInv.", tempPath;
		ItemStack[] items; int length;
		for(Entry<String, ItemStack[]> entry: inv.serialize().entrySet()){
			if(!(entry.getKey().equalsIgnoreCase("contents")||entry.getKey().equalsIgnoreCase("armorContents")))
				continue;
			
			tempPath = configPath+entry.getKey();
			
			items = entry.getValue();
			length = items.length;
			
			for(int i = 0; i<length; i++){
				yamlConfig.set(tempPath+".slot"+i, items[i]);
			}
			yamlConfig.set(tempPath+".amount", length);
		}
	}
	
}
