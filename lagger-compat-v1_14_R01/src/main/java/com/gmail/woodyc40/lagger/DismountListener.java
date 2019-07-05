package com.gmail.woodyc40.lagger;

import org.bukkit.entity.Dolphin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class DismountListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Entity clicked = event.getRightClicked();
        if (clicked instanceof Dolphin) {
            clicked.addPassenger(event.getPlayer());
        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if (event.getDismounted() instanceof Dolphin && event.getEntity() instanceof Player) {
            if (((Player) event.getEntity()).isSneaking()) {
                return;
            } else {
                event.setCancelled(true);
            }
        }
    }
}

