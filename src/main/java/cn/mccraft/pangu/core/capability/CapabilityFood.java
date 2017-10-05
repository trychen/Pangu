package cn.mccraft.pangu.core.capability;

import cn.mccraft.pangu.core.util.function.FoodEatenCallback;
import cn.mccraft.pangu.core.util.function.FoodUseFinishCallback;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumAction;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface CapabilityFood {
    @CapabilityInject(FoodStats.class)
    Capability<FoodStats> CAPABILITY_FOOD_STATS = null;

    class Implementation implements FoodStats {
        private int amount;
        private boolean wolfFood = false;
        private float saturationModifier = 0.6f;
        private boolean alwaysEdible = false;
        private int maxItemUseDuration = 32;
        private EnumAction action = EnumAction.EAT;
        private PotionEffect potion;
        private float potionEffectProbability;
        private String unlocalizedName = "";
        private ModelResourceLocation model;
        private FoodUseFinishCallback useFinishCallback = FoodUseFinishCallback.BASIC_CALLBACK;
        private FoodEatenCallback eatenCallback = FoodEatenCallback.BASIC_CALLBACK;

        @Override
        public int getAmount() {
            return amount;
        }

        @Override
        public FoodStats setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        @Override
        public boolean isWolfFood() {
            return wolfFood;
        }

        @Override
        public FoodStats setWolfFood() {
            wolfFood = true;
            return this;
        }

        @Override
        public float getSaturationModifier() {
            return saturationModifier;
        }

        @Override
        public FoodStats setSaturationModifier(float saturationModifier) {
            this.saturationModifier = saturationModifier;
            return this;
        }

        @Override
        public boolean isAlwaysEdible() {
            return alwaysEdible;
        }

        public FoodStats setAlwaysEdible() {
            alwaysEdible = true;
            return this;
        }

        @Override
        public int getMaxItemUseDuration() {
            return maxItemUseDuration;
        }

        @Override
        public FoodStats setMaxItemUseDuration(int maxItemUseDuration) {
            this.maxItemUseDuration = maxItemUseDuration;
            return this;
        }

        @Override
        public EnumAction getAction() {
            return action;
        }

        @Override
        public FoodStats setAction(EnumAction action) {
            this.action = action;
            return this;
        }

        @Override
        public FoodUseFinishCallback getUseFinishCallback() {
            return useFinishCallback;
        }

        @Override
        public FoodStats setUseFinishCallback(FoodUseFinishCallback useFinishCallback) {
            this.useFinishCallback = useFinishCallback;
            return this;
        }

        @Override
        public FoodEatenCallback getEatenCallback() {
            return eatenCallback;
        }

        @Override
        public FoodStats setEatenCallback(FoodEatenCallback eatenCallback) {
            this.eatenCallback = eatenCallback;
            return this;
        }

        @Override
        public PotionEffect getPotion() {
            return potion;
        }

        @Override
        public FoodStats setPotion(PotionEffect potion) {
            this.potion = potion;
            return this;
        }

        @Override
        public float getPotionEffectProbability() {
            return potionEffectProbability;
        }

        @Override
        public FoodStats setPotionEffectProbability(float potionEffectProbability) {
            this.potionEffectProbability = potionEffectProbability;
            return this;
        }

        @Override
        public String getUnlocalizedName() {
            return unlocalizedName;
        }

        @Override
        public FoodStats setUnlocalizedName(String unlocalizedName) {
            this.unlocalizedName = unlocalizedName;
            return this;
        }

        @Override
        public ModelResourceLocation getModel() {
            return model;
        }

        @Override
        public FoodStats setModel(ModelResourceLocation model) {
            this.model = model;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Implementation that = (Implementation) o;

            if (amount != that.amount) return false;
            if (wolfFood != that.wolfFood) return false;
            if (Float.compare(that.saturationModifier, saturationModifier) != 0) return false;
            if (alwaysEdible != that.alwaysEdible) return false;
            if (maxItemUseDuration != that.maxItemUseDuration) return false;
            if (Float.compare(that.potionEffectProbability, potionEffectProbability) != 0) return false;
            if (action != that.action) return false;
            if (potion != null ? !potion.equals(that.potion) : that.potion != null) return false;
            if (unlocalizedName != null ? !unlocalizedName.equals(that.unlocalizedName) : that.unlocalizedName != null)
                return false;
            if (model != null ? !model.equals(that.model) : that.model != null) return false;
            if (useFinishCallback != null ? !useFinishCallback.equals(that.useFinishCallback) : that.useFinishCallback != null)
                return false;
            return eatenCallback != null ? eatenCallback.equals(that.eatenCallback) : that.eatenCallback == null;
        }

        @Override
        public int hashCode() {
            int result = amount;
            result = 31 * result + (wolfFood ? 1 : 0);
            result = 31 * result + (saturationModifier != +0.0f ? Float.floatToIntBits(saturationModifier) : 0);
            result = 31 * result + (alwaysEdible ? 1 : 0);
            result = 31 * result + maxItemUseDuration;
            result = 31 * result + action.hashCode();
            result = 31 * result + (potion != null ? potion.hashCode() : 0);
            result = 31 * result + (potionEffectProbability != +0.0f ? Float.floatToIntBits(potionEffectProbability) : 0);
            result = 31 * result + (unlocalizedName != null ? unlocalizedName.hashCode() : 0);
            result = 31 * result + (model != null ? model.hashCode() : 0);
            result = 31 * result + (useFinishCallback != null ? useFinishCallback.hashCode() : 0);
            result = 31 * result + (eatenCallback != null ? eatenCallback.hashCode() : 0);
            return result;
        }
    }

    class Storage implements Capability.IStorage<FoodStats> {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<FoodStats> capability, FoodStats instance, EnumFacing side) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setInteger("amount", instance.getAmount());
            tagCompound.setBoolean("wolfFood", instance.isWolfFood());
            tagCompound.setFloat("saturationModifier", instance.getSaturationModifier());
            tagCompound.setBoolean("alwaysEdible", instance.isAlwaysEdible());
            tagCompound.setInteger("maxItemUseDuration", instance.getMaxItemUseDuration());
            tagCompound.setInteger("action", instance.getAction().ordinal());
            if (instance.getPotion() != null)
                tagCompound.setTag("potion", instance.getPotion().writeCustomPotionEffectToNBT(new NBTTagCompound()));
            tagCompound.setFloat("potionEffectProbability", instance.getPotionEffectProbability());
            tagCompound.setString("unlocalizedName", instance.getUnlocalizedName());
            return tagCompound;
        }

        @Override
        public void readNBT(Capability<FoodStats> capability, FoodStats instance, EnumFacing side, NBTBase nbt) {
            if (!(nbt instanceof NBTTagCompound))
                return;
            NBTTagCompound tagCompound = (NBTTagCompound) nbt;
            instance.setAmount(tagCompound.getInteger("amount"))
                    .setSaturationModifier(tagCompound.getFloat("saturationModifier"))
                    .setMaxItemUseDuration(tagCompound.getInteger("maxItemUseDuration"))
                    .setAction(EnumAction.values()[tagCompound.getInteger("action")])
                    .setPotion(PotionEffect.readCustomPotionEffectFromNBT(tagCompound.getCompoundTag("potion")))
                    .setPotionEffectProbability(tagCompound.getFloat("potionEffectProbability"))
                    .setUnlocalizedName(tagCompound.getString("unlocalizedName"));
            if (tagCompound.getBoolean("wolfFood"))
                instance.setWolfFood();
            if (tagCompound.getBoolean("alwaysEdible"))
                instance.setAlwaysEdible();
        }
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    class Provider implements ICapabilitySerializable<NBTTagCompound> {
        private FoodStats stats = new Implementation();

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CAPABILITY_FOOD_STATS;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == CAPABILITY_FOOD_STATS ? (T) stats : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) CAPABILITY_FOOD_STATS.getStorage().writeNBT(CAPABILITY_FOOD_STATS, stats, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            CAPABILITY_FOOD_STATS.getStorage().readNBT(CAPABILITY_FOOD_STATS, stats, null, nbt);
        }
    }
}
