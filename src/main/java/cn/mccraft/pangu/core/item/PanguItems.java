package cn.mccraft.pangu.core.item;

import cn.mccraft.pangu.core.loader.annotation.RegItem;

public interface PanguItems {
    @RegItem("food")
    PGFood PANGU_FOOD = new PGFood();
}
