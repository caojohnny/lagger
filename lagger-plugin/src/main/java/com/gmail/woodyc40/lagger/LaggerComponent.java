package com.gmail.woodyc40.lagger;

import com.gmail.woodyc40.lagger.cmd.ChunkCommand;
import com.gmail.woodyc40.lagger.cmd.EventSnifferCommand;
import com.gmail.woodyc40.lagger.cmd.OpenHeadInventoryCommand;
import com.gmail.woodyc40.lagger.cmd.PacketSnifferCommand;
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

    @Component.Builder
    interface Builder {
        @BindsInstance Builder plugin(Lagger plugin);
        Builder nmsModule(NmsModule module);
        LaggerComponent build();
    }
}
