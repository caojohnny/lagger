package com.gmail.woodyc40.lagger.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;

/**
 * Clears the inventory and the armor of the user running
 * the command.
 *
 * <p>usage: /ci</p>
 */
public class ClearInventoryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender,
                             @Nonnull Command command,
                             @Nonnull String label,
                             @Nonnull String[] args) {
        if (!sender.hasPermission("lagger.ci")) {
            sender.sendMessage("No permission!");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player!");
            return true;
        }

        Player player = (Player) sender;
        PlayerInventory inv = player.getInventory();

        inv.clear();
        inv.setArmorContents(new ItemStack[4]);
        player.sendMessage("Cleared your inventory!");

        return true;
    }
}
