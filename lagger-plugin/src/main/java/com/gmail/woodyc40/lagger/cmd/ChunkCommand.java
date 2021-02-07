package com.gmail.woodyc40.lagger.cmd;

import com.gmail.woodyc40.lagger.ChunkCompat;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Chunk manipulation and information commands.
 *
 * <p>usage: /chunk [load | loadasync | unload | reload | help] [loaded]</p>
 */
public class ChunkCommand implements CommandExecutor {
    private final ChunkCompat chunkCompat;

    @Inject
    public ChunkCommand(ChunkCompat chunkCompat) {
        this.chunkCompat = chunkCompat;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             @Nonnull Command command,
                             @Nonnull String label,
                             @Nonnull String[] args) {
        if (!sender.hasPermission("lagger.chunk")) {
            sender.sendMessage("No permission!");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            return sendChunkInfo(player);
        } else if (args.length == 1) {
            return this.handleLocalChunk(player, args);
        } else if (args.length == 2) {
            return this.handleLoadedChunks(player, args);
        }

        return false;
    }

    /**
     * Send information about the current chunk and the
     * chunks in the player's world.
     *
     * @param player the player to which to send the
     *               information
     * @return whether the command was valid
     */
    @SuppressWarnings("SameReturnValue")
    private static boolean sendChunkInfo(Player player) {
        Location location = player.getLocation();
        Chunk chunk = location.getChunk();
        World world = requireNonNull(location.getWorld());
        Chunk[] chunks = world.getLoadedChunks();

        int tiles = 0;
        for (Chunk c : chunks) {
            tiles += c.getTileEntities().length;
        }

        player.sendMessage(format("Current world: %s", world.getName()));
        player.sendMessage(format("Current chunk coordinates: %s", getChunkCoords(chunk)));
        player.sendMessage(format("Relative chunk coordinates: %d %d %d",
                location.getBlockX() & 15, location.getBlockY(), location.getBlockZ() & 15));
        player.sendMessage(format("Loaded chunks: %d", chunks.length));
        player.sendMessage(format("Loaded entities: %d", world.getEntities().size()));
        player.sendMessage(format("Loaded tile entities: %d", tiles));

        return true;
    }

    /**
     * Handles the case for the player manipulating the
     * chunk on which they are standing.
     *
     * @param player the player who dispatched the command
     * @param args   the command arguments
     * @return whether the command is valid
     */
    private boolean handleLocalChunk(Player player, String[] args) {
        Chunk chunk = player.getLocation().getChunk();
        String coords = getChunkCoords(chunk);

        if (args[0].equalsIgnoreCase("load")) {
            if (chunk.isLoaded()) {
                player.sendMessage(format("Chunk '%s' has already loaded, but loading again anyways", coords));
            }

            boolean success = this.chunkCompat.loadChunk(chunk);
            player.sendMessage(format("Loaded chunk at '%s', success=%s", coords, success));
            return true;
        }

        if (args[0].equalsIgnoreCase("unload")) {
            if (!chunk.isLoaded()) {
                player.sendMessage(format("Chunk '%s' has already unloaded, but loading again anyways", coords));
            }

            boolean success = this.chunkCompat.unloadChunk(chunk);
            player.sendMessage(format("Unloaded chunk at '%s', success=%s", coords, success));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!chunk.isLoaded()) {
                player.sendMessage(format("Chunk '%s' is unloaded", coords));
            } else {
                player.sendMessage(format("Chunk '%s' is loaded", coords));
            }

            boolean unloadSuccess = this.chunkCompat.unloadChunk(chunk);
            boolean loadSuccess = this.chunkCompat.loadChunk(chunk);

            player.sendMessage(format("Reloaded chunk at '%s', unload=%s load=%s", coords, unloadSuccess, loadSuccess));
            return true;
        }

        return false;
    }

    /**
     * Handles the case for the player manipulating the
     * loaded chunks in their world.
     *
     * @param player the player who dispatched the command
     * @param args   the command arguments
     * @return whether the command is valid
     */
    private boolean handleLoadedChunks(Player player, String[] args) {
        if (args[1].equalsIgnoreCase("loaded")) {
            Chunk[] chunks = player.getWorld().getLoadedChunks();
            int count = chunks.length;

            if (args[0].equalsIgnoreCase("load")) {
                player.sendMessage("Chunks are already unloaded, but loading all of them again anyways");

                boolean success = true;
                for (Chunk loaded : chunks) {
                    if (!this.chunkCompat.loadChunk(loaded)) {
                        success = false;
                    }
                }
                player.sendMessage(format("Loaded %d loaded chunks in current world, success=%s", count, success));
                return true;
            }

            if (args[0].equalsIgnoreCase("unload")) {
                boolean success = true;
                for (Chunk loaded : chunks) {
                    if (!this.chunkCompat.unloadChunk(loaded)) {
                        success = false;
                    }
                }
                player.sendMessage(format("Unloaded %d loaded chunks in current world, success=%s", count, success));
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                boolean success = true;
                for (Chunk loaded : chunks) {
                    if (!this.chunkCompat.unloadChunk(loaded) || !this.chunkCompat.loadChunk(loaded)) {
                        success = false;
                    }
                }
                player.sendMessage(format("Reloaded %d loaded chunks in current world, success=%s", count, success));
                return true;
            }
        }

        return false;
    }

    /**
     * Formats the coordinates of the given chunk as a
     * X,Z string.
     *
     * @param chunk the chunk whose coordinates to format
     * @return the formatted coordinate string
     */
    private static String getChunkCoords(Chunk chunk) {
        return format("%d,%d", chunk.getX(), chunk.getZ());
    }
}
