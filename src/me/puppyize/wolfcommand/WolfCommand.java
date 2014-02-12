package me.puppyize.wolfcommand;

import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
 * "/wolf stand" to stand.</br>While holding a stick, left clicking on a mob or
 * player will force all tamed wolves to attack. right clicking will cancel the attack.
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
				
				WolfPlayer wp = new WolfPlayer((Player) sender);
				switch (args[0].toLowerCase()) {
					case "sit":
						wp.sitWolves();
						return true;
					case "stand":
						wp.standWolves();
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
			
			WolfPlayer wp = new WolfPlayer(p); // Decorate Player object
			
			if (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK) {
				target = wp.getTarget();
			} else if (!(a == Action.RIGHT_CLICK_AIR  || a == Action.RIGHT_CLICK_BLOCK)) {
				return;
			}
			
			wp.setTarget(target);
		}
	}
	
	

}
