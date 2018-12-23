package cn.mccraft.pangu.core.asm.transformer;

import cn.mccraft.pangu.core.asm.dev.DevOnly;
import cn.mccraft.pangu.core.asm.util.ASMHelper;
import cn.mccraft.pangu.core.asm.util.LambdaGatherer;
import cn.mccraft.pangu.core.util.Environment;

import java.util.Iterator;
import java.util.List;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLLog;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

/**
 * Impl of {@link DevOnly}
 */
public class DevTransformer implements IClassTransformer {
    private boolean isDevMode = Environment.isDevEnv;

    public DevTransformer() {
        FMLLog.log.info("Environment Status: " + (isDevMode ? "Development" : "Production"));
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (isDevMode) return basicClass;

        ClassNode classNode = ASMHelper.newClassNode(basicClass);

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

        ClassWriter writer = ASMHelper.newClassWriter(ClassWriter.COMPUTE_MAXS);
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
}
