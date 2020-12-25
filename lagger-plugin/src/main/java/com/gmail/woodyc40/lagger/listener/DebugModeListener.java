package com.gmail.woodyc40.lagger.listener;

import com.gmail.woodyc40.lagger.Lagger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
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

/**
 * The purpose of this listener is to cancel certain events
 * for debug mode that would make plugin testing easier.
 */
@Singleton
public class DebugModeListener implements Listener {
    /**
     * A session-persistent store of players currently in
     * debug mode.
     */
    private final Set<UUID> debugging = new HashSet<>();

    /**
     * Creates a new instance of this listener using the
     * given injected instance of the plugin.
     *
     * @param plugin the injected plugin instance
     */
    @Inject
    public DebugModeListener(Lagger plugin) {
        this.startTimeFreeze(plugin);
    }

    /**
     * Begin a task that will lock the time of day if
     * anyone is currently debugging.
     *
     * @param plugin the Lagger plugin instance
     */
    private void startTimeFreeze(Lagger plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (this.debugging.isEmpty()) {
                return;
            }

            for (World world : Bukkit.getWorlds()) {
                // Keep these values constant
                world.setTime(world.getTime());
                world.setWeatherDuration(Integer.MAX_VALUE);
            }
        }, 1L, 1L);
    }

    /**
     * Cancels damage to players who are currently in debug
     * mode.
     *
     * @param event the injected event
     */
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        UUID id = entity.getUniqueId();
        if (this.debugging.contains(id)) {
            event.setCancelled(true);
        }
    }

    /**
     * Cancels any changes to the player's hunger level if
     * they are in debug mode.
     *
     * @param event the injected event
     */
    @EventHandler
    public void onChange(FoodLevelChangeEvent event) {
        HumanEntity entity = event.getEntity();
        UUID id = entity.getUniqueId();
        if (this.debugging.contains(id)) {
            event.setCancelled(true);
        }
    }

    /**
     * Cancels any mob targeting actions if the new target
     * is a player who is in debug mode.
     *
     * @param event the injected event
     */
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

    /**
     * Toggles the debug mode of the player. This sets
     * whether they are currently debugging to the opposite
     * to their current state.
     *
     * @param player the player to toggle the debug mode
     * @return {@code true} if they are now in debug mode
     */
    public boolean toggleDebugMode(Player player) {
        UUID id = player.getUniqueId();
        if (this.debugging.remove(id)) {
            return false;
        } else {
            this.debugging.add(id);
            return true;
        }
    }
}
