package com.mcdr.ecore.listener;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.mcdr.ecore.GlobalDataManager;
import com.mcdr.ecore.eCore;
import com.mcdr.ecore.eLogger;
import com.mcdr.ecore.player.PlayerManager;
import com.mcdr.ecore.player.eCorePlayer;
import com.mcdr.ecore.task.TaskManager;


public class eCorePlayerListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (player.getName().equalsIgnoreCase(eCore.name)){
			TaskManager.startFlameEffect();
			player.setMaxHealth(80);
			player.setHealth(player.getMaxHealth());
			return;
		}
		eCorePlayer ePlayer = PlayerManager.addPlayer(player);
		if(ePlayer.isInEventWorld()&&!ePlayer.hasInvBypass()){
			ePlayer.getPlayer().setGameMode(GameMode.ADVENTURE);
			player.sendMessage(ChatColor.DARK_BLUE + "[eCore] " + ChatColor.WHITE + "Welcome back!\nYou'll be teleported to the current checkpoint.");
			player.teleport(GlobalDataManager.state.getRespawn());
		}
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
		if(e.getCause()==TeleportCause.UNKNOWN)
			return;
		Player player = e.getPlayer();
		if (player.getName().equalsIgnoreCase(eCore.name)){
			World world = player.getWorld();
			Location loc = player.getLocation();
			for(int i = 0;i<100;i++)
				for(double j = 0.0; j <= 2.0; j+=0.25)
					world.playEffect(loc.clone().add(0.0, j, 0.0),Effect.SMOKE, i, 16);
			world.playSound(e.getFrom(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
			//world.playSound(e.getTo(), Sound.ENDERMAN_TELEPORT, 1f, 0f);
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
		if(entity instanceof Player){
			Player player = (Player) entity;
			if(((Player) entity).getName().equalsIgnoreCase(eCore.name)){
				Entity damager = null;
				if (e instanceof EntityDamageByEntityEvent) {
					damager = ((EntityDamageByEntityEvent) e).getDamager();
					if (damager instanceof Projectile && !(((Projectile) damager).getShooter() instanceof Player)){
						e.setCancelled(true);
					} else if(damager instanceof Monster||damager instanceof Wolf)
						e.setCancelled(true);
				}
				if(e.isCancelled())
					return;
				
				switch(GlobalDataManager.state.getID()){
					case 0:
					case 27:
						e.setDamage(0);
						player.setHealth(player.getMaxHealth());
						break;
					case 14:
					case 26:
						if((e.getDamage()/4)>=player.getHealth()){
							e.setDamage(player.getHealth()-2);
							GlobalDataManager.advanceCheckpoint();
						} else {
							e.setDamage(((e.getDamage()/4)>0)?e.getDamage()/4:0.25);
						}
						break;
					default:
						if(damager!=null && !e.getCause().equals(DamageCause.THORNS)){
							if(damager instanceof Player){
								((Player) damager).sendMessage(ChatColor.DARK_RED+"You failed to penetrate "+ChatColor.DARK_GRAY+"Kraeghnor's"+ChatColor.DARK_RED+" aura.");
								eLogger.d(((Player) damager).getName() + " | " + e.getCause() + " | " + player.getName());
							} else if(damager instanceof Projectile && (((Projectile) damager).getShooter() instanceof Player)){
								((Player)((Projectile) damager).getShooter()).sendMessage(ChatColor.DARK_RED+"You failed to penetrate "+ChatColor.DARK_GRAY+"Kraeghnor's"+ChatColor.DARK_RED+" aura.");
								((Projectile) damager).setBounce(false);
							}
						}
						e.setCancelled(true);
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
		if(ePlayer.hasDiedInEventWorld())
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
	
	@EventHandler
	public void onGameModeChange(PlayerGameModeChangeEvent e){
		Player p = e.getPlayer();
		
		if(p.getName().equalsIgnoreCase(eCore.name))
			return;
		eCorePlayer ePlayer = PlayerManager.getPlayer(p);
		if(!ePlayer.isInEventWorld())
			ePlayer.setNonEventMode(p.getGameMode());
	}
	
	private void updateCurInv(Player p){
		if(p.getName().equalsIgnoreCase(eCore.name) || p.getHealth() <= 0)
			return;
		PlayerManager.getPlayer(p).updateCurInv();
	}
}
