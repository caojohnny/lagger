package com.gmail.woodyc40.lagger;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * NMS compatibility layer for getting skull items for
 * caching player skull skins.
 */
public interface SkullCompat {
    /**
     * Obtains the skull for the player with the given
     * name. May block.
     *
     * @param playerName the name of the player whose skin
     *                   to set to the returned skull
     * @return the skull with the player skin
     */
    ItemStack getPlayerSkull(String playerName);

    /**
     * Ensures that the skull meta has been set once it is
     * inserted into the inventory.
     *
     * @param inv  the inventory containing the skull
     * @param slot the slot containing the skull
     */
    void ensureSkullTextures(Inventory inv, int slot);
}
