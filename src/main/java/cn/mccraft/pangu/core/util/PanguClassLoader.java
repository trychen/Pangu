package cn.mccraft.pangu.core.util;

import java.util.HashMap;
import java.util.Map;

public class PanguClassLoader extends ClassLoader {
    private static PanguClassLoader INSTANCE;

    public static PanguClassLoader getInstance() {
        if (INSTANCE == null) INSTANCE = new PanguClassLoader();
        return INSTANCE;
    }

    private final Map<String, byte[]> extraClassDefs = new HashMap<>();

    public PanguClassLoader() {
        super(PanguClassLoader.class.getClassLoader());
    }

    public Class defineClass(String name, byte[] clazz) throws ClassNotFoundException {
        extraClassDefs.put(name, clazz);
        return findClass(name);
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        byte[] classBytes = this.extraClassDefs.remove(name);
        if (classBytes != null) {
            return defineClass(name, classBytes, 0, classBytes.length);
        }
        return super.findClass(name);
    }
}
