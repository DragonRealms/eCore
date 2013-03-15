package com.mcdr.ecore.listener;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import com.mcdr.ecore.eCore;
import com.mcdr.ecore.task.TaskManager;


public class eCorePlayerListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (player.getName().equalsIgnoreCase(eCore.name)){
			TaskManager.startFlameEffect();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (player.getName().equalsIgnoreCase(eCore.name)){
			TaskManager.stopFlameEffect();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onTeleport(PlayerTeleportEvent e){
		Player player = e.getPlayer();
		if (player.getName().equalsIgnoreCase(eCore.name)){
			World world = player.getWorld();
			Location loc = player.getLocation();
			for(int i = 0;i<100;i++)
				for(double j = 0.0; j <= 2.0; j+=0.25)
					world.playEffect(loc.clone().add(0.0, j, 0.0),Effect.SMOKE, i, 16);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
	public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent e){
		LivingEntity lEntity = e.getTarget();
		if(lEntity instanceof Player && ((Player) lEntity).getName().equalsIgnoreCase(eCore.name)){
			e.setTarget(null);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onFoodLevelChange(FoodLevelChangeEvent e){
		if(e.getEntity() instanceof Player && ((Player)e.getEntity()).getName().equalsIgnoreCase(eCore.name)){
			if(e.getFoodLevel()<20)
				e.setFoodLevel(20);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
	public void onEntityDamage(EntityDamageEvent e){
		if (e.getDamage() <= 0)
			return;
		Entity entity = e.getEntity();
		if(entity instanceof Player && ((Player) entity).getName().equalsIgnoreCase(eCore.name)){
			Player player = (Player) entity;
			if (e instanceof EntityDamageByEntityEvent) {
				Entity damager = ((EntityDamageByEntityEvent) e).getDamager();
				if (damager instanceof Projectile && !(((Projectile) damager).getShooter() instanceof Player))
					e.setCancelled(true);
				else if(!(damager instanceof Player))
					e.setCancelled(true);
			}
			
			if(player.getHealth() - e.getDamage() < 0)
				switch(eCore.deathType){
					default:
					case 0:
						player.setHealth(player.getMaxHealth());
						break;
					case 1:
						
						break;
					case 2:
						break;
				}
		}
	}
	
	/*@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent e){
		Player p = e.getEntity();
		PlayerInventory inv = p.getInventory();
		
	}*/
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e){
		
	}
	
	
}
