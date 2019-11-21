package cn.mccraft.pangu.core.client.toast;

import cn.mccraft.pangu.core.client.PGClient;
import cn.mccraft.pangu.core.util.render.Rect;
import com.trychen.bytedatastream.ByteDeserializable;
import com.trychen.bytedatastream.ByteSerializable;
import com.trychen.bytedatastream.DataInput;
import com.trychen.bytedatastream.DataOutput;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    private Style style = Style.DARK;
    private Icon icon = Icon.RECIPE_BOOK;
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
                .setStyle(in.readEnum(Style.class))
                .setIcon(in.readEnum(Icon.class));
    }

    public enum Style {
        DARK(0, -256, -1),
        WHITE(1, -11534256, -16777216),
        PRIMARY_WARNING(2, -256, -1),
        WHITE_RECTANGLE(3, -11534256, -16777216);

        @Getter
        private final int index, titleColor, subtitleColor;

        Style(int index, int titleColor, int subtitleColor) {
            this.index = index;
            this.titleColor = titleColor;
            this.subtitleColor = subtitleColor;
        }

        @SideOnly(Side.CLIENT)
        public void draw() {
            Rect.bind(IToast.TEXTURE_TOASTS);
            Rect.drawTextured(0, 0, 0, this.index * 32, 160, 32);
        }
    }

    public enum Icon {
        MOVEMENT_KEYS(0, 0),
        MOUSE(1, 0),
        TREE(2, 0),
        RECIPE_BOOK(0, 1),
        WOODEN_PLANKS(1, 1),
        COMPASS(2, 1),
        CLOCK(3, 1),
        PLAYER_PERL(0, 2),
        FORBIDDEN(1, 2),
        EARTH(2, 2),
        NEW(3, 2),
        NONE(-1, -1) {
            @Override
            public void draw(int x, int y) {
            }
        };

        @Getter
        private final int column, row;

        Icon(int columnIn, int rowIn) {
            this.column = columnIn;
            this.row = rowIn;
        }

        /**
         * Draws the icon at the specified position in the specified Gui
         */
        @SideOnly(Side.CLIENT)
        public void draw(int x, int y) {
            Rect.bind(PGClient.PG_TOAST_ICON_TEXTURE);
            Rect.drawTextured(x, y, this.column * 20, this.row * 20, 20, 20);
        }
    }
}
