package cn.mccraft.pangu.core.client.ui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface Focusable {
    void onFocused();

    void onLostFocus();
}
