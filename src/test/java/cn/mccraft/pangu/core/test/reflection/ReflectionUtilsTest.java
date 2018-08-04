package cn.mccraft.pangu.core.test.reflection;

import cn.mccraft.pangu.core.util.ReflectUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionUtilsTest {
    @Test
    public void testClass() {
        assertEquals(SimpleChild.class, ReflectUtils.forName("cn.mccraft.pangu.core.test.reflection.SimpleChild"));
        assertNull(ReflectUtils.forName("cn.mccraft.pangu.core.test.reflection.FakeSimpleChild"));
        assertNull(ReflectUtils.forInstance(SimpleParent.class));
    }

    @Test
    public void testInterface() {
        SimpleChild instance = ReflectUtils.forInstance(SimpleChild.class);

        assertNotNull(instance);

        assertEquals(1, (int) ReflectUtils.invokeMethod(instance, "returnOne"));
        assertEquals(1, (int) ReflectUtils.invokeMethod(instance, "returnOne", int.class));
        assertEquals(2, (int) ReflectUtils.invokeMethod(instance, "returnTwo", int.class));

        assertEquals("SFF", ReflectUtils.getField(instance, "SFF", String.class));
        assertEquals("SFF", ReflectUtils.getField(SimpleInterface.class, "SFF", String.class));

        assertFalse(ReflectUtils.setField(SimpleInterface.class, "SFF", "Hello"));
    }
    @Test
    public void testPrivate() {
        SimpleChild instance = ReflectUtils.forInstance(SimpleChild.class);

        assertNotNull(instance);

        assertNull(ReflectUtils.getField(instance, "hello"));
        assertNotNull(ReflectUtils.getField(instance, "hello"));
    }
}
