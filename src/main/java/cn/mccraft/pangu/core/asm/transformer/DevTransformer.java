package cn.mccraft.pangu.core.asm.transformer;

import cn.mccraft.pangu.core.asm.dev.DevOnly;
import cn.mccraft.pangu.core.asm.util.ASMHelper;
import cn.mccraft.pangu.core.asm.util.LambdaGatherer;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.mccraft.pangu.core.asm.util.PanguClassWriter;
import cn.mccraft.pangu.core.util.Sides;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLLog;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

/**
 * Impl of {@link DevOnly}
 */
public class DevTransformer implements IClassTransformer {
    private boolean isDevMode = Sides.isDevEnv;

    public DevTransformer() {
        FMLLog.log.info("Environment Status: " + (isDevMode ? "Development" : "Production"));
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (isDevMode) return basicClass;
        if (basicClass == null) return null;

        AtomicBoolean edited = new AtomicBoolean(false);
        ClassNode classNode = ASMHelper.newClassNode(basicClass);

        if (isDevOnly(classNode.invisibleAnnotations)) {
            throw new RuntimeException(String.format("Attempted to load class %s for invalid environment %s", classNode.name, isDevMode ? "Development" : "Production"));
        }

        // remove fields
        classNode.fields.removeIf(field -> {
            if (isDevOnly(field.visibleAnnotations)) {
                edited.set(true);
                return true;
            } else {
                return false;
            }
        });

        // remove methods
        LambdaGatherer lambdaGatherer = new LambdaGatherer();
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while (methods.hasNext()) {
            MethodNode method = methods.next();
            if (isDevOnly(method.visibleAnnotations)) {
                edited.set(true);
                methods.remove();
                lambdaGatherer.accept(method);
            }
        }

        // remove dynamic lambda methods that are inside of removed methods
        List<Handle> dynamicLambdaHandles = lambdaGatherer.getDynamicLambdaHandles();
        if (!dynamicLambdaHandles.isEmpty()) {
            classNode.methods.forEach(method -> dynamicLambdaHandles.removeIf(dynamicLambdaHandle -> {
                if (method.name.equals(dynamicLambdaHandle.getName()) && method.desc.equals(dynamicLambdaHandle.getDesc())) {
                    edited.set(true);
                    return true;
                }
                return false;
            }));
        }
        if (edited.get()) {
            ClassWriter writer = new PanguClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(writer);
            return writer.toByteArray();
        }
        return basicClass;
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
