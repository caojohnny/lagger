package com.gmail.woodyc40.lagger;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityRemoveListener implements Listener {
    @EventHandler
    public void onRemove(EntityRemoveFromWorldEvent event) {
        Entity entity = event.getEntity();
        System.out.println("Removed: " + entity);
    }
}
