package com.gmail.woodyc40.lagger.cmd;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Modifies the hunger or health of a player.
 *
 * <p>usage: /hurt [hunger] [target] &lt;amount&gt;</p>
 */
public class HurtCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender,
                             @Nonnull Command command,
                             @Nonnull String label,
                             @Nonnull String[] args) {
        if (!sender.hasPermission("lagger.hurt")) {
            sender.sendMessage("No permission!");
            return true;
        }

        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You are not a player!");
                return true;
            }

            Player target = (Player) sender;
            try {
                double amount = Double.parseDouble(args[0]);
                setHealth(target, target.getHealth() - amount);
                sender.sendMessage(format("Your health is now: %f", target.getHealth()));
            } catch (NumberFormatException e) {
                sender.sendMessage(format("Not a number: '%s'", args[0]));
            }

            return true;
        }

        if (args.length == 2) {
            if ("hunger".equalsIgnoreCase(args[0])) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("You are not a player!");
                    return true;
                }

                Player target = (Player) sender;
                try {
                    int amount = Integer.parseInt(args[1]);
                    setHunger(target, target.getFoodLevel() - amount);
                    sender.sendMessage(format("Your hunger is now: %d", target.getFoodLevel()));
                } catch (NumberFormatException e) {
                    sender.sendMessage(format("Not a number: '%s'", args[1]));
                }

                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(format("Player '%s' is not online!", args[0]));
                return true;
            }

            try {
                double amount = Double.parseDouble(args[1]);
                setHealth(target, target.getHealth() - amount);
                sender.sendMessage(format("Player %s's health is now: %f", target.getName(), target.getHealth()));
            } catch (NumberFormatException e) {
                sender.sendMessage(format("Not a number: '%s'", args[1]));
            }

            return true;
        }

        if (args.length == 3) {
            if ("hunger".equalsIgnoreCase(args[0])) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(format("Player '%s' is not online!", args[1]));
                    return true;
                }

                try {
                    int amount = Integer.parseInt(args[2]);
                    setHunger(target, target.getFoodLevel() - amount);
                    sender.sendMessage(format("Player %s's hunger is now: %d", target.getName(), target.getFoodLevel()));
                } catch (NumberFormatException e) {
                    sender.sendMessage(format("Not a number: '%s'", args[2]));
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Sets the player's health to the given amount,
     * clamping the health value by the player's maximum
     * health level.
     *
     * @param player    the player to set the health
     * @param newHealth the new health value to set
     */
    private static void setHealth(Player player, double newHealth) {
        if (newHealth < 0) {
            newHealth = 0;
        }

        AttributeInstance maxHealthAttr =
                requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH));
        double maxHealth = maxHealthAttr.getValue();
        if (newHealth > maxHealth) {
            newHealth = maxHealth;
        }

        player.setHealth(newHealth);
    }

    /**
     * Sets the player's hunger level to the given amount,
     * clamping the hunger value to within the valid range
     * of hunger values.
     *
     * @param player    the player to set the hunger level
     * @param newHunger the new hunger level
     */
    private static void setHunger(Player player, int newHunger) {
        if (newHunger < 0) {
            newHunger = 0;
        }

        if (newHunger > 20) {
            newHunger = 20;
        }

        player.setFoodLevel(newHunger);
    }
}
