package cn.mccraft.pangu.core.client.ui;

import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TabContainer extends Component {
    @Getter
    private List<Container> containers = new ArrayList<>();

    @Getter
    private int index = 0;

    @Getter
    private Container currentContainer;

    @Getter
    @Setter
    private TabSelectEvent tabSelectEvent;

    public TabContainer(int width, int height) {
        this(width, height, new Container[0]);
    }

    public TabContainer(int width, int height, Container... containers) {
        setSize(width, height);
        addTabs(containers);
    }

    public TabContainer addTab(Container container) {
        container.setOffset(getX(), getY());
        containers.add(container);
        if (currentContainer == null) {
            select(0);
        }
        return this;
    }

    public TabContainer addTabs(Container... containers) {
        for (Container container : containers) addTab(container);
        return this;
    }

    public TabContainer select(Container container) {
        select(containers.indexOf(container));
        return this;
    }

    public TabContainer select(int index) {
        if (index < 0 || containers.size() <= index) throw new IndexOutOfBoundsException();
        if (tabSelectEvent != null) tabSelectEvent.onTabSelect(index, this.index);
        this.index = index;
        currentContainer = containers.get(index);
        return this;
    }

    public TabContainer remove(Container container) {
        remove(containers.indexOf(container));
        return this;
    }

    public TabContainer remove(int index) {
        if (this.index == index) select(index == 0 ? 1 : index - 1);
        containers.remove(index);
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        if (currentContainer == null) return;
        currentContainer.onDraw(partialTicks, mouseX, mouseY);
    }

    @Override
    public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
        if (currentContainer == null) return;
        currentContainer.onMousePressed(mouseButton, mouseX, mouseY);
    }

    @Override
    public void onMouseReleased(int mouseButton, int mouseX, int mouseY) {
        if (currentContainer == null) return;
        currentContainer.onMouseReleased(mouseButton, mouseX, mouseY);
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (currentContainer == null) return;
        currentContainer.onKeyTyped(typedChar, keyCode);
    }

    @Override
    public void onMouseInput(int mouseX, int mouseY) {
        if (currentContainer == null) return;
        currentContainer.onMouseInput(mouseX, mouseY);
    }

    interface TabSelectEvent {
        void onTabSelect(int newIndex, int oldIndex);
    }
}
