package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.client.input.BindKeyPress;
import org.lwjgl.input.Keyboard;

public interface Test {
    @Bridge
    static void test(String name) {
        System.out.println(name);
    }

    @BindKeyPress(Keyboard.KEY_J)
    static void test() {
        test("Hello");
    }
}
