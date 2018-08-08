package cn.mccraft.pangu.core.test.reflection;

public class SimpleChild extends SimpleParent {
    private int privateField = -1;
    public int field = 1;

    public SimpleChild() {
    }

    @Override
    public int returnOne() {
        return 1;
    }

    @Override
    public String sayHello() {
        return "HelloWorld";
    }

    private String sayPrivateHello() {
        return "HelloPrivate";
    }
}
