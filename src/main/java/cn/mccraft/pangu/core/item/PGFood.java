package cn.mccraft.pangu.core.item;

import cn.mccraft.pangu.core.capability.CapabilityFood;
import cn.mccraft.pangu.core.capability.FoodStats;
import cn.mccraft.pangu.core.util.LoreHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PGFood extends ItemFood {
    public PGFood() {
        super(0, false);
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, @Nullable World worldIn, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            entityplayer.getFoodStats().addStats(this, stack);
            worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            this.onFoodEaten(stack, worldIn, entityplayer);
            entityplayer.addStat(StatList.getObjectUseStats(this));

            if (entityplayer instanceof EntityPlayerMP)
            {
                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
            }
        }

        stack.shrink(1);
        return stack;
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, @Nonnull EntityPlayer player) {
        if (!worldIn.isRemote && getStats(stack).getPotion() != null && worldIn.rand.nextFloat() < getStats(stack).getPotionEffectProbability())
            player.addPotionEffect(getStats(stack).getPotion());
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return getStats(stack).getMaxItemUseDuration();
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return getStats(stack).getAction();
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (playerIn.canEat(getStats(itemstack).isAlwaysEdible())) {
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
        }
        else
            return new ActionResult<>(EnumActionResult.FAIL, itemstack);
    }

    @Override
    public int getHealAmount(ItemStack stack) {
        return getStats(stack).getAmount();
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        return getStats(stack).getSaturationModifier();
    }

    @Override
    public boolean isWolfsFavoriteMeat() {
        return false; // FIXME
    }

    @Nonnull
    @Override
    public ItemFood setPotionEffect(@Nonnull PotionEffect effect, float probability) {
        return this;
    }

    @Nonnull
    @Override
    public ItemFood setAlwaysEdible() {
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        FoodStats stats = getStats(stack);
        tooltip.add(I18n.format("lore.food.amount", stats.getAmount()));
        tooltip.add(I18n.format("lore.food.saturationModifier", stats.getSaturationModifier()));

        if (flagIn.isAdvanced()) {
            if (stats.isAlwaysEdible())
                tooltip.add(I18n.format("lore.food.alwaysEdible"));
            if (stats.isWolfFood())
                tooltip.add(I18n.format("lore.food.wolfFood"));
        } else {
            LoreHelper.shiftLoreWithI18n(tooltip, getUnlocalizedName());
        }
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public String getItemStackDisplayName(ItemStack stack) {
        final String localizedName = getStats(stack).getUnlocalizedName() == null ? "" : getStats(stack).getUnlocalizedName();
        return I18n.format(localizedName + ".name").trim();
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab))
            items.addAll(FoodManager.INSTANCE.toStacks());
    }

    private FoodStats getStats(@Nonnull ItemStack stack) {
        return stack.getCapability(CapabilityFood.CAPABILITY_FOOD_STATS, null);
    }
}
