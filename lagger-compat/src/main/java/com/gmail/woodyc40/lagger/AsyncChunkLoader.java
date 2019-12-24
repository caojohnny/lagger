package com.gmail.woodyc40.lagger;

import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.function.Consumer;

public interface AsyncChunkLoader {
    void loadChunkAsync(World world, int x, int z, Consumer<Chunk> callback);
}
