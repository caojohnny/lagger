package com.gmail.woodyc40.lagger.cmd;

import com.gmail.woodyc40.lagger.SetSlotCompat;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import javax.inject.Inject;

import static java.lang.String.format;

/**
 * This is a test command that allows players to set their
 * hotbar slot to a certain item while retaining the item
 * "bounce" animation.
 *
 * <p>usage: /setslot <-2 | 0 | update></p>
 */
public class SetSlotCommand implements CommandExecutor {
    private static final ItemStack TEST_ITEM_STACK = new ItemStack(Material.DIAMOND_SWORD);

    static {
        ItemMeta meta = TEST_ITEM_STACK.getItemMeta();
        meta.setDisplayName("This item is fake");
        TEST_ITEM_STACK.setItemMeta(meta);
    }

    private final SetSlotCompat compat;

    @Inject
    public SetSlotCommand(SetSlotCompat compat) {
        this.compat = compat;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lagger.setslot")) {
            sender.sendMessage("No permission!");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player!");
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        Player player = (Player) sender;
        try {
            int windowId = Integer.parseInt(args[0]);

            PlayerInventory inventory = player.getInventory();
            int slot = inventory.getHeldItemSlot();

            this.compat.setSlot(player, windowId, slot, TEST_ITEM_STACK);
            player.sendMessage(format("Set slot=%d on windowId=%d to %s",
                    slot, windowId, TEST_ITEM_STACK));
        } catch (NumberFormatException e) {
            if (args[0].equalsIgnoreCase("update")) {
                player.updateInventory();
                player.sendMessage("Updated your inventory to the actual items");
            } else {
                sender.sendMessage("That is not a number");
            }
        }

        return true;
    }
}
