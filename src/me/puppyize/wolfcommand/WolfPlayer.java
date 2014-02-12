package me.puppyize.wolfcommand;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;

/**
 * <p>
 * This Minecraft plugin allows a player advanced control over their tamed
 * wolves.
 * </p>
 * 
 * @author Puppy Firelyte <mc@puppyize.me>
 */
public class WolfPlayer {
	final Double ATTACK_RANGE = 40D;
	
	/**
	 * Keeps the current player
	 */
	private Player player;
	
	/**
	 * Creates a WolfPlayer to decorate a Player object
	 * @param player
	 */
	public WolfPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * Commands player's wolves to sit down
	 * @return
	 */
	public int sitWolves() {
		int count = 0;
		for (Wolf w : this.getWolves()) {
			if (!w.isSitting()) {
				w.setSitting(true);
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Commands player's wolves to stand up
	 * @return
	 */
	public int standWolves() {
		int count = 0;
		for (Wolf w : this.getWolves()) {
			if (w.isSitting()) {
				w.setSitting(false);
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Gets player's tamed wolves
	 * @return
	 */
	public List<Wolf> getWolves() {
		List<Wolf> entities = new ArrayList<Wolf>();
		for (Entity e : player.getNearbyEntities(ATTACK_RANGE, ATTACK_RANGE, ATTACK_RANGE)) {
			if (e instanceof Wolf && e instanceof Tameable) {
				Tameable t = (Tameable) e;
				if (t.isTamed() && t.getOwner() == this.player) {
					entities.add((Wolf) e);
				}
			}
		}
		return entities;
	}
	
	/**
	 * Sets player's wolves target
	 * @param target 
	 */
	public void setTarget(LivingEntity target) {
		for (Wolf w : this.getWolves()) {
			if (!w.isSitting()) {
				w.setTarget(target);
			}
		}
	}
	
	/**
	 * Gets the closest LivingEntity in player's crosshair
	 * @return target
	 */
	public LivingEntity getTarget() {
		Location observerPos = this.player.getEyeLocation();
        Vector3D observerDir = new Vector3D(observerPos.getDirection());
        Vector3D observerStart = new Vector3D(observerPos);
        Vector3D observerEnd = observerStart.add(observerDir.multiply(ATTACK_RANGE));
        Entity targetEntity = null;

        // Loop through nearby entities (may be slow if too many around)
        for (Entity entity : this.player.getNearbyEntities(ATTACK_RANGE, ATTACK_RANGE, ATTACK_RANGE)) {
        	// Skip not living entities
        	if (!(entity instanceof LivingEntity)) {
        		continue;
        	}

        	// Skip our own wolves
        	if (entity instanceof Tameable && entity instanceof Wolf) {
        		Tameable t = (Tameable) entity;
        		if (t.isTamed() && t.getOwner() == this.player) {
        			continue;
        		}
        	}

        	// We can't attack what we can't see
        	if (!this.player.hasLineOfSight(entity)) {
        		continue;
        	}

        	// Bukkit API does not export an axis-aligned bounding box, so we'll settle for this
        	// Bounding box is set to 1 block in width and 1.67 blocks high from entity's center
        	Vector3D targetPos = new Vector3D(entity.getLocation());
            Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
            Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);

            if (entity != this.player && Vector3D.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
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
