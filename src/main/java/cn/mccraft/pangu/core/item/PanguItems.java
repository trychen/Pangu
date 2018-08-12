package cn.mccraft.pangu.core.item;

import cn.mccraft.pangu.core.item.silk.PGItemSilk;
import cn.mccraft.pangu.core.loader.annotation.RegItem;
import cn.mccraft.pangu.core.loader.creativetabs.SharedCreativeTab;

@SharedCreativeTab("core")
public interface PanguItems {
    @RegItem(value = "food", registerModel = false)
    PGFood FOOD = new PGFood();

    @RegItem("silk")
    PGItemSilk SILK = new PGItemSilk();
}
