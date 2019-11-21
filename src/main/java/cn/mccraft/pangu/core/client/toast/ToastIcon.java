package cn.mccraft.pangu.core.client.toast;

import cn.mccraft.pangu.core.client.PGClient;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum ToastIcon {
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

    ToastIcon(int columnIn, int rowIn) {
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
