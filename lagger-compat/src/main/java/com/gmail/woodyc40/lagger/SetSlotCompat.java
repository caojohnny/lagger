package com.gmail.woodyc40.lagger;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * NMS interface for maintaining compatibility between
 * capabilities for setting items in inventories without
 * preventing the item bounce effect.
 */
public interface SetSlotCompat {
    /**
     * Sets the item for the given player in the inventory
     * with the given ID at the given slot to the given
     * item.
     *
     * @param player   the player which to send the update
     * @param windowId the window ID value for the target
     *                 inventory or -2 to use the player
     *                 inventory using an emulated stock
     *                 {@link Inventory#setItem(int, ItemStack)}
     *                 effect
     * @param slot     the slot to set the item
     * @param item     the item to set into the slot
     */
    void setSlot(Player player, int windowId, int slot, ItemStack item);
}
