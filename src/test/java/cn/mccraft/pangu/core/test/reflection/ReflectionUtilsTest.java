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

        assertNull(ReflectUtils.getField(SimpleParent.class, instance, "hello", null, false));
        assertEquals("Hello~", ReflectUtils.getField(SimpleParent.class, instance, "hello", null, true));

        assertEquals("HelloWorld", ReflectUtils.invokeMethod(instance, "sayHello"));
        assertNull(ReflectUtils.invokeMethod(instance, "sayPrivateHello"));
        assertEquals("HelloPrivate", ReflectUtils.invokeMethod(instance.getClass(), instance, "sayPrivateHello", null, true));
    }

    @Test
    public void testFieldSet() {
        SimpleChild instance = ReflectUtils.forInstance(SimpleChild.class);

        assertNotNull(instance);

        assertEquals(1, ReflectUtils.getField(instance, "field"));
        assertEquals(Integer.valueOf(-1), ReflectUtils.getField(instance.getClass(), instance, "privateField", int.class, true));

        assertTrue("Unable to set value", ReflectUtils.setField(instance, "field", 2));
        assertEquals(2, ReflectUtils.getField(instance, "field"));
        
        assertTrue("Unable to set value for private field", ReflectUtils.setField(instance.getClass(), instance, "privateField", -2, true));
        assertEquals(Integer.valueOf(-2), ReflectUtils.getField(instance.getClass(), instance, "privateField", int.class, true));

    }
}
