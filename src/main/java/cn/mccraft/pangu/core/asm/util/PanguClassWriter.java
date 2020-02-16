package cn.mccraft.pangu.core.asm.util;

import org.objectweb.asm.ClassWriter;

public class PanguClassWriter extends ClassWriter {
    private final boolean runtime;

    public PanguClassWriter(int flags) {
        this(flags, false);
    }

    public PanguClassWriter(int flags, boolean runtime)
    {
        super(flags);
        this.runtime = runtime;
    }


    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        String c = type1.replace('/', '.');
        String d = type2.replace('/', '.');

        if (ClassManager.classExtends(d, c)) return type1;

        if (ClassManager.classExtends(c, d)) return type2;

        do
            c = ClassManager.getSuperClass(c, runtime);
        while (!ClassManager.classExtends(d, c));

        return c.replace('.', '/');
    }
}
