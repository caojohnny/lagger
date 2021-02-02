package com.gmail.woodyc40.lagger;

import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.function.Consumer;

public class AsyncChunkLoaderPaper116 implements AsyncChunkLoader {
    @Override
    public void loadChunkAsync(World world, int x, int z, Consumer<Chunk> callback) {
        world.getChunkAtAsync(x, z, true, callback);
    }
}
