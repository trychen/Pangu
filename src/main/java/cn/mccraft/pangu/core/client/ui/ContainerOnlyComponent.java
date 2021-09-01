package cn.mccraft.pangu.core.client.ui;

import lombok.Data;

@Data
public class ContainerOnlyComponent extends Component {
    protected ScreenContainer container;

    @Override
    public Component setScreen(Screen screen) {
        if (screen == null)
            return super.setScreen(screen);

        if (!(screen instanceof ScreenContainer)) {
            throw new RuntimeException("Component only support with ScreenContainer " + getClass().getName());
        }

        this.container = (ScreenContainer) screen;
        return super.setScreen(screen);
    }
}
