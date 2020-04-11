package cn.mccraft.pangu.core.util.data.builtin;

import com.google.gson.*;
import com.trychen.bytedatastream.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public enum ItemStackSerializer implements ByteSerializer<ItemStack>, ByteDeserializer<ItemStack>, JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
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

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject element = json.getAsJsonObject();

        Item id = Item.getByNameOrId(element.get("id").getAsString());
        if (id == null) {
            return ItemStack.EMPTY;
        }
        int amount = element.has("amount") ? element.get("amount").getAsByte() : 1;
        int meta = element.has("meta") ? element.get("meta").getAsShort() : 0;
        ItemStack itemStack = new ItemStack(id, amount, meta);

        if (element.has("nbt"))
            itemStack.setTagCompound(context.deserialize(element.get("nbt"), NBTTagCompound.class));
        if (element.has("nbt-json")) try {
            itemStack.setTagCompound(JsonToNBT.getTagFromJson(element.getAsJsonObject("nbt-json").toString()));
        } catch (NBTException e) {
            e.printStackTrace();
        }

        return itemStack;
    }

    @Override
    public JsonElement serialize(ItemStack src, Type type, JsonSerializationContext context) {
        Map<String, Object> map = new HashMap<>();
        if (src.isEmpty()) {
            map.put("id", -1);
        } else {
            map.put("id", Item.getIdFromItem(src.getItem()));
            map.put("amount", src.getCount());
            map.put("meta", src.getMetadata());

            NBTTagCompound nbt = null;
            if (src.getItem().isDamageable() || src.getItem().getShareTag()) {
                nbt = src.getItem().getNBTShareTag(src);
                map.put("nbt", nbt);
            }
        }
        return context.serialize(map);
    }
}
