package com.mcdr.ecore;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public enum eCoreStage {
	INACTIVE,
	ARMORY(-18.0, 60.0, -24.0, -90f),
	HEADQUARTERS(-7.5, 59.0, 62.5, 180f),
	COOKIE(-41.5, 59.0, -53.0, 90f),
	DARKNESS(-43.5, 31.0, -53.5, -90f),
	PROUDOFSUBJECT(2.5, 31.0, -68.5, 180f),
	SURVIVOR(-18.5, 32.0, -95.5, 90f),
	LAVAEVERYWHERE(-21.5, 32.0, -89.0, 180f),
	MOBSEVERYWHERE(-36.0, 45.0, -22.5, 0f),
	NUKED(-75.5, 39.0, -41.0, 90f),
	GOOGLEGLASS(-86.0, 33.0, 60.5, -90f),
	CHEMICALS(81.5, 45.0, 80.5, 0f),
	SPIRALING(-62.5, 19.0, 104.5, 0f),
	PARADOXALHALLWAY(-85.5, 19.0, 200.5, 90f),
	BRIDGEJUMPING(-109.5, 20.0, 172.5, 180f),
	TELEPORTER(-100.5, 19.82, 148.5, 180f),
	CAVETROLL(-167.5, 29.0, 105.5, 180f),
	WITHTHEFISHES(-211.0, 33.0, 46.5, 180f),
	COOPERATION(-211.0, 27.0, -8.5, 180f),
	HUMONGOUS(-218.5, 27.0, 6.5, 0f),
	LIBRARIAN(-162.5, 28.0, 23.5, -90f),
	CHOICES1(-115.5, 28.0, 23.5, -90f),
	CHOICES2(-115.5, 28.0, 23.5, 0f),
	CHOICES3(-115.5, 28.0, 23.5, 180f),
	DEJAVU(-91.0, 39.0, 39.5, 0f),
	NUKETHERABBITHOLE(-374.5, 65.0, 56.5, 90f),
	DOWNTHEDRAIN(-507.0, 54.0, 60.5, 90f),
	FINISHED;
	
	
	private final double x, y, z;
	private final float rot;
	private Location respawnLocation;
	private static final Map<String, eCoreStage> NAME_MAP = new HashMap<String, eCoreStage>();
	
	eCoreStage(){
		x = y = z = 0.0;
		rot = 0f;
		respawnLocation = null;
	}
	
	eCoreStage(double x, double y, double z, float rot){
		this.x = x;
		this.y = y;
		this.z = z;
		this.rot = rot;
		setRespawn();
	}
	
	public void setRespawn(){
		if(this!=eCoreStage.INACTIVE)
			respawnLocation = eCore.getWorld()==null?null:new Location(eCore.getWorld(), x, y, z, rot, 0f);
		else
			respawnLocation = null;
	}
	
	public Location getRespawn(){
		return respawnLocation;
	}
	
	static{
		for(eCoreStage eCoreStage: values())
			NAME_MAP.put(eCoreStage.toString().toLowerCase(), eCoreStage);
	}
	
	public static eCoreStage fromString(String eCoreStage) {
		if(eCoreStage==null)
			return null;
		return NAME_MAP.get(eCoreStage.toLowerCase());
	}
	
}
