package cn.mccraft.pangu.core.potion;

import cn.mccraft.pangu.core.loader.annotation.RegPotion;
import net.minecraft.potion.Potion;

public interface PGPotions {
    @RegPotion
    Potion vertigo = new PGPotion(true, 0xA65D4D);
}
