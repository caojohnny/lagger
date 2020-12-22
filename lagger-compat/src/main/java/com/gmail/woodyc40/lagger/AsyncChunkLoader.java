package com.gmail.woodyc40.lagger;

import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.function.Consumer;

/**
 * Provides access to asynchronous chunk loading should the
 * server support it.
 */
public interface AsyncChunkLoader {
    /**
     * Loads the chunk in the given world at the given
     * chunk coordinates asynchronously. The given callback
     * will be executed on the main thread when the chunk
     * is finished loading.
     *
     * @param world the world to load the chunk
     * @param x the chunk X coordinate
     * @param z the chunk Z coordinate
     * @param callback the callback to invoke when finished
     */
    void loadChunkAsync(World world, int x, int z, Consumer<Chunk> callback);
}
