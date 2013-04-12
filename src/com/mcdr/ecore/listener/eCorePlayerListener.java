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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import com.mcdr.ecore.eCore;
import com.mcdr.ecore.event.eCoreEventManager;
import com.mcdr.ecore.event.eCoreEventManager.EventType;
import com.mcdr.ecore.player.PlayerManager;
import com.mcdr.ecore.player.eCorePlayer;
import com.mcdr.ecore.task.TaskManager;


public class eCorePlayerListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (player.getName().equalsIgnoreCase(eCore.name)){
			TaskManager.startFlameEffect();
			return;
		}
		PlayerManager.addPlayer(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (player.getName().equalsIgnoreCase(eCore.name)){
			TaskManager.stopFlameEffect();
			return;
		}
		eCorePlayer ePlayer = PlayerManager.getPlayer(player);
		ePlayer.updateInv();
		ePlayer.save();
		PlayerManager.removePlayer(player);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
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
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent e){
		Player player = e.getPlayer();
		if(player.getName().equalsIgnoreCase(eCore.name))
			return;
		eCorePlayer ePlayer = PlayerManager.getPlayer(player);
		if(ePlayer==null){
			PlayerManager.addPlayer(player);
			ePlayer = PlayerManager.getPlayer(player);
		}
		ePlayer.updateInv();
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent e){
		LivingEntity lEntity = e.getTarget();
		if(lEntity instanceof Player && ((Player) lEntity).getName().equalsIgnoreCase(eCore.name)){
			e.setTarget(null);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onFoodLevelChange(FoodLevelChangeEvent e){
		if(e.getEntity() instanceof Player && ((Player)e.getEntity()).getName().equalsIgnoreCase(eCore.name)){
			if(e.getFoodLevel()<20)
				e.setFoodLevel(20);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
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
			
			if(player.getHealth() - e.getDamage() <= 0){
				switch(eCore.deathType){
					default:
					case 0:
						player.setHealth(player.getMaxHealth());
						break;
					case 1:
						eCoreEventManager.runEvent(EventType.FIRSTRETREAT);
						break;
					case 2:
						eCoreEventManager.runEvent(EventType.FINALRETREAT);
						break;
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeath(PlayerDeathEvent e){
		Player p = e.getEntity();
		if(p.getName().equalsIgnoreCase(eCore.name))
			return;
		eCorePlayer ePlayer = PlayerManager.getPlayer(p);
		if(e.getEntity().getLocation().getWorld().getName().equalsIgnoreCase(eCore.worldName))
			ePlayer.onDeathInEventWorld(e);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e){
		Player p = e.getPlayer();
		if(p.getName().equalsIgnoreCase(eCore.name))
			return;
		eCorePlayer ePlayer = PlayerManager.getPlayer(p);
		if(ePlayer.hasTempSpawnBackup())
			ePlayer.onRespawn(e);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerItemBreak(PlayerItemBreakEvent e){
		updateCurInv(e.getPlayer());
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerItemConsume(PlayerItemConsumeEvent e){
		updateCurInv(e.getPlayer());
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerItemPickup(PlayerPickupItemEvent e){
		updateCurInv(e.getPlayer());
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerItemDrop(PlayerDropItemEvent e){
		updateCurInv(e.getPlayer());
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onInventoryClose(InventoryCloseEvent e){
		if(e.getInventory().getType()==InventoryType.PLAYER){
			updateCurInv(((Player) e.getInventory().getHolder()));
		} else if(e.getView().getBottomInventory().getType()==InventoryType.PLAYER){
			updateCurInv(((Player) e.getView().getBottomInventory().getHolder()));
		}
	}
	
	private void updateCurInv(Player p){
		if(p.getName().equalsIgnoreCase(eCore.name))
			return;
		PlayerManager.getPlayer(p).updateCurInv();
	}
}
