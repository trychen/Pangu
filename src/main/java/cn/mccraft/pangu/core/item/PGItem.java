package cn.mccraft.pangu.core.item;

import cn.mccraft.pangu.core.util.LoreHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class PGItem extends Item {
    private boolean i18nLore = false;

    public PGItem setI18nLore() {
        this.i18nLore = true;
        return this;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (i18nLore) LoreHelper.shiftLoreWithI18n(tooltip, getUnlocalizedName());
    }
}
