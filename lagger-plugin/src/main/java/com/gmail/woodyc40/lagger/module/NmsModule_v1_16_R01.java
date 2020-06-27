package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.*;
import io.github.agenttroll.lagger.ChunkCompat116R01;
import io.github.agenttroll.lagger.PacketSniffer116R01;
import io.github.agenttroll.lagger.SetSlotCompat116R01;
import io.github.agenttroll.lagger.SkullCompat116R01;

public class NmsModule_v1_16_R01 extends NmsModule {
    @Override
    ChunkCompat provideChunkCompat() {
        return new ChunkCompat116R01();
    }

    @Override
    PacketSniffer providePacketSniffer(Lagger plugin) {
        return new PacketSniffer116R01(plugin);
    }

    @Override
    SkullCompat provideSkullCompat() {
        return new SkullCompat116R01();
    }

    @Override
    SetSlotCompat provideSetSlotCompat() {
        return new SetSlotCompat116R01();
    }
}
