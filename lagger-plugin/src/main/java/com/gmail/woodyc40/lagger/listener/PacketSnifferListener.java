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
    private final PacketSniffer sniffer;

    @Inject
    public PacketSnifferListener(PacketSniffer sniffer) {
        this.sniffer = sniffer;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.sniffer.unsniff(event.getPlayer());
    }
}
