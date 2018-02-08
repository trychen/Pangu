package cn.mccraft.pangu.core.item;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.Registering;
import cn.mccraft.pangu.core.loader.annotation.RegItem;
import cn.mccraft.pangu.core.loader.annotation.SetCreativeTab;

@Registering(PanguCore.MODID)
public interface PanguItems {

    @RegItem("food")
    @SetCreativeTab("tools")
    PGFood PANGU_FOOD = new PGFood();

}
