package cn.mccraft.pangu.core.test.registry;

import cn.mccraft.pangu.core.util.NameBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class NameBuilderTest {
    @Test
    public void buildRegistryName() {
        assertEquals("hello", NameBuilder.buildRegistryName("hello"));
        assertEquals("hello_world", NameBuilder.buildRegistryName("hello", "world"));
        assertEquals("hello_minecraft_world", NameBuilder.buildRegistryName("hello", "minecraft", "world"));
        assertEquals("hello_world", NameBuilder.buildRegistryName("hello", "WORLD"));
        assertEquals("hello_world", NameBuilder.buildRegistryName("Hello", "World"));
        assertEquals("a", NameBuilder.buildRegistryName("a"));
    }

    @Test
    public void buildUnlocalizedName() {
        assertEquals("hello", NameBuilder.buildTranslationKey("hello"));
        assertEquals("helloWorld", NameBuilder.buildTranslationKey("hello", "world"));
        assertEquals("helloMinecraftWorld", NameBuilder.buildTranslationKey("hello", "minecraft", "world"));
        assertEquals("helloWorld", NameBuilder.buildTranslationKey("hello", "WORLD"));
        assertEquals("helloWorld", NameBuilder.buildTranslationKey("Hello", "World"));
        assertEquals("a", NameBuilder.buildTranslationKey("a"));
    }

    @Test
    public void humpToUnderline() {
        assertEquals("hello", NameBuilder.humpToUnderline("hello"));
        assertEquals("world", NameBuilder.humpToUnderline("World"));
        assertEquals("hello_world", NameBuilder.humpToUnderline("helloWorld"));
        assertEquals("hello_world", NameBuilder.humpToUnderline("HelloWorld"));
    }

    @Test
    public void apartHump() {
        assertArrayEquals(new String[]{"hello"}, NameBuilder.apartHump("hello"));
        assertArrayEquals(new String[]{"hello"}, NameBuilder.apartHump("Hello"));
        assertArrayEquals(new String[]{"hello", "world"}, NameBuilder.apartHump("helloWorld"));
        assertArrayEquals(new String[]{"hello", "world"}, NameBuilder.apartHump("HelloWorld"));
        assertArrayEquals(new String[]{"hello", "hump", "world"}, NameBuilder.apartHump("helloHumpWorld"));
    }

    @Test
    public void apartUnderline() {
        assertArrayEquals(new String[]{"hello"}, NameBuilder.apartUnderline("hello"));
        assertArrayEquals(new String[]{"hello"}, NameBuilder.apartUnderline("Hello"));
        assertArrayEquals(new String[]{"hello", "world"}, NameBuilder.apartUnderline("hello_world"));
        assertArrayEquals(new String[]{"hello", "world"}, NameBuilder.apartUnderline("hello_World"));
        assertArrayEquals(new String[]{"hello", "world"}, NameBuilder.apartUnderline("Hello_World"));
        assertArrayEquals(new String[]{"hello", "hump", "world"}, NameBuilder.apartUnderline("hello_hump_world"));
    }

    @Test
    public void autoApart() {
        assertArrayEquals(new String[]{"hello"}, NameBuilder.apart("hello"));
        assertArrayEquals(new String[]{"hello"}, NameBuilder.apart("Hello"));
        assertArrayEquals(new String[]{"hello", "world"}, NameBuilder.apart("hello_world"));
        assertArrayEquals(new String[]{"hello", "world"}, NameBuilder.apart("helloWorld"));
        assertArrayEquals(new String[]{"hello", "world"}, NameBuilder.apart("HelloWorld"));
        assertArrayEquals(new String[]{"hello", "hump", "world"}, NameBuilder.apart("hello_hump_world"));
        assertArrayEquals(new String[]{"hello", "hump", "world"}, NameBuilder.apart("helloHumpWorld"));
    }
}
