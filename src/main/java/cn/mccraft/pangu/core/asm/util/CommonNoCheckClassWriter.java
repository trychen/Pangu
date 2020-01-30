package cn.mccraft.pangu.core.asm.util;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import static cn.mccraft.pangu.core.asm.util.ASMHelper.*;

public class CommonNoCheckClassWriter extends ClassWriter {
    public CommonNoCheckClassWriter(int flags) {
        super(flags);
    }

    @Override
    protected String getCommonSuperClass(String a, String b) {
        ClassNode aNode = getClassNode(a), bNode = getClassNode(b);
        java.util.Objects.requireNonNull(aNode);
        java.util.Objects.requireNonNull(bNode);

        if (isInstance(aNode, bNode))
            return a;
        if (isInstance(bNode, aNode))
            return b;
        if (isInterface(aNode) || isInterface(bNode))
            return OBJECT_NAME;
        do
            aNode = getSuperClassNode(aNode);
        while (!isInstance(aNode, bNode));
        return aNode.name;
    }
}
