package cn.mccraft.pangu.core.client.ui;

import java.util.Collections;
import javax.annotation.Nonnull;
import net.minecraft.util.NonNullList;

public class Container extends Component {
  private NonNullList<Component> components;
  private Focusable focusedComponent;

  public Container(int width, int height) {
    super(width, height);
  }

  public Container addComponents(Component... cs) {
    if (cs.length == 0) return this;
    Collections.addAll(components, cs);
    Collections.sort(components);

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
    components.add(c);
    Collections.sort(components);

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
  public void onDraw(float partialTicks, int mouseX, int mouseY) {
    components.forEach(c -> c.onUpdata(mouseX, mouseY));
    components
        .stream()
        .filter(Component::isVisable)
        .forEach(c -> c.onDraw(partialTicks, mouseX, mouseY));
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
