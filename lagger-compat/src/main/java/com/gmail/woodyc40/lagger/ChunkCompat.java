package com.gmail.woodyc40.lagger;

import org.bukkit.Chunk;
import org.bukkit.World;

/**
 * Chunk loading compatibility between different versions
 * of the Spigot API.
 */
public interface ChunkCompat {
    /**
     * Loads the given chunk at the given world at the
     * given coordinates.
     *
     * @param world the world to load the chunk
     * @param x the chunk X coordinate
     * @param z the chunk Z coordinate
     * @return {@code true} if the chunk was successfully
     * loaded
     */
    boolean loadChunk(World world, int x, int z);

    /**
     * Loads the given chunk object.
     *
     * @param chunk the chunk to load
     * @return {@code true} if the chunk was successfully
     * loaded
     */
    default boolean loadChunk(Chunk chunk) {
        return this.loadChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }

    /**
     * Unloads the chunk in the given world at the given
     * chunk coordinates.
     *
     * @param world the world to load the chunk
     * @param x the chunk X coordinate
     * @param z the chunk Z coordinate
     * @return {@code true} if the chunk was unloaded
     * successfully
     */
    boolean unloadChunk(World world, int x, int z);

    /**
     * Unloads the given chunk object.
     *
     * @param chunk the chunk to unload
     * @return {@code true} if the chunk was unloaded
     * successfully
     */
    default boolean unloadChunk(Chunk chunk) {
        return this.unloadChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }
}
