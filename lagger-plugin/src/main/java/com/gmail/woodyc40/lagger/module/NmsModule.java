package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.*;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class NmsModule {
    @Provides
    @Singleton
    ChunkCompat provideChunkCompat() {
        return null;
    }

    @Provides
    @Singleton
    PacketSniffer providePacketSniffer(Lagger plugin) {
        return null;
    }

    @Provides
    @Singleton
    SkullCompat provideSkullCompat() {
        return null;
    }

    @Provides
    @Singleton
    SetSlotCompat provideSetSlotCompat() {
        return null;
    }
}
