package com.mcdr.ecore.player;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.PlayerInventory;

import com.mcdr.ecore.GlobalDataManager;
import com.mcdr.ecore.eCore;

public class eCorePlayer {
	private OfflinePlayer player;
	private eCoreInventory eCoreInv, normalInv;
	private boolean eventInv = false, saveNeeded = false, invBypass = false;
	private int score;
	private Location tempSpawnBackup = null;

	public eCorePlayer(Player player){
		this(player, eCoreInventory.createNewInventory(player), new eCoreInventory(player), false, 0, false);
	}
	
	public eCorePlayer(OfflinePlayer player, eCoreInventory eCoreInv, eCoreInventory normalInv, boolean eventInv, int score, boolean invBypass) {
		setPlayer(player);
		this.eCoreInv = eCoreInv;
		this.normalInv = normalInv;
		this.eventInv = eventInv;
		this.invBypass = invBypass;
	}

	public Player getPlayer() {
		return player.getPlayer();
	}

	public void setPlayer(OfflinePlayer player) {
		this.player = player;
	}
	
	/**
	 * Update the inventory and switch if necessary
	 */
	public void updateInv(){
		if((getPlayer())!=null){
			if(isInEventWorld()){
				if(!eventInv){
					normalInv.update();
					if(!hasInvBypass()){
						normalInv.update();
						setInv(eCoreInv);
						eventInv = true;
					}
				} else {
					eCoreInv.update();
					if(hasInvBypass()){
						setInv(normalInv);
						eventInv = false;
					}
				}
			} else {
				if(eventInv) {
					eCoreInv.update();
					setInv(normalInv);
					eventInv = false;
				} else {
					normalInv.update();
				}
			}
			saveNeeded = true;
		}
	}
	
	/**
	 * Just update the currently equipped inventory
	 */
	public void updateCurInv(){
		getInventory(eventInv).update();
		saveNeeded = true;
	}
	
	private void setInv(eCoreInventory eInv){
		if(getPlayer()!=null)
			setInv(eInv, getPlayer().getInventory());
	}
	
	private void setInv(eCoreInventory eInv, PlayerInventory pInv){
		pInv.clear();
		pInv.setContents(eInv.getContents());
		pInv.setArmorContents(eInv.getArmorContents());
	}
	
	public void save(){
		PlayerDataManager.savePlayerData(this);
		saveNeeded = false;
	}
	
	public boolean isSaveNeeded(){
		return saveNeeded;
	}

	public String getName() {
		return player.getName();
	}
	
	public eCoreInventory getInventory(boolean eventInv){
		return eventInv?eCoreInv:normalInv;
	}
	
	public boolean usesEventInv(){
		return eventInv;
	}
	
	public boolean isInEventWorld(){
		return (getPlayer()!=null)?getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(eCore.worldName):false;
	}
	
	public int getScore(){
		return score;
	}
	
	public void addScore(int amount){
		if(amount==0)
			return;
		score += Math.abs(amount);
		saveNeeded = true;
	}
	
	public boolean hasInvBypass(){
		return (GlobalDataManager.invBypass && eCore.hasPermission(getPlayer(), "eCore.invBypass.global.use"))||this.invBypass;
	}
	
	public void setLocalInvBypass(boolean invBypass){
		if(this.invBypass==invBypass)
			return;
		this.invBypass = invBypass;
		saveNeeded = true;
	}
	
	public boolean hasLocalInvBypass(){
		return invBypass;
	}

	public void onDeathInEventWorld(PlayerDeathEvent e) {
		e.setKeepLevel(true);
		e.setDroppedExp(0);
		e.getDrops().clear();
		//updateCurInv();
		GlobalDataManager.state.setRespawn();
		tempSpawnBackup = e.getEntity().getBedSpawnLocation();
		e.getEntity().setBedSpawnLocation(GlobalDataManager.state.getRespawn(), true);
	}
	
	public void onRespawn(PlayerRespawnEvent e){
		Player p = e.getPlayer();
		p.setBedSpawnLocation(tempSpawnBackup, true);
		tempSpawnBackup = null;
		setInv(getInventory(this.eventInv));
	}

	public boolean hasTempSpawnBackup() {
		return tempSpawnBackup!=null;
	}
	
	public Location getTempSpawnBackup() {
		return tempSpawnBackup;
	}
	
	public void setTempSpawnBackup(Location tempSpawnBackup){
		this.tempSpawnBackup = tempSpawnBackup;
	}
}
