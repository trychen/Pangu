package cn.mccraft.pangu.core.item;

import cn.mccraft.pangu.core.util.LoreHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PGItem extends Item {
    private boolean hasI18nLore = false;

    /**
     * Whether adding lore information automatically
     */
    public PGItem setI18nLore() {
        this.hasI18nLore = true;
        return this;
    }

    public boolean hasI18nLore() {
        return hasI18nLore;
    }

    /**
     * make the item will never be break by setting an infinity max damage
     */
    public PGItem setInfinityDamage() {
        this.setMaxDamage(-1);
        return this;
    }

    /**
     * ItemStacks that can be used to repair this item
     */
    private List<ItemStack> repairableItems;

    /**
     * Add ItemStacks that can be used to repair this item
     */
    public PGItem addRepairableItem(ItemStack... itemStacks) {
        if (repairableItems == null) repairableItems = Arrays.asList(itemStacks);
        else Collections.addAll(repairableItems, itemStacks);
        return this;
    }

    /**
     * Add items that can be used to repair this item for all meta
     */
    public PGItem addRepairableItem(Item... items) {
        List<ItemStack> itemStacks = Arrays.stream(items).map(item -> new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE)).collect(Collectors.toList());
        if (repairableItems == null) repairableItems = itemStacks;
        else repairableItems.addAll(itemStacks);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (repairableItems != null) return repairableItems.stream().anyMatch(repairableItem -> OreDictionary.itemMatches(repairableItem, repair, false));
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (hasI18nLore()) LoreHelper.shiftLoreWithI18n(tooltip, getTranslationKey());
    }
}
