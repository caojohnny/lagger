package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.*;

public class NmsModule_v1_10_R01 extends NmsModule {
    @Override
    ChunkCompat provideChunkCompat() {
        return new ChunkCompat110R01();
    }

    @Override
    PacketSniffer providePacketSniffer(Lagger plugin) {
        return new PacketSniffer110R01(plugin);
    }

    @Override
    SkullCompat provideSkullCompat() {
        return new SkullCompat110R01();
    }

    @Override
    SetSlotCompat provideSetSlotCompat() {
        return new SetSlotCompat110R01();
    }
}
