package com.gmail.woodyc40.lagger.cmd;

import com.gmail.woodyc40.lagger.Lagger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static java.util.Objects.requireNonNull;

/**
 * Spawns an armor stand and gives it a velocity to permit
 * it to move. The purpose of this command is to test the
 * effect of {@link ArmorStand#setMarker(boolean)} on the
 * hitbox of the armor stand.
 *
 * <p>usage: /sas [marker]</p>
 */
public class SpawnArmorStandCommand implements CommandExecutor {
    private final Lagger plugin;

    @Inject
    public SpawnArmorStandCommand(Lagger plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             @Nonnull Command command,
                             @Nonnull String label,
                             @Nonnull String[] args) {
        if (!sender.hasPermission("lagger.spawnarmorstand")) {
            sender.sendMessage("No permission!");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player!");
            return true;
        }

        Player player = (Player) sender;
        Location location = player.getLocation();
        World world = requireNonNull(location.getWorld());
        Vector direction = location.getDirection();

        ArmorStand as = world.spawn(location, ArmorStand.class);
        as.setMarker(args.length == 1);
        as.setVisible(true);
        new BukkitRunnable() {
            private int runCount;

            @Override
            public void run() {
                as.setVelocity(direction);

                if (this.runCount++ == 6) {
                    this.cancel();
                    as.remove();
                }
            }
        }.runTaskTimer(this.plugin, 0L, 10L);

        return true;
    }
}
