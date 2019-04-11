package com.gmail.woodyc40.lagger.cmd;

import com.gmail.woodyc40.lagger.SkullCompat;
import com.gmail.woodyc40.lagger.Lagger;
import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class OpenHeadInventory implements CommandExecutor {
    private final Lagger plugin;
    private final SkullCompat compat;

    private final Map<String, ItemStack> openedPlayers = new HashMap<>();

    @Inject
    public OpenHeadInventory(Lagger plugin, SkullCompat compat) {
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

        if (args.length != 1) {
            sender.sendMessage(command.getUsage());
            return true;
        }

        Player player = (Player) sender;
        String targetName = args[0];
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage(String.format("Opening inventory with %s's skull | offline=true seen=%s", targetName, this.openedPlayers.containsKey(targetName)));
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                ItemStack item = this.openedPlayers.computeIfAbsent(targetName, k -> this.compat.getPlayerSkull(targetName));
                Bukkit.getScheduler().runTask(this.plugin, () -> player.openInventory(this.createSkullInv(player, item)));
            });
        } else {
            sender.sendMessage(String.format("Opening inventory with %s's skull | offline=false", targetName));
            ItemStack item = this.openedPlayers.computeIfAbsent(targetName, k -> this.compat.getPlayerSkull(targetName));
            player.openInventory(this.createSkullInv(player, item));
        }

        return true;
    }

    private Inventory createSkullInv(Player player, ItemStack skull) {
        Inventory inv = Bukkit.createInventory(player, 9);
        inv.setItem(4, skull);
        this.compat.ensureSkullTextures(inv, 4);

        return inv;
    }
}
