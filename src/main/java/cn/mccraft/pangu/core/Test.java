package cn.mccraft.pangu.core;

import cn.mccraft.pangu.core.loader.AutoWired;

public class Test {
    @AutoWired
    public static Hello hello;

    @AutoWired
    public static class Hello {
        public Hello() {
            System.out.println("Hello");
        }
    }
}
