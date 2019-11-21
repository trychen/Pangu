package cn.mccraft.pangu.core.client.toast;

import com.trychen.bytedatastream.ByteDeserializable;
import com.trychen.bytedatastream.ByteSerializable;
import com.trychen.bytedatastream.DataInput;
import com.trychen.bytedatastream.DataOutput;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class ToastData implements ByteSerializable, ByteDeserializable {
    @NonNull
    private String title;
    private String subtitle = "";
    private String key = "";

    private int duration = 5000;
    private ToastStyle style = ToastStyle.DARK;
    private ToastIcon icon = ToastIcon.RECIPE_BOOK;
    private List<ItemStack> itemStacks = new ArrayList<>();
    private boolean progress = false;
    private boolean isCustomProgress = false;

    @Override
    public void serialize(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(title);
        dataOutput.writeUTF(subtitle);
        dataOutput.writeUTF(key);
        dataOutput.writeInt(duration);
        dataOutput.writeList(itemStacks, ItemStack.class);
        dataOutput.writeBoolean(progress);
        dataOutput.writeBoolean(isCustomProgress);
        dataOutput.writeEnum(style);
        dataOutput.writeEnum(icon);
    }

    public static ToastData deserialize(DataInput in) throws IOException {
        return new ToastData(in.readUTF())
                .setSubtitle(in.readUTF())
                .setKey(in.readUTF())
                .setDuration(in.readInt())
                .setItemStacks(in.readList(ItemStack.class))
                .setProgress(in.readBoolean())
                .setCustomProgress(in.readBoolean())
                .setStyle(ToastStyle.valueOf(in.readUTF()))
                .setIcon(ToastIcon.valueOf(in.readUTF()));
    }

}
