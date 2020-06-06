package cn.mccraft.pangu.core.test.utils;

import cn.mccraft.pangu.core.util.render.Rect;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectTest {
    @Test
    public void testColorGet() {
        assertEquals(0xFF, Rect.alphaInt(0xFF000000));
        assertEquals(0xAB, Rect.alphaInt(0xABCD1200));
        assertEquals(0xAB, Rect.alphaInt(0xAB0A00B0));

        assertEquals(0xAC, Rect.redInt(0xFFACDE11));
        assertEquals(0xCD, Rect.redInt(0xABCD1200));
        assertEquals(0x0A, Rect.redInt(0xAB0A00B0));
    }
    @Test
    public void testColorSet() {
        assertEquals(0x11AD0000, Rect.alpha(0xFFAD0000, 0x00000011));
        assertEquals(0xFFAD0000, Rect.red(0xFF000000, 0xAD));
        assertEquals(0xFF00CD00, Rect.green(0xFF00AB00, 0xCD));
        assertEquals(0xFFADCD12, Rect.blue(0xFFADCD56, 0x12));
    }
}
