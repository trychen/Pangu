package cn.mccraft.pangu.core.client.ui.container;

import cn.mccraft.pangu.core.client.ui.ContainerOnlyComponent;
import net.minecraft.inventory.ClickType;

public class ThrownArea extends ContainerOnlyComponent {
    @Override
    public void onMouseReleased(int mouseButton, int mouseX, int mouseY) {
        getContainer().handleMouseClick(null, -999, mouseButton, ClickType.PICKUP);
        getContainer().setLastClickTime(0);
        getContainer().setDragSplitting(false);
    }
}
