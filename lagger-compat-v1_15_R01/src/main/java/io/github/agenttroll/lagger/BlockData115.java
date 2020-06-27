package io.github.agenttroll.lagger;

import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.IRegistry;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public class BlockData115 {
    public BlockData115() {
        IRegistry.BLOCK.forEach(block -> {
            IBlockData data = block.getBlockData();
            CraftBlockData cbd = CraftBlockData.fromData(data);
            int combinedId = Block.getCombinedId(data);
            Material material = cbd.getMaterial();

            System.out.println(material + ": " + combinedId + " (" + Integer.toBinaryString(combinedId) + ")");
        });
    }
}
