package com.gmail.woodyc40.lagger;

import org.bukkit.entity.Player;

public interface PacketSniffer {
    void filter(String packetName);

    void enableDump();

    void disableDump();

    void sniff(Player player);

    void unsniff(Player player);
}
