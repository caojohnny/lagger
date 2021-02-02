package com.gmail.woodyc40.lagger;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutSetSlot;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetSlotCompat112R01 implements SetSlotCompat {
    @Override
    public void setSlot(Player player, int windowId, int slot, ItemStack item) {
        // Code lifted from CraftInventoryPlayer
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        if (ep.playerConnection != null) {
            if (slot < net.minecraft.server.v1_12_R1.PlayerInventory.getHotbarSize()) {
                slot += 36;
            } else if (slot > 39) {
                slot += 5;
            } else if (slot > 35) {
                slot = 8 - (slot - 36);
            }

            if (windowId == -2) {
                PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(0, slot, CraftItemStack.asNMSCopy(item));
                ep.playerConnection.sendPacket(packet);
                ep.playerConnection.sendPacket(packet);
                return;
            }

            ep.playerConnection.sendPacket(new PacketPlayOutSetSlot(windowId, slot, CraftItemStack.asNMSCopy(item)));
        }
    }
}
