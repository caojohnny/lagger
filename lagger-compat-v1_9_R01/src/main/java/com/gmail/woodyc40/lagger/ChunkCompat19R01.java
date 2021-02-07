package com.gmail.woodyc40.lagger;

import org.bukkit.World;

public class ChunkCompat19R01 implements ChunkCompat {
    @Override
    public boolean loadChunk(World world, int x, int z) {
        return world.loadChunk(x, z, true);
    }

    @Override
    public boolean unloadChunk(World world, int x, int z) {
        // 1.9 deprecates force chunk unloading
        return world.unloadChunk(x, z, true);
    }
}
