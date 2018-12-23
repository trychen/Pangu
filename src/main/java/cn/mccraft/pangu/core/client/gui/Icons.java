package cn.mccraft.pangu.core.client.gui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static cn.mccraft.pangu.core.client.PGClient.PG_ICONS_TEXTURE;

@SideOnly(Side.CLIENT)
public interface Icons {
    Icon NONE = Icon.of(PG_ICONS_TEXTURE, 0, 0, 0);
    Icon DOT_GREEN = Icon.of(PG_ICONS_TEXTURE);
    Icon POINTER_RIGHT = Icon.of(PG_ICONS_TEXTURE, 0, 11);
}
