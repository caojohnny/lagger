package com.gmail.woodyc40.lagger;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class PacketSniffer18 implements PacketSniffer {
    private final JavaPlugin plugin;
    private final Set<String> filteredPacketNames = new HashSet<>();
    private final Map<Player, Sniffer> sniffing = new HashMap<>();
    private boolean dumpEnabled;

    public PacketSniffer18(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void filter(String packetName) {
        this.filteredPacketNames.add(packetName.toLowerCase());
    }

    @Override
    public void enableDump() {
        this.dumpEnabled = true;
    }

    @Override
    public void disableDump() {
        this.dumpEnabled = false;
    }

    @Override
    public void sniff(Player player) {
        EntityPlayer ep = toNmsPlayer(player);
        PlayerConnection con = ep.playerConnection;
        Sniffer sniffer = new Sniffer(con);

        this.sniffing.put(player, sniffer);

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        NetworkManager manager = con.networkManager;
        ep.playerConnection = new PlayerConnection(server, manager, ep) {
            @Override
            public void sendPacket(Packet packet) {
                super.sendPacket(packet);

                String packetName = packet.getClass().getSimpleName();
                if (filteredPacketNames.contains(packetName.toLowerCase())) {
                    return;
                }

                String playerName = ep.getName();
                plugin.getLogger().info(String.format("SEND PACKET: %s -> %s", packetName, playerName));

                if (dumpEnabled) {
                    dump(packet);
                }
            }
        };
    }

    @Override
    public void unsniff(Player player) {
        Sniffer sniffer = this.sniffing.remove(player);
        if (sniffer != null) {
            EntityPlayer ep = toNmsPlayer(player);
            ep.playerConnection = sniffer.getOriginalConnection();
        }
    }

    private void dump(Packet<?> packet) {
        Logger logger = this.plugin.getLogger();
        logger.info("{");
        for (Field field : packet.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String fieldType = field.getType().getSimpleName();
            String fieldName = field.getName();
            try {
                Object fieldValue = field.get(packet);
                logger.info(String.format("  %s %s = %s", fieldType, fieldName, fieldValue));

                if (fieldValue instanceof ItemStack) {
                    ItemStack item = (ItemStack) fieldValue;
                    logger.info(String.format("    %s", item.getTag()));
                }
            } catch (Exception e) {
                logger.warning(String.format("  %s %s = ? (%s)", fieldType, fieldName, e.getMessage()));
            }
        }

        logger.info("");
        CallTracer.trace(logger);

        logger.info("}");
    }

    private static EntityPlayer toNmsPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    private static class Sniffer {
        private final PlayerConnection originalConnection;

        public Sniffer(PlayerConnection originalConnection) {
            this.originalConnection = originalConnection;
        }

        public PlayerConnection getOriginalConnection() {
            return this.originalConnection;
        }
    }
}
