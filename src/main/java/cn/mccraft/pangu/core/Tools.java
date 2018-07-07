package cn.mccraft.pangu.core;

import cn.mccraft.pangu.core.asm.dev.DevOnly;
import cn.mccraft.pangu.core.block.PGBlock;
import cn.mccraft.pangu.core.item.PGItem;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.Registering;
import cn.mccraft.pangu.core.loader.annotation.RegBlock;
import cn.mccraft.pangu.core.loader.annotation.RegItem;
import cn.mccraft.pangu.core.loader.creativetabs.SharedCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

@DevOnly
@AutoWired
@Registering()
@SharedCreativeTab("tools")
public class Tools {
    @RegItem("swords")
    public Item swords = new PGItem();

    @RegBlock("block")
    public Block block = new PGBlock(Material.PLANTS);
}
