package com.gmail.woodyc40.lagger.listener;

import com.gmail.woodyc40.lagger.PacketSniffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.inject.Inject;

/**
 * The purpose of this listener is to clean-up and disable
 * the packet listener injection for players that leave the
 * server.
 */
public class PacketSnifferListener implements Listener {
    /**
     * The instance of the {@link PacketSniffer} being
     * used by the plugin.
     */
    private final PacketSniffer sniffer;

    /**
     * Creates a new instance of the listener using an
     * injected instance of the sniffer.
     *
     * @param sniffer the injected instance of the sniffer
     */
    @Inject
    public PacketSnifferListener(PacketSniffer sniffer) {
        this.sniffer = sniffer;
    }

    /**
     * Unsniffs from the player's connection when they
     * leave the server.
     *
     * @param event the injected event
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.sniffer.unsniff(event.getPlayer());
    }
}
