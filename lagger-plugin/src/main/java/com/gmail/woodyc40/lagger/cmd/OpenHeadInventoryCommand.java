package com.gmail.woodyc40.lagger.cmd;

import com.gmail.woodyc40.lagger.Lagger;
import com.gmail.woodyc40.lagger.SkullCompat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenHeadInventoryCommand implements CommandExecutor {
    private final Lagger plugin;
    private final SkullCompat compat;

    private final Map<String, ItemStack> openedPlayers = new HashMap<>();

    @Inject
    public OpenHeadInventoryCommand(Lagger plugin, SkullCompat compat) {
        this.plugin = plugin;
        this.compat = compat;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("lagger.ohi")) {
            sender.sendMessage("No permission!");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Not a player!");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(command.getUsage());
            return true;
        }

        boolean verbose = args.length == 2 && args[1].equalsIgnoreCase("verbose");

        Player player = (Player) sender;
        String targetName = args[0];
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                ItemStack item = this.openedPlayers.computeIfAbsent(targetName, k -> this.compat.getPlayerSkull(targetName));
                addSkullLore(item);
                Bukkit.getScheduler().runTask(this.plugin, () -> player.openInventory(this.createSkullInv(player, item)));

                sender.sendMessage(String.format("Opening inventory with %s's skull | offline=true seen=%s", targetName, this.openedPlayers.containsKey(targetName)));
                if (verbose) {
                    sender.sendMessage(String.format("Head #toString()=%s", item.toString()));
                }
            });
        } else {
            ItemStack item = this.openedPlayers.computeIfAbsent(targetName, k -> this.compat.getPlayerSkull(targetName));
            addSkullLore(item);
            player.openInventory(this.createSkullInv(player, item));

            sender.sendMessage(String.format("Opening inventory with %s's skull | offline=false", targetName));
            if (verbose) {
                sender.sendMessage(String.format("Head #toString()=%s", item.toString()));
            }
        }

        return true;
    }

    private static void addSkullLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(List.of("Don't mind me", "I'm just a test lore"));
        item.setItemMeta(meta);
    }

    private Inventory createSkullInv(Player player, ItemStack skull) {
        Inventory inv = Bukkit.createInventory(player, 9);
        inv.setItem(4, skull);
        this.compat.ensureSkullTextures(inv, 4);

        return inv;
    }
}
