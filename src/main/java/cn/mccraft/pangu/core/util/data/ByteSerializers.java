package cn.mccraft.pangu.core.util.data;

import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.util.data.builtin.ItemStackSerializer;
import cn.mccraft.pangu.core.util.data.builtin.NBTSerializer;
import cn.mccraft.pangu.core.util.data.builtin.TextComponentSerializer;
import com.trychen.bytedatastream.ByteSerialization;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public interface ByteSerializers {
    @Load
    static void register() {
        ByteSerialization.register(BlockPos.class, (out, obj) -> obj.toLong(), in -> BlockPos.fromLong(in.readLong()));
        ByteSerialization.register(ItemStack.class, ItemStackSerializer.INSTANCE, ItemStackSerializer.INSTANCE);
        ByteSerialization.register(NBTTagCompound.class, NBTSerializer.INSTANCE, NBTSerializer.INSTANCE);
        ByteSerialization.register(ITextComponent.class, TextComponentSerializer.INSTANCE, TextComponentSerializer.INSTANCE);
    }
}
