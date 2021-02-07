package com.gmail.woodyc40.lagger;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import static java.util.Objects.requireNonNull;

public class SkullCompat112R01 implements SkullCompat {
    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getPlayerSkull(String playerName) {
        ItemStack craftItem = CraftItemStack.asCraftCopy(new ItemStack(Material.SKULL_ITEM, 1, (byte) 3));
        SkullMeta meta = (SkullMeta) craftItem.getItemMeta();
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        } else {
            meta.setOwningPlayer(player);
        }
        craftItem.setItemMeta(meta);

        return craftItem;
    }

    @Override
    public void ensureSkullTextures(Inventory inv, int slot) {
        IInventory iinv = ((CraftInventory) inv).getInventory();
        net.minecraft.server.v1_12_R1.ItemStack item = requireNonNull(iinv.getItem(slot));
        NBTTagCompound tag = requireNonNull(item.getTag());

        NBTTagCompound skullOwner = (NBTTagCompound) tag.get("SkullOwner");
        if (skullOwner == null) {
            throw new IllegalArgumentException("No SkullOwner NBT tag");
        }

        GameProfile profile = requireNonNull(GameProfileSerializer.deserialize(skullOwner));
        if (!profile.getProperties().containsKey("textures")) {
            TileEntitySkull.b(profile, input -> {
                NBTTagCompound owner = new NBTTagCompound();
                GameProfileSerializer.serialize(owner, input);
                tag.set("SkullOwner", owner);

                updateSlot(inv, slot, item);
                return false;
            }, false);
        }
    }

    private static void updateSlot(Inventory inventory, int slot, net.minecraft.server.v1_12_R1.ItemStack item) {
        for (HumanEntity viewer : inventory.getViewers()) {
            Player player = (Player) viewer;
            EntityPlayer ep = ((CraftPlayer) player).getHandle();
            ep.a(ep.activeContainer, slot, item);
        }
    }
}
