package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.client.input.BindKeyPress;
import org.lwjgl.input.Keyboard;

public class RemoteTest {
    @Remote(100)
    public static void test(String test, float flo) {
        System.out.println("From local");
    }

    @BindKeyPress(description = "key.test", keyCode = Keyboard.KEY_O)
    public static void key() {
        test("Hello World", 1.1153F);
    }
}
