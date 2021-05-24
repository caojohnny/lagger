package io.github.caojohnny.lagger;

import com.gmail.woodyc40.lagger.ChunkCompat;
import org.bukkit.World;

public class ChunkCompat116R01 implements ChunkCompat {
    @Override
    public boolean loadChunk(World world, int x, int z) {
        return world.loadChunk(x, z, true);
    }

    @Override
    public boolean unloadChunk(World world, int x, int z) {
        return world.unloadChunk(x, z, true);
    }
}
