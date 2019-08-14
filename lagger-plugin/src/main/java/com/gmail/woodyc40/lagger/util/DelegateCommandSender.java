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

import java.util.Set;

public class DelegateCommandSender implements CommandSender {
    private final CommandSender delegate;

    public DelegateCommandSender(CommandSender delegate) {
        this.delegate = delegate;
    }

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
    public void sendMessage(String s) {
        this.delegate.sendMessage(s);
    }

    @Override
    public void sendMessage(String[] strings) {
        this.delegate.sendMessage(strings);
    }

    @Override
    public Server getServer() {
        return this.delegate.getServer();
    }

    @Override
    public String getName() {
        return this.delegate.getName();
    }

    @Override
    public Spigot spigot() {
        return this.delegate.spigot();
    }

    @Override
    public boolean isPermissionSet(String s) {
        return this.delegate.isPermissionSet(s);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return this.delegate.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(String s) {
        return this.delegate.hasPermission(s);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return this.delegate.hasPermission(permission);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
        return this.delegate.addAttachment(plugin, s, b);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return this.delegate.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
        return this.delegate.addAttachment(plugin, s, b, i);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int i) {
        return this.delegate.addAttachment(plugin, i);
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment) {
        this.delegate.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions() {
        this.delegate.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
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
