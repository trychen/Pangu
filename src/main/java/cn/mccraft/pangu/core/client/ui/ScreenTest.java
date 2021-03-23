package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.client.ui.Screen;
import cn.mccraft.pangu.core.client.ui.builtin.ColorPane;
import cn.mccraft.pangu.core.client.ui.builtin.LiteSideBarScrollingContainer;
import cn.mccraft.pangu.core.client.ui.builtin.Waterfall;
import com.google.common.collect.Lists;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Random;

import static cn.mccraft.pangu.core.util.render.Rect.randomColor;

public class ScreenTest extends Screen {

    private static Random RAND = new Random();

//    @BindKeyPress(value = Keyboard.KEY_O, devOnly = true)
//    public static void openScreen() {
//        new ScreenTest().open();
//    }

    @Override
    public void init() {
        Waterfall waterfall = new Waterfall(20, 5, 5);
        waterfall.setPaddingY(5);

        waterfall.addComponent(new ColorPane(randomColor()).setSize(20, RAND.nextInt(60) + 10));
        waterfall.addComponent(new ColorPane(randomColor()).setSize(20, RAND.nextInt(60) + 10));
        waterfall.addComponent(new ColorPane(randomColor()).setSize(20, RAND.nextInt(60) + 10));
        waterfall.addComponent(new ColorPane(randomColor()).setSize(20, RAND.nextInt(60) + 10));
        waterfall.addComponent(new ColorPane(randomColor()).setSize(20, RAND.nextInt(60) + 10));
        waterfall.addComponent(new ColorPane(randomColor()).setSize(20, RAND.nextInt(60) + 10));
        waterfall.addComponent(new ColorPane(randomColor()).setSize(20, RAND.nextInt(60) + 10));
        waterfall.addComponent(new ColorPane(randomColor()).setSize(20, RAND.nextInt(60) + 10));
        waterfall.addComponent(new ColorPane(randomColor()).setSize(20, RAND.nextInt(60) + 10));
        waterfall.addComponent(new ColorPane(randomColor()).setSize(20, RAND.nextInt(60) + 10));
        waterfall.addComponent(new ColorPane(randomColor()).setSize(20, RAND.nextInt(60) + 10));
        waterfall.addComponent(new ColorPane(randomColor()).setSize(20, RAND.nextInt(60) + 10));
        waterfall.addComponent(new ColorPane(randomColor()).setSize(20, RAND.nextInt(60) + 10));

        waterfall.rectifyHeight();

        addComponent(new LiteSideBarScrollingContainer(waterfall, 50).setCanScrollOnContent(false).setCenteredPosition(halfWidth, halfHeight));
    }
}
