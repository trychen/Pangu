package cn.mccraft.pangu.core.client.ui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class Button extends Component {

  public Button(int width, int height) {
    super(width, height);
  }

  public abstract void onClick(int mouseButton, int mouseX, int mouseY);

  @Override
  public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
    if (actionOnReleased(mouseX, mouseY)) return;
    this.onClick(mouseButton, mouseX, mouseY);
  }

  @Override
  public void onMouseReleased(int mouseX, int mouseY) {
    if (!actionOnReleased(mouseX, mouseY)) return;
    this.onClick(0, mouseX, mouseY);
  }

  public boolean actionOnReleased(int mouseX, int mouseY) {
    return true;
  }
}
