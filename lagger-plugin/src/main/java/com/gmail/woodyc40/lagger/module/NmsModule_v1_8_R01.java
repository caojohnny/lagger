package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.*;

public class NmsModule_v1_8_R01 extends NmsModule {
    @Override
    ChunkCompat provideChunkCompat() {
        return new ChunkCompat18R01();
    }

    @Override
    PacketSniffer providePacketSniffer(Lagger plugin) {
        return new PacketSniffer18R01(plugin);
    }

    @Override
    SkullCompat provideSkullCompat() {
        return new SkullCompat18R01();
    }

    @Override
    SetSlotCompat provideSetSlotCompat() {
        return new SetSlotCompat18R01();
    }
}
