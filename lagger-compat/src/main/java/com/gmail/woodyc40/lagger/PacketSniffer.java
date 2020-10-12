package com.gmail.woodyc40.lagger;

import org.bukkit.entity.Player;

/**
 * Packet interception utility. This does not support
 * handshake or login packet interception.
 */
public interface PacketSniffer {
    /**
     * Adds the packet with the given NMS simple class name
     * to the filter, preventing it from being handled by
     * the sniffer.
     *
     * @param packetName the packet NMS simple class name
     */
    void filter(String packetName);

    /**
     * Enables additional debug information associated with
     * the packet being sent or received.
     */
    void enableDump();

    /**
     * Disables additional debug information printed.
     */
    void disableDump();

    /**
     * Begins the packet sniffer on the given player's
     * packet traffic.
     *
     * @param player the player whose connection to
     *               intercept packets
     */
    void sniff(Player player);

    /**
     * Disables the sniffer on the given player's
     * connection.
     *
     * @param player the player whose connection to
     *               disable sniffing
     */
    void unsniff(Player player);
}
