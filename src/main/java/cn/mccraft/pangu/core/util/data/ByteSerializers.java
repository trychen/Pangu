package cn.mccraft.pangu.core.util.data;

import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.util.data.builtin.*;
import com.trychen.bytedatastream.ByteSerialization;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import java.awt.image.BufferedImage;

public interface ByteSerializers {
    @Load
    static void register() {
        ByteSerialization.register(ResourceLocation.class, ResourceLocationSerializer.INSTANCE, ResourceLocationSerializer.INSTANCE);
        ByteSerialization.register(BlockPos.class, (out, obj) -> obj.toLong(), in -> BlockPos.fromLong(in.readLong()));
        ByteSerialization.register(ItemStack.class, ItemStackSerializer.INSTANCE, ItemStackSerializer.INSTANCE);
        ByteSerialization.register(NBTTagCompound.class, NBTSerializer.INSTANCE, NBTSerializer.INSTANCE);
        ByteSerialization.register(ITextComponent.class, TextComponentSerializer.INSTANCE, TextComponentSerializer.INSTANCE);
        ByteSerialization.register(BufferedImage.class, ImageSerializer.INSTANCE, ImageSerializer.INSTANCE);
        ByteSerialization.register(SoundEvent.class, SoundEventSerializer.INSTANCE, SoundEventSerializer.INSTANCE);
    }
}
