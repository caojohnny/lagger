package com.gmail.woodyc40.lagger;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface SetSlotCompat {
    void setSlot(Player player, int windowId, int slot, ItemStack item);
}
