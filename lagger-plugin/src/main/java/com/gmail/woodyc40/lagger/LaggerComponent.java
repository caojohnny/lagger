package com.gmail.woodyc40.lagger;

import com.gmail.woodyc40.lagger.cmd.*;
import com.gmail.woodyc40.lagger.config.Config;
import com.gmail.woodyc40.lagger.listener.PacketSnifferListener;
import com.gmail.woodyc40.lagger.module.NmsModule;
import dagger.BindsInstance;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = NmsModule.class)
public interface LaggerComponent {
    Config getConfig();
    PacketSniffer getPacketSniffer();

    PacketSnifferListener newPacketSnifferListener();
    OpenHeadInventoryCommand newOhiCmd();
    PacketSnifferCommand newPSniffCmd();
    EventSnifferCommand newESniffCmd();
    ChunkCommand newChunkCmd();
    SetSlotCommand newSetSlotCmd();

    @Component.Builder
    interface Builder {
        @BindsInstance Builder plugin(Lagger plugin);
        Builder nmsModule(NmsModule module);
        LaggerComponent build();
    }
}
