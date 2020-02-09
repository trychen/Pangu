package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.util.NonNullList;
import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Container that stored components
 */
@Accessors(chain = true)
@ToString
public class Container extends Component implements Cloneable {
    @Getter
    @Setter
    protected NonNullList<Component> components = NonNullList.create();


    @Setter
    protected float offsetX, offsetY;

    @Getter
    @Setter
    protected Component debugSelectedComponent;

    @Getter
    @Setter
    protected Component debugMovingComponent;

    @Getter
    @Setter
    protected float debugMovingComponentOffsetX, debugMovingComponentOffsetY;

    public Container(float width, float height) {
        super();
        setSize(width, height);
    }

    public Container addComponents(Component... cs) {
        if (cs.length == 0) return this;

        // Set parents
        for (Component c : cs) {
            c.setParent(this).setScreen(getScreen());
        }

        // Add component
        Collections.addAll(getComponents(), cs);

        // Sort with Z Index
        Collections.sort(getComponents());

        return this;
    }

    public Container setOffset(float x, float y) {
        setOffsetX(x); setOffsetY(y);
        return this;
    }

    public Container addComponent(@Nonnull Component c) {
        getComponents().add(c.setParent(this).setScreen(getScreen()));
        Collections.sort(getComponents());
        return this;
    }

    public Component focus(@Nonnull Component c) {
        if (getScreen() == null) return c;
        if (getScreen().getFocusedComponent() != null) getScreen().getFocusedComponent().setFocused(false);
        c.setFocused(true);
        getScreen().setFocusedComponent(c);
        return c;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        drawBackground();
        if (getScreen() != null && getScreen().isDebug() && getDebugMovingComponent() != null) {
            getDebugMovingComponent().setPosition(mouseX + getDebugMovingComponentOffsetX(), mouseY + getDebugMovingComponentOffsetY());
        }
        // update information
        onUpdate(mouseX, mouseY);
        getComponents().forEach(c -> c.onUpdate(mouseX, mouseY));

        if (getParent() instanceof TransformHover) {
            setHovered(getParent().isHovered());
        }

        // draw component
        Component debugComponent = getDebugSelectedComponent();
        getComponents()
                .stream()
                .filter(Component::isVisible)
                .forEach(c -> {
                    c.onDraw(partialTicks, mouseX, mouseY);

                    if (getScreen() != null) {
                        // draw tooltips
                        if (c.isHovered()) {
                            List<String> toolTip = c.getToolTips();
                            if (toolTip != null)
                                getScreen().setTooltips2Render(c);
                        }

                        if (getScreen().isDebug() && getDebugMovingComponent() != c)
                        c.drawComponentBox(debugComponent == c ? 0xFF00FF00 : 0xFFFF0000);
                    }
                });


        drawForeground();

        // debug info
        if (getScreen() != null && getScreen().isDebug()) {
            if (getScreen().rootContainer != this) drawComponentBox(0xFFAA0000);
            if (getDebugSelectedComponent() != null) {
                drawComponentDebugInfo(mouseX, mouseY, debugComponent);
            }
        }
    }

    private void drawComponentDebugInfo(int mouseX, int mouseY, Component debugC) {
        String lineOne = String.format("(x,y)=%d,%d", (int) debugC.getX(), (int) debugC.getY());
        String lineTwo = String.format("(x,y)-(halfWidth,halfHeight)=%d,%d", (int) (debugC.getX() - getScreen().getHalfWidth()) , (int) (debugC.getY() - getScreen().getHalfHeight()));
        DefaultFontProvider.INSTANCE.drawString(lineOne, mouseX, mouseY - 34, 0x7ed321, true);
        DefaultFontProvider.INSTANCE.drawString(lineTwo, mouseX, mouseY - 26, 0x7ed321, true);
    }

    @Override
    public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
        AtomicBoolean componentClicked = new AtomicBoolean(false);

        getComponents()
                .stream()
                .filter(Component::isHovered)
                .forEach(
                        c -> {
                            componentClicked.set(true);

                            // debug
                            if (getScreen() != null && getScreen().isDebug()) {
                                if (getDebugSelectedComponent() == c) {
                                    if (c instanceof Container) {
                                        c.onMousePressed(mouseButton, mouseX, mouseY);
                                        setDebugMovingComponent(((Container) c).getDebugMovingComponent());
                                    }
                                    if (getDebugMovingComponent() == null) setDebugMovingComponent(c);
                                    setDebugMovingComponentOffsetX(getDebugMovingComponent().getX() - mouseX);
                                    setDebugMovingComponentOffsetY(getDebugMovingComponent().getY() - mouseY);
                                    return;
                                }
                                setDebugSelectedComponent(c);
                            }

                            if (getScreen() != null && c != getScreen().getFocusedComponent() && c instanceof Focusable) {
                                focus(c);
                            }
                            c.onMousePressed(mouseButton, mouseX, mouseY);
                        });

        if (!componentClicked.get()) {
            setDebugSelectedComponent(null);
            setDebugMovingComponent(null);
        }
    }

    public float getOffsetX() {
        if (parent instanceof Container) return ((Container) parent).getOffsetX() + offsetX;
        return offsetX;
    }

    public float getOffsetY() {
        if (parent instanceof Container) return ((Container) parent).getOffsetY() + offsetY;
        return offsetY;
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY) {
        setDebugMovingComponent(null);
        if (getScreen() != null && getScreen().isDebug()) return;
        getComponents()
                .stream()
                .filter(Component::isHovered)
                .forEach(c -> c.onMouseReleased(mouseX, mouseY));
    }


    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (getScreen() != null && getScreen().isDebug()) {
            if (getDebugSelectedComponent() != null) {
                float x = getDebugSelectedComponent().getX(), y = getDebugSelectedComponent().getY();
                switch (keyCode) {
                    case Keyboard.KEY_UP:
                        y--;
                        break;
                    case Keyboard.KEY_DOWN:
                        y++;
                        break;
                    case Keyboard.KEY_LEFT:
                        x--;
                        break;
                    case Keyboard.KEY_RIGHT:
                        x++;
                        break;
                }
                getDebugSelectedComponent().setPosition(x, y);
            }
            return;
        }
        getComponents().stream().filter(c -> !c.isDisabled()).forEach(c -> c.onKeyTyped(typedChar, keyCode));
    }

    @Override
    public void onMouseInput(int mouseX, int mouseY) {
        getComponents()
                .stream()
                .filter(Component::isHovered)
                .forEach(c -> c.onMouseInput(mouseX, mouseY));
    }

    public void clear() {
        getComponents().clear();
        setDebugSelectedComponent(null);
        setDebugMovingComponent(null);
    }

    public void drawBackground() {
    }

    public void drawForeground() {
    }

    @Override
    public Container setScreen(Screen screen) {
        for (Component component : components) component.setScreen(screen);
        super.setScreen(screen);
        return this;
    }
}