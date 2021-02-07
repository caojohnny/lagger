package com.gmail.woodyc40.lagger.cmd;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Gives the command sender an item with the given name.
 *
 * <p>usage: /getitem &lt;material&gt; [amount]</p>
 */
public class GetItemCommand implements CommandExecutor {
    /**
     * Mapping of item names to the material type.
     */
    private static final Map<String, Material> LOOKUP_TABLE =
            new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    static {
        Pattern space = Pattern.compile(" ", Pattern.LITERAL);

        for (Material material : Material.values()) {
            String name = material.name();
            LOOKUP_TABLE.put(name, material);

            String noSpaceName = space.matcher(name).replaceAll("");
            LOOKUP_TABLE.put(noSpaceName, material);
        }
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender,
                             @Nonnull Command command,
                             @Nonnull String label,
                             @Nonnull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            Material material = findMaterial(args[0]);
            if (material == null) {
                sender.sendMessage(format("No such material '%s'", args[0]));
                return true;
            }

            ItemStack item = new ItemStack(material);
            giveItem(player, item);

            return true;
        }

        if (args.length == 2) {
            Material material = findMaterial(args[0]);
            if (material == null) {
                sender.sendMessage(format("No such material '%s'", args[0]));
                return true;
            }

            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(format("'%s' is not a number", args[1]));
                return true;
            }

            ItemStack item = new ItemStack(material, amount);
            giveItem(player, item);

            return true;
        }

        return false;
    }

    /**
     * Attempts to find the material type given by the
     * name string, searching both the modern and legacy
     * mappings.
     *
     * @param materialName the name of the material
     * @return the material, or {@code null} if no material
     * with the given name exists
     */
    private static Material findMaterial(String materialName) {
        Material material = LOOKUP_TABLE.get(materialName);
        if (material != null) {
            return material;
        }

        String legacy = "LEGACY_" + materialName;
        return LOOKUP_TABLE.get(legacy);
    }

    /**
     * Helper function to give the player an item,
     * otherwise dropping the item if the player's
     * inventory is full.
     *
     * @param player the player to give the item
     * @param item   the item to give the player
     */
    private static void giveItem(Player player, ItemStack item) {
        Location location = player.getLocation();
        World world = requireNonNull(location.getWorld());
        for (ItemStack rem : player.getInventory()
                .addItem(item)
                .values()) {
            world.dropItem(location, rem);
        }
    }
}
