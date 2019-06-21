package cn.mccraft.pangu.core.client.ui.example;

import cn.mccraft.pangu.core.asm.dev.DevOnly;
import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.client.ui.Screen;
import cn.mccraft.pangu.core.client.ui.UI;
import cn.mccraft.pangu.core.client.ui.meta.Alignment;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@DevOnly
@SideOnly(Side.CLIENT)
public class ScreenExample2 extends Screen {
    public ScreenExample2() {
//        setDebug(true);
    }

    @BindKeyPress(Keyboard.KEY_K)
    public static void test() {
        new ScreenExample2().open();
    }

    @Override
    public void init() {
        addComponent(
                VStack(
                        HStack(UI.text("设置"), HSpacer(30), UI.button("X")),
                        HSpacer(20),
                        HStack(UI.text("爆炸"), HSpacer(30), UI.button("允许")),
                        HStack(UI.text("生成"), HSpacer(30), UI.button("允许")),
                        HStack(UI.text("燃烧"), HSpacer(30), UI.button("允许")),
                        HSpacer(20),
                        HStack(UI.button("完成"))
                ).padding(4).setCenteredPosition(halfWidth, halfHeight)
        );
        addComponent(UI.selection("Hello"));
//        addComponent(
//                VStack(
//                        HStack(
//                                UI.ofYesButton(), Spacer(50), VStack(UI.ofYesButton(), UI.ofNoButton())
//                        ).alignment(Alignment.LEADING),
//
//                        UI.ofNoButton()
//                ).padding(5).setCenteredPosition(halfWidth, halfHeight)
//        );
    }
}
