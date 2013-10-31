package com.mcdr.ecore.util.legacy;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;

public enum GameModes {
	SURVIVAL(0),
	CREATIVE(1),
	ADVENTURE(2);

	
	private final int id;
	private static final Map<Integer, GameModes> BY_ID = new HashMap<Integer, GameModes>();
	private static final Map<String, GameModes> BY_NAME = new HashMap<String, GameModes>();
	
	
	static{
		for(GameModes gm: GameModes.values()){
			BY_ID.put(gm.getId(), gm);
			BY_NAME.put(gm.toString().toLowerCase(), gm);
		}
	}
	
	GameModes(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public static int getIdByName(String mode){
		return BY_NAME.get(mode.toLowerCase()).getId();
	}
	
	public static String getNameById(int id){
		return BY_ID.get(id).toString();
	}
	
	public static GameMode getById(int id){
		return GameMode.valueOf(getNameById(id));
	}
}
