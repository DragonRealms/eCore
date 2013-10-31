package com.mcdr.ecore.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mcdr.ecore.eLogger;

public class eCoreInventory{
	private OfflinePlayer owner;
	private ItemStack[] contents, armorContents;
	
	public eCoreInventory(Player owner) {
		this(owner, owner.getInventory().getContents(), owner.getInventory().getArmorContents());
	}
	
	public eCoreInventory(OfflinePlayer owner, ItemStack[] contents, ItemStack[] armorContents){
		this.owner = owner;
		this.contents = contents;
		this.armorContents = armorContents;
	}

	public ItemStack[] getContents() {
		return contents;
	}

	public ItemStack[] getArmorContents() {
		return armorContents;
	}
	
	public OfflinePlayer getOfflineOwner(){
		return owner;
	}
	
	public Player getOwner(){
		return owner.getPlayer();
	}
	
	public void setOwner(Player owner){
		this.owner = owner;
	}
	
	public void update(){
		if(!owner.isOnline()){
			eLogger.d("Tried to update "+owner.getName()+"'s inventory, but "+owner.getName()+" is offline");
			return;
		}
		Player owner = getOwner();
		if(!this.contents.equals(owner.getInventory().getContents()))
			this.contents = owner.getInventory().getContents();
		if(!this.armorContents.equals(owner.getInventory().getArmorContents()))
			this.armorContents = owner.getInventory().getArmorContents();
		eLogger.d("Updated "+owner.getName()+"'s inventory");
	}
	
	public static eCoreInventory createNewInventory(OfflinePlayer owner){
		ItemStack[] defaultContents = {}, defaultArmorContents = {};
		
		return new eCoreInventory(owner, defaultContents, defaultArmorContents);
	}
	
	/**
	 * Deserializes an eCoreInventory
	 * Use this when you don't have the player object of the owner, it will then be extracted from the serialized map.
	 * Note: This only works for online players
	 * @param Map<String, ItemStack[]> The serialized verion of an eCoreInventory obtainable by using {@link #serialize()}
	 * @return eCoreInventory the deserialized version of the eCoreInventory supplied
	 */
	public static eCoreInventory deserialize(Map<String, ItemStack[]> serialized){
		OfflinePlayer owner = null;
		for(Entry<String, ItemStack[]> entry: serialized.entrySet()){
			if(!(entry.getKey().equalsIgnoreCase("contents")||entry.getKey().equalsIgnoreCase("armorContents"))){
				owner = Bukkit.getOfflinePlayer(entry.getKey());
				break;
			}
		}
		return deserialize(owner, serialized);
	}
	
	/**
	 * Deserializes an eCoreInventory
	 * Use this when you do have the player object of the owner
	 * @param Map<String, ItemStack[]> The serialized verion of an eCoreInventory obtainable by using {@link #serialize()}
	 * @return eCoreInventory the deserialized version of the eCoreInventory supplied
	 */
	public static eCoreInventory deserialize(OfflinePlayer owner, Map<String, ItemStack[]> serialized){
		if(owner==null||!(serialized.containsKey("contents")&&serialized.containsKey("armorContents")))
			return null;		
		return new eCoreInventory(owner, serialized.get("contents"), serialized.get("armorContents"));
	}
	
	/**
	 * Serializes an eCoreInventory
	 * @return Map<String, ItemStack[]> The serialized version of this eCoreInventory
	 */
	public Map<String, ItemStack[]> serialize(){
		Map<String, ItemStack[]> inv = new HashMap<String, ItemStack[]>();
		inv.put("contents", contents);
		inv.put("armorContents", armorContents);
		inv.put(owner.getName(), null);
		return inv;
	}
}
