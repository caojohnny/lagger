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
    @Provides
    @Singleton
    ChunkCompat provideChunkCompat() {
        throw new RuntimeException();
    }

    @Provides
    @Singleton
    PacketSniffer providePacketSniffer(Lagger plugin) {
        throw new RuntimeException();
    }

    @Provides
    @Singleton
    SkullCompat provideSkullCompat() {
        throw new RuntimeException();
    }

    @Provides
    @Singleton
    SetSlotCompat provideSetSlotCompat() {
        throw new RuntimeException();
    }
}
