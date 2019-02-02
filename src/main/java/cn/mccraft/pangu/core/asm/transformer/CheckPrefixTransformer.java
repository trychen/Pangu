package cn.mccraft.pangu.core.asm.transformer;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.asm.PanguPlugin;
import cn.mccraft.pangu.core.asm.util.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CheckPrefixTransformer implements IClassTransformer {
    protected static final Method
        EQUALS = new Method("equals", Type.BOOLEAN_TYPE, new Type[]{ASMHelper.TYPE_STRING});

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!transformedName.equals("net.minecraftforge.registries.GameData")) return basicClass;

        ClassNode classNode = ASMHelper.newClassNode(basicClass);

        outer:
        for (MethodNode m : classNode.methods) {
            if (!m.name.equals("checkPrefix")) continue;
            for (int i = 0; i < m.instructions.size(); i++) {
                AbstractInsnNode next = m.instructions.get(i);
                // GETSTATIC net/minecraftforge/fml/common/FMLLog.log : Lorg/apache/logging/log4j/Logger;
                if (next.getOpcode() != Opcodes.GETSTATIC) continue;
                FieldInsnNode fieldNode = (FieldInsnNode) next;
                if (!"net/minecraftforge/fml/common/FMLLog".equals(fieldNode.owner)) continue;
                if (!"log".equals(fieldNode.name)) continue;
                if (!"Lorg/apache/logging/log4j/Logger;".equals(fieldNode.desc)) continue;

                LabelNode label = new LabelNode();
                // LDC "pangu"
                // ALOAD 2
                m.instructions.insertBefore(fieldNode, new LdcInsnNode(PanguCore.ID));
                m.instructions.insertBefore(fieldNode, new VarInsnNode(Opcodes.ALOAD, 2));

                // INVOKEVIRTUAL java/lang/String.equals (Ljava/lang/Object;)Z
                // IFNE L13
                m.instructions.insertBefore(fieldNode, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false));
                m.instructions.insertBefore(fieldNode, new JumpInsnNode(Opcodes.IFNE, label));

                AbstractInsnNode invokeNode = fieldNode;
                while (invokeNode.getOpcode() != Opcodes.INVOKEINTERFACE) {
                    invokeNode = invokeNode.getNext();
                }
                m.instructions.insert(invokeNode, label);

                ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
                classNode.accept(cw);
                PanguPlugin.getLogger().info("Hooked net.minecraftforge.registries.GameData#checkPrefix(Ljava/lang/String;)Lnet/minecraft/util/ResourceLocation;");
                return cw.toByteArray();
            }
        }

        PanguPlugin.getLogger().error("Couldn't hook checkPrefix!");

        return basicClass;
    }
}
