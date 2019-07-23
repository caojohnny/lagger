package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.ChunkCompat;
import com.gmail.woodyc40.lagger.Lagger;
import com.gmail.woodyc40.lagger.PacketSniffer;
import com.gmail.woodyc40.lagger.SkullCompat;
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
}
