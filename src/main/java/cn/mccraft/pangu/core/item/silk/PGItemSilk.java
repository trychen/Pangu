package cn.mccraft.pangu.core.item.silk;

import cn.mccraft.pangu.core.capability.color.CapabilityColor;
import cn.mccraft.pangu.core.item.PGItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PGItemSilk extends PGItem {
    @Nonnull
    public ItemStack createSilkFromColor(int color) {
        ItemStack stack = new ItemStack(this);
        if (stack.hasCapability(CapabilityColor.COLOR_STATS, null)) {
            stack.getCapability(CapabilityColor.COLOR_STATS, null).setColor(color);
        }
        return stack;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (EnumDyeColor dyeColor : EnumDyeColor.values())
                items.add(createSilkFromColor(dyeColor.getColorValue()));
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && stack.hasCapability(CapabilityColor.COLOR_STATS, null))
            tooltip.add(I18n.format("lore.silk.color", stack.getCapability(CapabilityColor.COLOR_STATS, null).getColor()));
    }
}
