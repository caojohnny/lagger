package com.gmail.woodyc40.lagger;

import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullCompat18R01 implements SkullCompat {
    @Override
    public ItemStack getPlayerSkull(String playerName) {
        ItemStack craftItem = CraftItemStack.asCraftCopy(new ItemStack(Material.SKULL_ITEM, 1, (byte) 3));
        SkullMeta meta = (SkullMeta) craftItem.getItemMeta();
        meta.setOwner(playerName);
        craftItem.setItemMeta(meta);

        return craftItem;
    }

    @Override
    public void ensureSkullTextures(Inventory inv, int slot) {
        IInventory iinv = ((CraftInventory) inv).getInventory();
        net.minecraft.server.v1_8_R3.ItemStack item = iinv.getItem(slot);
        NBTTagCompound tag = item.getTag();

        NBTTagCompound skullOwner = (NBTTagCompound) tag.get("SkullOwner");
        if (skullOwner == null) {
            throw new IllegalArgumentException("No SkullOwner NBT tag");
        }

        if (!skullOwner.hasKey("Properties")) {
            inv.setItem(slot, CraftItemStack.asBukkitCopy(item));
        }
    }
}
