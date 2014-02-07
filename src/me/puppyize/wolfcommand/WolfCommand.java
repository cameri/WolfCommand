package me.puppyize.wolfcommand;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;

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
					for (Wolf w : getPlayerWolves((Player) sender)) {
						if (!w.isSitting()) {
							w.setSitting(true);
						}
					}
					return true;
				case "stand":
					for (Wolf w : getPlayerWolves((Player) sender)) {
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
		ItemStack itemInHand = p.getItemInHand(); 
		if (itemInHand.getType() == Material.STICK) {
			LivingEntity attackMe = this.getTarget(p, 40);
			ArrayList<Wolf> wolves = getPlayerWolves(p);
			if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) {
				
				if (attackMe == null || wolves.contains(attackMe)) {
					return;
				}
				for (Wolf w : getPlayerWolves(p)) {
					if (!w.isSitting()) {
						w.setTarget(attackMe);
					}
				}
			} else if ( e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
				for (Wolf w : getPlayerWolves(p)) {
					w.setTarget(null);
				}
			}
		}
	}

	private ArrayList<Wolf> getPlayerWolves(Player p) {
		ArrayList<Wolf> entities = new ArrayList<Wolf>();
		for (World w : Bukkit.getWorlds()) {
			for (Entity e : w.getEntitiesByClass(Wolf.class)) {
				if (((Tameable) e).isTamed()
						&& ((Tameable) e).getOwner().getName()
								.equals(p.getName())) {
					entities.add((Wolf) e);
				}
			}
		}
		return entities;
	}

	private LivingEntity getTarget(Player p, int range) {

		List<Entity> nearbyE = p.getNearbyEntities(range, range, range);
		ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();
		BlockIterator bI = new BlockIterator(p, range);
		Block b;
		Location loc;
		int bx, by, bz;
		double ex, ey, ez;

		for (Entity e : nearbyE) {
			if (e instanceof LivingEntity) {
				livingE.add((LivingEntity) e);
			}
		}

		while (bI.hasNext()) {
			b = bI.next();
			bx = b.getX();
			by = b.getY();
			bz = b.getZ();
			for (LivingEntity e : livingE) {
				loc = e.getLocation();
				ex = loc.getX();
				ey = loc.getY();
				ez = loc.getZ();
				if ((bx - .75 <= ex && ex <= bx + 1.75)
						&& (bz - .75 <= ez && ez <= bz + 1.75)
						&& (by - 1 <= ey && ey <= by + 2.5)) {
					return e;
				}
			}
		}
		return null;
	}

}
