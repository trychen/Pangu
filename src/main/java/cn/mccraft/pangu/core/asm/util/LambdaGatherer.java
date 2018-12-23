package cn.mccraft.pangu.core.asm.util;

import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class LambdaGatherer extends MethodVisitor {
    private static final Handle META_FACTORY = new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory",
            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",
            false);
    private final List<Handle> dynamicLambdaHandles = new ArrayList<>();

    public LambdaGatherer() {
        super(Opcodes.ASM5);
    }

    public void accept(MethodNode method) {
        ListIterator<AbstractInsnNode> insnNodeIterator = method.instructions.iterator();
        while (insnNodeIterator.hasNext()) {
            AbstractInsnNode insnNode = insnNodeIterator.next();
            if (insnNode.getType() == AbstractInsnNode.INVOKE_DYNAMIC_INSN) {
                insnNode.accept(this);
            }
        }
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
        if (META_FACTORY.equals(bsm))
            dynamicLambdaHandles.add((Handle) bsmArgs[1]);
    }

    public List<Handle> getDynamicLambdaHandles() {
        return dynamicLambdaHandles;
    }
}
