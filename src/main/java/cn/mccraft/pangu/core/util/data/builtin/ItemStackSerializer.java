package cn.mccraft.pangu.core.util.data.builtin;

import com.trychen.bytedatastream.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;

public enum ItemStackSerializer implements ByteSerializer<ItemStack>, ByteDeserializer<ItemStack> {
    INSTANCE;

    @Override
    public void serialize(DataOutput stream, ItemStack stack) throws IOException {
        if (stack.isEmpty()) {
            stream.writeShort(-1);
        } else {
            stream.writeShort(Item.getIdFromItem(stack.getItem()));
            stream.writeByte(stack.getCount());
            stream.writeShort(stack.getMetadata());
            NBTTagCompound nbttagcompound = null;

            if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
                nbttagcompound = stack.getItem().getNBTShareTag(stack);
            } else nbttagcompound = new NBTTagCompound();

            NBTSerializer.INSTANCE.serialize(stream, nbttagcompound);
        }
    }

    @Override
    public ItemStack deserialize(DataInput in) throws IOException {
        int i = in.readShort();

        if (i < 0) {
            return ItemStack.EMPTY;
        } else {
            int j = in.readByte();
            int k = in.readShort();
            ItemStack itemstack = new ItemStack(Item.getItemById(i), j, k);
            itemstack.setTagCompound(NBTSerializer.INSTANCE.deserialize(in));
            return itemstack;
        }
    }
}
