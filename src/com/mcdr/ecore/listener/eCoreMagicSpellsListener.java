package com.mcdr.ecore.listener;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.mcdr.ecore.eCore;
import com.nisovin.magicspells.events.SpellCastedEvent;

public class eCoreMagicSpellsListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onSpellCasted(SpellCastedEvent e){
		Player plyr = e.getCaster();
		String spellName = e.getSpell().toString().split("@")[0];
		
		if(spellName.equalsIgnoreCase("com.nisovin.magicspells.spells.targeted.spawnmonsterspell") && plyr.getName().equalsIgnoreCase(eCore.name)){
			List<Entity> entities = plyr.getNearbyEntities(16, 5, 16);
			for(Entity entity: entities){
				if(entity.getType()==EntityType.PIG_ZOMBIE)
					((PigZombie)entity).setAngry(true);
			}
		}
	}
}