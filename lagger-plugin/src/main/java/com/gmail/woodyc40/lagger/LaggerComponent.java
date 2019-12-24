package com.gmail.woodyc40.lagger;

import com.gmail.woodyc40.lagger.cmd.*;
import com.gmail.woodyc40.lagger.config.Config;
import com.gmail.woodyc40.lagger.listener.DebugModeListener;
import com.gmail.woodyc40.lagger.listener.PacketSnifferListener;
import com.gmail.woodyc40.lagger.module.NmsModule;
import com.gmail.woodyc40.lagger.module.PlugRemapperModule;
import com.gmail.woodyc40.lagger.util.EventSniffer;
import dagger.BindsInstance;
import dagger.Component;

import javax.annotation.Nullable;
import javax.inject.Singleton;

@Singleton
@Component(modules = { NmsModule.class, PlugRemapperModule.class })
public interface LaggerComponent {
    Config getConfig();
    PacketSniffer getPacketSniffer();
    EventSniffer getEventSniffer();

    PacketSnifferListener newPacketSnifferListener();
    DebugModeListener newDebugModeListener();

    OpenHeadInventoryCommand newOhiCmd();
    PacketSnifferCommand newPSniffCmd();
    EventSnifferCommand newESniffCmd();
    ChunkCommand newChunkCmd();
    SetSlotCommand newSetSlotCmd();
    CopyPluginsCommand newCopyPluginsCmd();
    DebugModeCommand newDebugModeCmd();
    LoadChunkAsyncCommand newLcaCmd();

    @Component.Builder
    interface Builder {
        @BindsInstance Builder plugin(Lagger plugin);
        @BindsInstance Builder asyncChunkLoader(@Nullable AsyncChunkLoader acl);
        Builder nmsModule(NmsModule module);
        LaggerComponent build();
    }
}
