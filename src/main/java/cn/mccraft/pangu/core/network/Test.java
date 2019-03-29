package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.util.data.JsonPersistence;
import lombok.experimental.Accessors;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

public interface Test {
    @Bridge(persistence = JsonPersistence.class)
    static void test(String name, Data data, String nullPara) {
        System.out.println(name);
        System.out.println(data);
        System.out.println(nullPara);
    }

    @BindKeyPress(Keyboard.KEY_J)
    static void test() {
        test("Hello", new Data().setName("Hello").setAge(20).setList(Arrays.asList("Hello", "World")), null);
    }

    @lombok.Data
    @Accessors(chain = true)
    class Data {
        private String name;
        private int age;
        private List<String> list;
    }
}
