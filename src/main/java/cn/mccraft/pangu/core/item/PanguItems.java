package cn.mccraft.pangu.core.item;

import cn.mccraft.pangu.core.loader.annotation.RegItem;
import cn.mccraft.pangu.core.loader.creativetabs.SharedCreativeTab;

@SharedCreativeTab("foods")
public interface PanguItems {
    @RegItem("food")
    PGFood PANGU_FOOD = new PGFood();
}
