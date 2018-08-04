package cn.mccraft.pangu.core.test.reflection;

public interface SimpleInterface {
    String SFF = "SFF";

    static String staticMethod() {
        return "Hello".toLowerCase();
    }

    int returnOne();

    default int returnTwo() {
        return 2;
    }
}
