package cn.mccraft.pangu.core;

import cn.mccraft.pangu.core.item.PGItem;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.Registering;
import cn.mccraft.pangu.core.loader.annotation.RegItem;
import cn.mccraft.pangu.core.loader.annotation.SetCreativeTab;
import net.minecraft.item.Item;

@AutoWired
@Registering(PanguCore.MODID)
@SetCreativeTab("tools")
public class Tools {
    @RegItem("swords")
    public Item swords = new PGItem().setUnlocalizedName("swords");
}
