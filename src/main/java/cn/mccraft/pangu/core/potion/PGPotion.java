package cn.mccraft.pangu.core.potion;

import cn.mccraft.pangu.core.client.gui.Icon;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @since 1.0.6
 * @author trychen
 */
public class PGPotion extends Potion {
    @SideOnly(Side.CLIENT)
    private Icon icon;

    public PGPotion(boolean isBadEffect, int liquidColor) {
        super(isBadEffect, liquidColor);
    }

    @SideOnly(Side.CLIENT)
    public PGPotion setIcon(Icon icon) {
        this.icon = icon;
        this.setIconIndex(icon.getOffsetX(), icon.getOffsetY());
        return this;
    }

    @Override
    public int getStatusIconIndex() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(icon.getTexture());
        return super.getStatusIconIndex();
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return false;
    }
}
