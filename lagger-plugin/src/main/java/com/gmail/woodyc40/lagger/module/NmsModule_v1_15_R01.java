package com.gmail.woodyc40.lagger.module;

import com.gmail.woodyc40.lagger.*;
import io.github.caojohnny.lagger.ChunkCompat115R01;
import io.github.caojohnny.lagger.PacketSniffer115R01;
import io.github.caojohnny.lagger.SetSlotCompat115R01;
import io.github.caojohnny.lagger.SkullCompat115R01;

public class NmsModule_v1_15_R01 extends NmsModule {
    @Override
    ChunkCompat provideChunkCompat() {
        return new ChunkCompat115R01();
    }

    @Override
    PacketSniffer providePacketSniffer(Lagger plugin) {
        return new PacketSniffer115R01(plugin);
    }

    @Override
    SkullCompat provideSkullCompat() {
        return new SkullCompat115R01();
    }

    @Override
    SetSlotCompat provideSetSlotCompat() {
        return new SetSlotCompat115R01();
    }
}
