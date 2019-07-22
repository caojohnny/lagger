package com.gmail.woodyc40.lagger.cmd;

import com.gmail.woodyc40.lagger.ChunkCompat;
import com.google.inject.Inject;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static java.lang.String.format;

public class ChunkCommand implements CommandExecutor {
    private final ChunkCompat chunkCompat;

    @Inject
    public ChunkCommand(ChunkCompat chunkCompat) {
        this.chunkCompat = chunkCompat;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player!");
            return true;
        }

        Player player = (Player) sender;
        Chunk chunk = player.getLocation().getChunk();
        String coords = getChunkCoords(chunk);

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("load")) {
                if (chunk.isLoaded()) {
                    sender.sendMessage(format("Chunk '%s' has already loaded, but loading again anyways", coords));
                }

                boolean success = this.chunkCompat.loadChunk(chunk);
                sender.sendMessage(format("Loaded chunk at '%s', success=%s", coords, success));
                return true;
            }

            if (args[0].equalsIgnoreCase("unload")) {
                if (!chunk.isLoaded()) {
                    sender.sendMessage(format("Chunk '%s' has already unloaded, but loading again anyways", coords));
                }

                boolean success = this.chunkCompat.unloadChunk(chunk);
                sender.sendMessage(format("Unloaded chunk at '%s', success=%s", coords, success));
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (!chunk.isLoaded()) {
                    sender.sendMessage(format("Chunk '%s' is unloaded", coords));
                } else {
                    sender.sendMessage(format("Chunk '%s' is loaded", coords));
                }

                boolean unloadSuccess = this.chunkCompat.unloadChunk(chunk);
                boolean loadSuccess = this.chunkCompat.loadChunk(chunk);

                sender.sendMessage(format("Reloaded chunk at '%s', unload=%s load=%s", coords, unloadSuccess, loadSuccess));
                return true;
            }
        }

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("loaded")) {
                if (args[0].equalsIgnoreCase("load")) {
                    sender.sendMessage("Chunks are already unloaded, but loading all of them again anyways");

                    boolean success = true;
                    for (Chunk loaded : player.getWorld().getLoadedChunks()) {
                        if (!this.chunkCompat.loadChunk(loaded)) {
                            success = false;
                        }
                    }
                    sender.sendMessage(format("Loaded all loaded chunks in current world, success=%s", success));
                    return true;
                }

                if (args[0].equalsIgnoreCase("unload")) {
                    boolean success = true;
                    for (Chunk loaded : player.getWorld().getLoadedChunks()) {
                        if (!this.chunkCompat.unloadChunk(loaded)) {
                            success = false;
                        }
                    }
                    sender.sendMessage(format("Unloaded all loaded chunks in current world, success=%s", success));
                    return true;
                }

                if (args[0].equalsIgnoreCase("reload")) {
                    boolean success = true;
                    for (Chunk loaded : player.getWorld().getLoadedChunks()) {
                        if (!this.chunkCompat.unloadChunk(loaded) || !this.chunkCompat.loadChunk(loaded)) {
                            success = false;
                        }
                    }
                    sender.sendMessage(format("Reloaded all loaded chunks in current world, success=%s", success));
                    return true;
                }
            }
        }

        return false;
    }

    private static String getChunkCoords(Chunk chunk) {
        return format("%d,%d", chunk.getX(), chunk.getZ());
    }
}
