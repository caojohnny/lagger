package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.*;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Represents an abstract module providing injection
 * providers for a variety of versioned class (not
 * necessarily strictly NMS related).
 */
@Module(includes = PlugRemapperModule.class)
public class NmsModule {
    /**
     * Obtains the instance of the NMS chunk compatibility
     * abstraction for this version of the server.
     *
     * @return the NMS chunk compat layer
     */
    @Provides
    @Singleton
    ChunkCompat provideChunkCompat() {
        throw new RuntimeException();
    }

    /**
     * Provides the packet sniffer instance for this
     * version of the server.
     *
     * @param plugin the injected plugin instance
     * @return the instance of the packet sniffer
     */
    @Provides
    @Singleton
    PacketSniffer providePacketSniffer(Lagger plugin) {
        throw new RuntimeException();
    }

    /**
     * Obtains the instance of the NMS skull compatibility
     * abstraction for this version of the server.
     *
     * @return the NMS skull compat layer
     */
    @Provides
    @Singleton
    SkullCompat provideSkullCompat() {
        throw new RuntimeException();
    }

    /**
     * Obtains the instance of the NMS set slot
     * compatibility abstraction for this version of the
     * server.
     *
     * @return the NMS set slot compat layer
     */
    @Provides
    @Singleton
    SetSlotCompat provideSetSlotCompat() {
        throw new RuntimeException();
    }
}
