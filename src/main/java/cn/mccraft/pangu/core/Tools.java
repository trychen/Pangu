package cn.mccraft.pangu.core;

import cn.mccraft.pangu.core.asm.dev.DevOnly;
import cn.mccraft.pangu.core.item.PGItem;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.Registering;
import cn.mccraft.pangu.core.loader.annotation.RegItem;
import cn.mccraft.pangu.core.loader.creativetabs.SharedCreativeTab;
import net.minecraft.item.Item;

@AutoWired
@Registering(PanguCore.ID)
@SharedCreativeTab("tools")
public class Tools {
    @RegItem("swords")
    public Item swords = new PGItem().setUnlocalizedName("swords");

    @DevOnly
    public static int devOnlyInt = 1;
}
