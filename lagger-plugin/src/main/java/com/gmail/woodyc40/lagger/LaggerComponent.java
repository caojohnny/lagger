package com.gmail.woodyc40.lagger;

import com.gmail.woodyc40.lagger.cmd.*;
import com.gmail.woodyc40.lagger.config.Config;
import com.gmail.woodyc40.lagger.listener.DebugModeListener;
import com.gmail.woodyc40.lagger.listener.PacketSnifferListener;
import com.gmail.woodyc40.lagger.module.NmsModule;
import com.gmail.woodyc40.lagger.util.EventSniffer;
import dagger.BindsInstance;
import dagger.Component;

import javax.annotation.Nullable;
import javax.inject.Singleton;

/**
 * Dagger injector component that produces instances of
 * desired classes that require injection.
 */
@Singleton
@Component(modules = {NmsModule.class})
public interface LaggerComponent {
    /**
     * Injects field parameters of the given instance of
     * the plugin main class.
     *
     * @param plugin the instance to inject
     */
    void inject(Lagger plugin);

    /**
     * Obtains a singleton instance of the plugin
     * configuration wrapper.
     *
     * @return the plugin config
     */
    Config getConfig();

    /**
     * Obtains a singleton instance of the packet sniffer.
     *
     * @return the packet sniffer
     */
    PacketSniffer getPacketSniffer();

    /**
     * Obtains a singleton instance of the event sniffer.
     *
     * @return the event sniffer
     */
    EventSniffer getEventSniffer();

    /**
     * Produces a new instance of the listener for packet
     * sniffing.
     *
     * @return the new listener instance
     */
    PacketSnifferListener newPacketSnifferListener();

    /**
     * Produces a new instance of the listener used for
     * the {@code /debugmode} command.
     *
     * @return the new listener instance
     */
    DebugModeListener newDebugModeListener();

    /**
     * Produces a new instance of the {@code /ohi} command
     * handler.
     *
     * @return the new command handler instance
     */
    OpenHeadInventoryCommand newOhiCmd();

    /**
     * Produces a new instance of the {@code /psniff}
     * command handler.
     *
     * @return the new command handler instance
     */
    PacketSnifferCommand newPSniffCmd();

    /**
     * Produces a new instance of the {@code /esniff}
     * command handler.
     *
     * @return the new command handler instance
     */
    EventSnifferCommand newESniffCmd();

    /**
     * Produces a new instance of the {@code /chunk}
     * command handler.
     *
     * @return the new command handler instance
     */
    ChunkCommand newChunkCmd();

    /**
     * Produces a new instance of the {@code /setslot}
     * command handler.
     *
     * @return the new command handler instance
     */
    SetSlotCommand newSetSlotCmd();

    /**
     * Produces a new instance of the {@code /copyplugins}
     * command handler.
     *
     * @return the new command handler instance
     */
    CopyPluginsCommand newCopyPluginsCmd();

    /**
     * Produces a new instance of the {@code /debugmode}
     * command handler.
     *
     * @return the new command handler instance
     */
    DebugModeCommand newDebugModeCmd();

    /**
     * Produces a new instance of the {@code /lca} command
     * handler.
     *
     * @return the new command handler instance
     */
    LoadChunkAsyncCommand newLcaCmd();

    /**
     * Produces a new instance of the {@code /sas} command
     * handler.
     *
     * @return the new command handler instance
     */
    SpawnArmorStandCommand newSasCmd();

    /**
     * The component builder used to configure an instance
     * of this injector.
     */
    @Component.Builder
    interface Builder {
        /**
         * Provides an instance of the plugin main class
         * to the injector.
         *
         * @param plugin the plugin instance
         * @return this builder instance
         */
        @BindsInstance
        Builder plugin(Lagger plugin);

        /**
         * Provides the given instance of the chunk loader
         * abstraction, if available.
         *
         * @param acl the chunk loader
         * @return this builder instance
         */
        @BindsInstance
        Builder asyncChunkLoader(@Nullable AsyncChunkLoader acl);

        /**
         * Provides the given NMS abstraction instance.
         *
         * @param module the abstraction module instance
         * @return this builder instance
         */
        Builder nmsModule(NmsModule module);

        /**
         * Creates the component with the configured
         * provided instances.
         *
         * @return the new component
         */
        LaggerComponent build();
    }
}
