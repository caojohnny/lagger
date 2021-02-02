package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.*;

public class NmsModule_v1_11_R01 extends NmsModule {
    @Override
    ChunkCompat provideChunkCompat() {
        return new ChunkCompat111R01();
    }

    @Override
    PacketSniffer providePacketSniffer(Lagger plugin) {
        return new PacketSniffer111R01(plugin);
    }

    @Override
    SkullCompat provideSkullCompat() {
        return new SkullCompat111R01();
    }

    @Override
    SetSlotCompat provideSetSlotCompat() {
        return new SetSlotCompat111R01();
    }
}
