package com.mcdr.ecore.listener;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import com.mcdr.ecore.eCore;

public class eCoreChunkListener implements Listener {
	private Boolean worldLoaded = false;
	private int[][] coords;
	private ArrayList<Chunk> chunks = new ArrayList<Chunk>();
	
	public eCoreChunkListener(int[][] chunkC){
		coords = chunkC;
		/*int length = chunkC.length;
		for(int i = 0 ; i < length ; i++){
			chunks.add(eCore.server.getWorld("Area51").getChunkAt(chunkC[i][0], chunkC[i][1]));
		}*/
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onChunkUnload(ChunkUnloadEvent e){
		if(worldLoaded && e.getWorld().getName().equalsIgnoreCase(eCore.worldname))
			if(chunks.contains(e.getChunk()))
				e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onWorldLoad(WorldLoadEvent e){
		if(e.getWorld().getName().equalsIgnoreCase(eCore.worldname)){
			worldLoaded = true;
			World w = e.getWorld();
			getChunks(w);
			loadChunks(w);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onWorldUnload(WorldUnloadEvent e){
		if(e.getWorld().getName().equalsIgnoreCase(eCore.worldname)){
			worldLoaded = false;
			chunks.clear();
		}
	}

	
	private void getChunks(World w){
		for(int[] coord : coords){
			chunks.add(w.getChunkAt(coord[0],coord[1]));
		}
	}
	
	private void loadChunks(World w){
		for(Chunk chunk: chunks)
			w.loadChunk(chunk);
	}
	
}
