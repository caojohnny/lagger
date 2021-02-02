package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.*;

public class NmsModule_v1_12_R01 extends NmsModule {
    @Override
    ChunkCompat provideChunkCompat() {
        return new ChunkCompat112R01();
    }

    @Override
    PacketSniffer providePacketSniffer(Lagger plugin) {
        return new PacketSniffer112R01(plugin);
    }

    @Override
    SkullCompat provideSkullCompat() {
        return new SkullCompat112R01();
    }

    @Override
    SetSlotCompat provideSetSlotCompat() {
        return new SetSlotCompat112R01();
    }
}
