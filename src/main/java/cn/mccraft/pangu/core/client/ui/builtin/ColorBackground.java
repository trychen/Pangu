package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.util.render.Rect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ColorBackground extends Component {
    public ColorBackground() {
        super();
        setZLevel(-1);
    }

    @Override
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        Rect.drawGradient(0, 0, (parent.getWidth() * 2) / 3, (parent.getHeight() * 2) / 2, 0xBB000000, 0x00000000, 0xBB000000, 0x00000000);
        Rect.draw(0, 0, parent.getWidth(), parent.getHeight(), 0x33000000);
    }
}
