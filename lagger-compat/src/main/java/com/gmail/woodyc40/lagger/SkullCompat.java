package com.gmail.woodyc40.lagger;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface SkullCompat {
    ItemStack getPlayerSkull(String playerName);

    void ensureSkullTextures(Inventory inv, int slot);
}
