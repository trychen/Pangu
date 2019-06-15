package cn.mccraft.pangu.core.client.ui.stack;

import cn.mccraft.pangu.core.client.ui.Component;

public class Spacer extends Component {
    public Spacer(float width, float height) {
        setSize(width, height);
    }

    public static Spacer of(float width, float height) {
        return new Spacer(width, height);
    }
}
