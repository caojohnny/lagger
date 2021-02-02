package com.gmail.woodyc40.lagger;

import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PacketSniffer19R01 implements PacketSniffer {
    private final JavaPlugin plugin;
    private final FieldInspector inspector;

    private final Set<String> filteredPacketNames = new HashSet<>();
    private final Map<Player, Sniffer> sniffing = new HashMap<>();

    private boolean dumpEnabled;

    public PacketSniffer19R01(JavaPlugin plugin) {
        this.plugin = plugin;
        this.inspector = new FieldInspector(plugin);

        this.inspector.addFieldListener(fieldValue -> {
            if (fieldValue instanceof ItemStack) {
                ItemStack item = (ItemStack) fieldValue;
                plugin.getLogger().info(String.format("    %s", item.getTag()));
            }
        });
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
                if (PacketSniffer19R01.this.filteredPacketNames.contains(packetName.toLowerCase())) {
                    return;
                }

                String playerName = ep.getName();
                PacketSniffer19R01.this.plugin.getLogger().info(String.format("SEND PACKET: %s -> %s", packetName, playerName));

                if (PacketSniffer19R01.this.dumpEnabled) {
                    PacketSniffer19R01.this.inspector.dump(packet);
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
