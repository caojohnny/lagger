package com.gmail.woodyc40.lagger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullCompat114R01 implements SkullCompat {
    @Override
    public ItemStack getPlayerSkull(String playerName) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        } else {
            meta.setOwningPlayer(player);
        }

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void ensureSkullTextures(Inventory inv, int slot) {
        // 1.13+ does its own skull caching so this isn't needed
    }
}
