package cn.mccraft.pangu.core.client.ui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import java.util.List;

public class Container extends Component {
    public List<Container> containers = Lists.newArrayList();

    /**
     * Drawing this component
     *
     * @param minecraft
     * @param mouseX
     * @param mouseY
     * @param partialTicks
     */
    @Override
    public void draw(@Nonnull Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {

    }
}
