package com.mcdr.ecore;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public enum eCoreStage {
	INACTIVE(0),
	ARMORY(-18.0, 60.0, -24.0, -90f, 1),
	HEADQUARTERS(-7.5, 59.0, 62.5, 180f, 2),
	COOKIE(-41.5, 59.0, -53.0, 90f, 3),
	DARKNESS(-43.5, 31.0, -53.5, -90f, 4),
	PROUDOFSUBJECT(2.5, 31.0, -68.5, 180f, 5),
	SURVIVOR(-18.5, 32.0, -95.5, 90f, 6),
	LAVAEVERYWHERE(-21.5, 32.0, -89.0, 180f, 7),
	MOBSEVERYWHERE(-36.0, 45.0, -22.5, 0f, 8),
	NUKED(-75.5, 39.0, -41.0, 90f, 9),
	GOOGLEGLASS(-86.0, 33.0, 60.5, -90f, 10),
	CHEMICALS(81.5, 45.0, 80.5, 0f, 11),
	SPIRALING(-62.5, 19.0, 104.5, 0f, 12),
	PARADOXALHALLWAY(-85.5, 19.0, 200.5, 90f, 13),
	BRIDGEJUMPING(-109.5, 20.0, 172.5, 180f, 14),
	TELEPORTER(-100.5, 19.82, 148.5, 180f, 15),
	CAVETROLL(-167.5, 32.0, 105.5, 180f, 16),
	WITHTHEFISHES(-211.0, 33.0, 46.5, 180f, 17),
	COOPERATION(-211.0, 27.0, -8.5, 180f, 18),
	HUMONGOUS(-218.5, 27.0, 6.5, 0f, 19),
	LIBRARIAN(-162.5, 28.0, 23.5, -90f, 20),
	CHOICES1(-115.5, 28.0, 23.5, -90f, 21),
	CHOICES2(-115.5, 28.0, 23.5, 0f, 22),
	CHOICES3(-115.5, 28.0, 23.5, 180f, 23),
	DEJAVU(-91.0, 39.0, 39.5, 0f, 24),
	NUKETHERABBITHOLE(-374.5, 65.0, 56.5, 90f, 25),
	DOWNTHEDRAIN(-507.0, 54.0, 60.5, 90f, 26),
	FINISHED(27);
	
	
	private final double x, y, z;
	private final int checkpointID;
	private final float rot;
	private Location respawnLocation;
	private static final Map<String, eCoreStage> NAME_MAP = new HashMap<String, eCoreStage>();
	
	eCoreStage(int checkpointID){
		this(0.0, 0.0, 0.0, 0f, checkpointID);
	}
	
	eCoreStage(double x, double y, double z, float rot, int checkpointID){
		this.checkpointID = checkpointID;
		this.x = x;
		this.y = y;
		this.z = z;
		this.rot = rot;
	}
	
	public void setRespawn(){
		if(checkpointID>0 && checkpointID<values().length-1)
			respawnLocation = eCore.getWorld()==null?null:new Location(eCore.getWorld(), x, y, z, rot, 0f);
		else
			respawnLocation = eCore.getWorld()==null?null:eCore.getWorld().getSpawnLocation();
	}
	
	public Location getRespawn(){
		return respawnLocation;
	}
	
	public int getID(){
		return checkpointID;
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
	
	public static eCoreStage fromID(int id){
		if(id<0||id>=values().length)
			return null;
		return values()[id];
	}
	
	
	public boolean isLast(){
		return getID()+1==values().length;
	}
	
	public boolean isFirst(){
		return getID()==0;
	}
	
	public eCoreStage next() {
		return next(getID());		
	}
	
	public static eCoreStage next(int curID) {
		int ln = values().length;
		if(curID >= 0 && curID+1 < ln){
			return fromID(curID+1);
		} else if(curID<0){
			return fromID(0);
		} else {
			return fromID(ln-1);
		}
	}
	
}
