package cn.mccraft.pangu.core.asm.transformer;

import cn.mccraft.pangu.core.asm.PanguPlugin;
import cn.mccraft.pangu.core.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class GuiBackgroundColorTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!transformedName.equals("net.minecraft.client.gui.GuiScreen")) return basicClass;

        ClassNode classNode = ASMHelper.newClassNode(basicClass);

        outer:for (MethodNode m : classNode.methods) {
            final String mappedMethodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, m.name, m.desc);
            if (!mappedMethodName.equals("drawWorldBackground") && !mappedMethodName.equals("func_146270_b")) continue;
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

                    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    classNode.accept(cw);
                    PanguPlugin.getLogger().info("Hooked net.minecraft.client.gui.GuiScreen#drawWorldBackground(int);");
                    return cw.toByteArray();
                }
            }
        }

        PanguPlugin.getLogger().error("Couldn't hook drawWorldBackground!");

        return basicClass;
    }

}