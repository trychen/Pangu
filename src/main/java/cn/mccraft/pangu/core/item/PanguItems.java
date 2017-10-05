package cn.mccraft.pangu.core.item;

import cn.mccraft.pangu.core.loader.annotation.RegItem;

public interface PanguItems {
    PanguItems INSTANCE = new PanguItems() {};

    @RegItem("food")
    ItemPanguFood PANGU_FOOD = new ItemPanguFood();
}
