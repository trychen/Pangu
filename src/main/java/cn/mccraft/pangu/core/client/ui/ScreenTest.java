package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.client.ui.Screen;
import cn.mccraft.pangu.core.client.ui.builtin.*;
import cn.mccraft.pangu.core.util.image.TextureProvider;
import cn.mccraft.pangu.core.util.render.Rect;
import com.google.common.collect.Lists;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Random;

import static cn.mccraft.pangu.core.util.render.Rect.randomColor;

public class ScreenTest extends Screen {
    private TextureProvider image = TextureProvider.of("https://ftp.bmp.ovh/imgs/2021/07/f87085b4a867bfe4.gif");
    private static Random RAND = new Random();
    private static long startTime;

//    @BindKeyPress(value = Keyboard.KEY_O, devOnly = true, enableInGUI = true)
    public static void openScreen() {
        new ScreenTest().open();
    }

    @Override
    public void init() {
        startTime = System.currentTimeMillis();

        addComponent(new TextButton("reset").onButtonClick(e -> {
            startTime = System.currentTimeMillis();
        }).setPosition(0, 100));
        addComponent(new Component() {
            @Override
            public void onDraw(float partialTicks, int mouseX, int mouseY) {
                Rect.startDrawing();
                Rect.bind(image, startTime, false);
                Rect.drawFullTexTexturedWithVerticalFlip(getX(), getY(), getWidth(), getHeight());
            }
        }.setSize(100, 100));

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
