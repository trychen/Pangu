package cn.mccraft.pangu.core.asm.dev;

import cn.mccraft.pangu.core.util.Environment;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLLog;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Impl of {@link DevOnly}
 */
public class DevTransformer implements IClassTransformer {
    private boolean isDevMode = Environment.isDevEnv();

    public DevTransformer() {
        FMLLog.log.info("Environment Status: " + (isDevMode ? "Development" : "Production"));
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (isDevMode) return basicClass;
        if (basicClass == null) return null;

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        if (isDevOnly(classNode.invisibleAnnotations))
            throw new RuntimeException(String.format("Attempted to load class %s for invalid environment %s", classNode.name, isDevMode ? "Development" : "Production"));

        // remove fields
        classNode.fields.removeIf(field -> isDevOnly(field.visibleAnnotations));

        // remove methods
        LambdaGatherer lambdaGatherer = new LambdaGatherer();
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while (methods.hasNext()) {
            MethodNode method = methods.next();
            if (isDevOnly(method.visibleAnnotations)) {
                methods.remove();
                lambdaGatherer.accept(method);
            }
        }

        // remove dynamic lambda methods that are inside of removed methods
        List<Handle> dynamicLambdaHandles = lambdaGatherer.getDynamicLambdaHandles();
        if (!dynamicLambdaHandles.isEmpty()) {
            classNode.methods.forEach(method -> dynamicLambdaHandles.removeIf(dynamicLambdaHandle -> method.name.equals(dynamicLambdaHandle.getName()) && method.desc.equals(dynamicLambdaHandle.getDesc())));
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    /**
     * check if the list contain {@link DevOnly} node
     *
     * @param anns annotation nodes
     * @return true if contain
     */
    public static boolean isDevOnly(List<AnnotationNode> anns) {
        return anns != null && anns.stream().anyMatch(ann -> ann.desc.equals(Type.getDescriptor(DevOnly.class)));
    }

    static class LambdaGatherer  extends MethodVisitor {
        private static final Handle META_FACTORY = new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory",
                "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",
                false);
        private final List<Handle> dynamicLambdaHandles = new ArrayList<Handle>();

        public LambdaGatherer() {
            super(Opcodes.ASM5);
        }

        public void accept(MethodNode method) {
            ListIterator<AbstractInsnNode> insnNodeIterator = method.instructions.iterator();
            while (insnNodeIterator.hasNext())
            {
                AbstractInsnNode insnNode = insnNodeIterator.next();
                if (insnNode.getType() == AbstractInsnNode.INVOKE_DYNAMIC_INSN)
                {
                    insnNode.accept(this);
                }
            }
        }

        @Override
        public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs)
        {
            if (META_FACTORY.equals(bsm))
            {
                Handle dynamicLambdaHandle = (Handle) bsmArgs[1];
                dynamicLambdaHandles.add(dynamicLambdaHandle);
            }
        }

        public List<Handle> getDynamicLambdaHandles()
        {
            return dynamicLambdaHandles;
        }
    }
}
