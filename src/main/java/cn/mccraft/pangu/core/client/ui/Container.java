package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.util.NonNullList;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * Container that stored components
 */
@Accessors(chain = true)
public class Container extends Component {
    @Getter
    @Setter
    protected NonNullList<Component> components = NonNullList.create();

    @Getter
    @Setter
    protected Focusable focusedComponent;

    public Container(int width, int height) {
        super();
        setSize(width, height);
    }

    public Container addComponents(Component... cs) {
        if (cs.length == 0) return this;

        // Set parents
        for (Component c : cs) c.setParent(this).setScreen(getScreen());

        // Add component
        Collections.addAll(components, cs);

        // Sort with Z Index
        Collections.sort(components);

        // Check focus
        if (focusedComponent == null) {
            for (Component c : cs) {
                if (c instanceof Focusable) {
                    focus((Focusable) c);
                }
            }
        }

        return this;
    }

    public Container addComponent(@Nonnull Component c) {
        components.add(c.setScreen(getScreen()));
        Collections.sort(components);
        c.setParent(this);

        if (focusedComponent == null && c instanceof Focusable) {
            focus((Focusable) c);
        }

        return this;
    }

    public Container focus(@Nonnull Focusable c) {
        if (focusedComponent != null) focusedComponent.onLostFocus();
        focusedComponent = c;
        focusedComponent.onFocused();
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        components.forEach(c -> c.onUpdate(mouseX, mouseY));
        components
                .stream()
                .filter(Component::isVisible)
                .forEach(c -> c.onDraw(partialTicks, mouseX, mouseY));

        // draw tooltips
        for (Component c : components) {
            if (!c.isHovered()) continue;
            List<String> toolTip = c.getToolTips();
            if (toolTip != null) {
                drawToolTips(toolTip, mouseX, mouseY);
                return;
            }
        }
    }

    @Override
    public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
        components
                .stream()
                .filter(Component::isHovered)
                .forEach(
                        c -> {
                            if (c != focusedComponent && c instanceof Focusable) {
                                focus((Focusable) c);
                            }
                            c.onMousePressed(mouseButton, mouseX, mouseY);
                        });
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY) {
        components
                .stream()
                .filter(Component::isHovered)
                .forEach(c -> c.onMouseReleased(mouseX, mouseY));
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        components.stream().filter(c -> !c.isDisabled()).forEach(c -> c.onKeyTyped(typedChar, keyCode));
    }
}
