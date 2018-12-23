package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.util.render.Rect;
import javax.annotation.Nullable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class Component implements Cloneable, Comparable<Component> {
  private Component parent;
  private int zLevel;
  private int height, width;
  private float x = 0, y = 0;
  private boolean hovered, visable, disabled;

  public Component(int width, int height) {
    this.height = height;
    this.width = width;
  }

  public void onDraw(float partialTicks, int mouseX, int mouseY) {}

  public void onMousePressed(int mouseButton, int mouseX, int mouseY) {}

  public void onMouseReleased(int mouseX, int mouseY) {}

  public void onKeyTyped(char typedChar, int keyCode) {}

  public void onUpdata(int mouseX, int mouseY) {
    this.hovered = isHovered(mouseX, mouseY);
  }

  public boolean isHovered(int mouseX, int mouseY) {
    return mouseX >= this.x
        && mouseY >= this.y
        && mouseX < this.x + this.width
        && mouseY < this.y + this.height;
  }

  public boolean isHovered() {
    return hovered;
  }

  public boolean isVisable() {
    return visable;
  }

  public boolean isDisabled() {
    return disabled;
  }

  public int getZLevel() {
    return zLevel;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public Component setPosition(float x, float y) {
    this.x = x;
    this.y = y;
    return this;
  }

  public Component setSize(int width, int height) {
    this.width = width;
    this.height = height;
    return this;
  }

  @Nullable
  public NonNullList<String> getToolTip() {
    return null;
  }

  public void drawComponentBox() {
    Rect.drawBox(x, y, width, height, 0xFFFFFFFF);
  }

  @Override
  public int compareTo(Component o) {
    return Integer.compare(this.getZLevel(), o.getZLevel());
  }
}
