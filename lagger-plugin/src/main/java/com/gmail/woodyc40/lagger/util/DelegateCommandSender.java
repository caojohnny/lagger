package com.gmail.woodyc40.lagger.util;

import org.bukkit.Server;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * This is a modified CommandSender subclass that acts as
 * an adapter for proxying certain methods.
 */
public class DelegateCommandSender implements CommandSender {
    private final CommandSender delegate;

    /**
     * Creates the delegate CommandSender that will forward
     * calls to the given delegate.
     *
     * @param delegate the forwarding target
     */
    public DelegateCommandSender(CommandSender delegate) {
        this.delegate = delegate;
    }

    /**
     * Performs a match check against the given sender.
     * This behaves differently because it assumes that all
     * console and block command senders will be equal to
     * their respective types while player senders will
     * have the same names.
     *
     * @param sender the sender to check
     * @return {@code true} if they match
     */
    public boolean matches(CommandSender sender) {
        if (this.delegate instanceof ConsoleCommandSender) {
            return sender instanceof ConsoleCommandSender;
        }

        if (this.delegate instanceof BlockCommandSender) {
            return sender instanceof BlockCommandSender;
        }

        if (this.delegate instanceof Player && sender instanceof Player) {
            return sender.getName().equals(this.delegate.getName());
        }

        return false;
    }

    @Override
    public void sendMessage(@Nonnull String s) {
        this.delegate.sendMessage(s);
    }

    @Override
    public void sendMessage(@Nonnull String[] strings) {
        this.delegate.sendMessage(strings);
    }

    @Override
    public @Nonnull
    Server getServer() {
        return this.delegate.getServer();
    }

    @Override
    public @Nonnull
    String getName() {
        return this.delegate.getName();
    }

    @Override
    public @Nonnull
    Spigot spigot() {
        return this.delegate.spigot();
    }

    @Override
    public boolean isPermissionSet(@Nonnull String s) {
        return this.delegate.isPermissionSet(s);
    }

    @Override
    public boolean isPermissionSet(@Nonnull Permission permission) {
        return this.delegate.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(@Nonnull String s) {
        return this.delegate.hasPermission(s);
    }

    @Override
    public boolean hasPermission(@Nonnull Permission permission) {
        return this.delegate.hasPermission(permission);
    }

    @Override
    public @Nonnull
    PermissionAttachment addAttachment(@Nonnull Plugin plugin, @Nonnull String s, boolean b) {
        return this.delegate.addAttachment(plugin, s, b);
    }

    @Override
    public @Nonnull
    PermissionAttachment addAttachment(@Nonnull Plugin plugin) {
        return this.delegate.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(@Nonnull Plugin plugin, @Nonnull String s, boolean b, int i) {
        return this.delegate.addAttachment(plugin, s, b, i);
    }

    @Override
    public PermissionAttachment addAttachment(@Nonnull Plugin plugin, int i) {
        return this.delegate.addAttachment(plugin, i);
    }

    @Override
    public void removeAttachment(@Nonnull PermissionAttachment permissionAttachment) {
        this.delegate.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions() {
        this.delegate.recalculatePermissions();
    }

    @Override
    public @Nonnull
    Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return this.delegate.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return this.delegate.isOp();
    }

    @Override
    public void setOp(boolean b) {
        this.delegate.setOp(b);
    }
}
