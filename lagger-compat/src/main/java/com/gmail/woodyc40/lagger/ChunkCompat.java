package com.gmail.woodyc40.lagger;

import org.bukkit.Chunk;
import org.bukkit.World;

public interface ChunkCompat {
    boolean loadChunk(World world, int x, int z);

    default boolean loadChunk(Chunk chunk) {
        return this.loadChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
    };

    boolean unloadChunk(World world, int x, int z);

    default boolean unloadChunk(Chunk chunk) {
        return this.unloadChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }
}
