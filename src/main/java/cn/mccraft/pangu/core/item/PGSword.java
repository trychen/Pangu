package cn.mccraft.pangu.core.item;

import cn.mccraft.pangu.core.util.LoreHelper;
import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PGSword extends ItemSword {
    /**
     * If the value is bigger than zero,
     * the entity hit by this weapon will be set fire
     */
    private int fireHitEntityDuration = 0;

    /**
     * The entity hit by this weapon will be add the PotionEffect from this list
     */
    private List<PotionEffect> potionEffectsHitEntity;

    public PGSword(ToolMaterial material) {
        super(material);
    }

    public PGSword setFireHitEntity(int fireDamagedEntityDuration) {
        this.fireHitEntityDuration = fireDamagedEntityDuration;
        return this;
    }

    public PGSword addPotionEffectHitEntity(PotionEffect... potionEffects) {
        if (potionEffectsHitEntity == null) potionEffectsHitEntity = Lists.newArrayList(potionEffects);
        else Collections.addAll(potionEffectsHitEntity, potionEffects);
        return this;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (fireHitEntityDuration > 0) target.setFire(fireHitEntityDuration);
        if (potionEffectsHitEntity != null) potionEffectsHitEntity.forEach(target::addPotionEffect);
        return super.hitEntity(stack, target, attacker);
    }

    /*
      Imported from {@link PGItem}
     */

    private boolean hasI18nLore = false;

    /**
     * Whether adding lore information automatically
     */
    public PGSword setI18nLore() {
        this.hasI18nLore = true;
        return this;
    }

    public boolean hasI18nLore() {
        return hasI18nLore;
    }

    /**
     * make the item will never be break by setting an infinity max damage
     */
    public PGSword setInfinityDamage() {
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
    public PGSword addRepairableItem(ItemStack... itemStacks) {
        if (repairableItems == null) repairableItems = java.util.Arrays.asList(itemStacks);
        else Collections.addAll(repairableItems, itemStacks);
        return this;
    }

    /**
     * Add items that can be used to repair this item for all meta
     */
    public PGSword addRepairableItem(Item... items) {
        List<ItemStack> itemStacks = java.util.Arrays.stream(items).map(item -> new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE)).collect(Collectors.toList());
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
        return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (hasI18nLore()) LoreHelper.shiftLoreWithI18n(tooltip, getTranslationKey());
    }
}
