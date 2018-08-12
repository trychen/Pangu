package cn.mccraft.pangu.core.capability.color;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface CapabilityColor {
    @CapabilityInject(ColorStats.class)
    Capability<ColorStats> COLOR_STATS = null;

    class Implementation implements ColorStats {
        private int color;

        @Override
        public int getColor() {
            return color;
        }

        @Override
        public void setColor(int color) {
            this.color = color;
        }
    }

    class Storage implements Capability.IStorage<ColorStats> {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<ColorStats> capability, ColorStats instance, EnumFacing side) {
            return new NBTTagInt(instance.getColor());
        }

        @Override
        public void readNBT(Capability<ColorStats> capability, ColorStats instance, EnumFacing side, NBTBase nbt) {
            if (nbt.getId() == 3) instance.setColor(((NBTPrimitive) nbt).getInt());
        }
    }

    class Provider implements ICapabilitySerializable<NBTTagInt> {
        private ColorStats colorStats = new Implementation();

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == COLOR_STATS;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == COLOR_STATS ? (T) colorStats : null;
        }

        @Override
        public NBTTagInt serializeNBT() {
            return (NBTTagInt) COLOR_STATS.writeNBT(colorStats, null);
        }

        @Override
        public void deserializeNBT(NBTTagInt nbt) {
            COLOR_STATS.readNBT(colorStats, null, nbt);
        }
    }
}
