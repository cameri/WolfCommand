package me.puppyize.wolfcommand;

import java.util.*;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * <p>
 * This Minecraft plugin allows a player advanced control over their tamed
 * wolves.
 * </p>
 * <p>
 * Typing in "/wolf sit" will force all personally tamed wolves to sit;
 * "/wolf stand" to stand.</br>While holding a stick, clicking on a mob or
 * player will force all tamed wolves to attack.
 * </p>
 * 
 * @author Puppy Firelyte <mc@puppyize.me>
 */
public final class WolfCommand extends JavaPlugin implements Listener {
	final Double ATTACK_RANGE = 40D;
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	public void onDisable() {
		HandlerList.unregisterAll();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("wolf")) {
			if (args.length != 1)
				return false;
			if (sender instanceof Player) {
				switch (args[0].toLowerCase()) {
				case "sit":
					for (Wolf w : getPlayerWolves((Player) sender, ATTACK_RANGE)) {
						if (!w.isSitting()) {
							w.setSitting(true);
						}
					}
					return true;
				case "stand":
					for (Wolf w : getPlayerWolves((Player) sender, ATTACK_RANGE)) {
						if (w.isSitting()) {
							w.setSitting(false);
						}
					}
					return true;
				}
			} else {
				sender.sendMessage("Sender must be a Player");
			}
		}

		return false;
	}

	@EventHandler
	public void attackDistantCreature(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand().getType() == Material.STICK) {
			LivingEntity target = null;
			Action a = e.getAction();
			
			if (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK) {
				target = this.getTarget(p, ATTACK_RANGE);
			} else if (!(a == Action.RIGHT_CLICK_AIR  || a == Action.RIGHT_CLICK_BLOCK)) {
				return;
			}
				
			for (Wolf w : getPlayerWolves(p, ATTACK_RANGE)) {
				if (!w.isSitting()) {
					w.setTarget(target);
				}
			}
		}
	}
	
	private List<Wolf> getPlayerWolves(Player p, Double range) {
		List<Wolf> entities = new ArrayList<Wolf>();
		for (Entity e : p.getNearbyEntities(range, range, range)) {
			if (e instanceof Wolf && e instanceof Tameable) {
				Tameable t = (Tameable) e;
				if (t.isTamed() && t.getOwner() == p) {
					entities.add((Wolf) e);
				}
			}
		}
		return entities;
	}

	private LivingEntity getTarget(Player observer, Double range) {
		
		Location observerPos = observer.getEyeLocation();
        Vector3D observerDir = new Vector3D(observerPos.getDirection());

        Vector3D observerStart = new Vector3D(observerPos);
        Vector3D observerEnd = observerStart.add(observerDir.multiply(range));

        Entity targetEntity = null;

        // Loop through nearby entities (may be slow if too many around)
        for (Entity entity : observer.getNearbyEntities(range, range, range)) {
        	// Skip not living entities
        	if (!(entity instanceof LivingEntity)) {
        		continue;
        	}

        	// Skip our own wolves
        	if (entity instanceof Tameable && entity instanceof Wolf) {
        		Tameable t = (Tameable) entity;
        		if (t.isTamed() && t.getOwner() == observer) {
        			continue;
        		}
        	}

        	// We can't attack what we can't see
        	if (!observer.hasLineOfSight(entity)) {
        		continue;
        	}

        	// Bukkit API does not export an axis-aligned bounding box, so we'll settle for this
        	// Bounding box is set to 1 block in width and 1.67 blocks high from entity's center
        	Vector3D targetPos = new Vector3D(entity.getLocation());
            Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
            Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);

            if (entity != observer && Vector3D.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
            	// Get closest living entity on vector
                if (targetEntity == null ||
                	targetEntity.getLocation().distanceSquared(observerPos) > entity.getLocation().distanceSquared(observerPos)) {
                    targetEntity = entity;
                }
            }
        }
        return (LivingEntity) targetEntity;
	}

}
