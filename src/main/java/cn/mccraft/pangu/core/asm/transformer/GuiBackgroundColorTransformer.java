package cn.mccraft.pangu.core.asm.transformer;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class GuiBackgroundColorTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("net.minecraft.client.gui.GuiScreen")) {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(basicClass);
            classReader.accept(classNode, 0);

            classNode.methods
                    .stream()
                    .filter(m -> m.name.equals("drawWorldBackground") || m.name.equals("func_146270_b"))
                    .forEach(m -> {
                        for (int i = 0; i < m.instructions.size(); i++) {
                            AbstractInsnNode next = m.instructions.get(i);
                            if (next.getOpcode() == Opcodes.LDC) {
                                AbstractInsnNode hook = new MethodInsnNode(Opcodes.INVOKESTATIC, "cn/mccraft/pangu/core/util/render/Blur", "getGuiBackgroundColor", "(Lnet/minecraft/client/gui/GuiScreen;Z)I", false);
                                AbstractInsnNode hook2 = hook.clone(null);
                                m.instructions.set(next, hook);
                                m.instructions.set(hook.getNext(), hook2);

                                m.instructions.insertBefore(hook, new VarInsnNode(Opcodes.ALOAD, 0));
                                m.instructions.insertBefore(hook, new InsnNode(Opcodes.ICONST_1));

                                m.instructions.insertBefore(hook2, new VarInsnNode(Opcodes.ALOAD, 0));
                                m.instructions.insertBefore(hook2, new InsnNode(Opcodes.ICONST_0));
                                return;
                            }
                        }
                    });

            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(cw);
            return cw.toByteArray();
        }

        return basicClass;
    }

}