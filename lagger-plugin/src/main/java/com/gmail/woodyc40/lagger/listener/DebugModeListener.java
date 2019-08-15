package com.gmail.woodyc40.lagger.listener;

import com.gmail.woodyc40.lagger.Lagger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Singleton
public class DebugModeListener implements Listener {
    private final Set<UUID> debugging = new HashSet<>();

    @Inject
    public DebugModeListener(Lagger plugin) {
        this.startTimeFreeze(plugin);
    }

    private void startTimeFreeze(Lagger plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (this.debugging.isEmpty()) {
                return;
            }

            for (World world : Bukkit.getWorlds()) {
                world.setTime(6000);
            }
        }, 1L, 1L);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        UUID id = entity.getUniqueId();
        if (this.debugging.contains(id)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChange(FoodLevelChangeEvent event) {
        HumanEntity entity = event.getEntity();
        UUID id = entity.getUniqueId();
        if (this.debugging.contains(id)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        Entity target = event.getTarget();
        if (target == null) {
            return;
        }

        UUID id = target.getUniqueId();
        if (this.debugging.contains(id)) {
            event.setTarget(null);
        }
    }

    public Set<UUID> getDebugging() {
        return this.debugging;
    }
}
