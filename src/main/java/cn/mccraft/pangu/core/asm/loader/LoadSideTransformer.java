package cn.mccraft.pangu.core.asm.loader;

import cn.mccraft.pangu.core.asm.transformer.LambdaGatherer;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;
import java.util.List;

/**
 * @see net.minecraftforge.fml.common.asm.transformers.SideTransformer
 */
public class LoadSideTransformer implements IClassTransformer {
    private static String SIDE = FMLLaunchHandler.side().name();
    private static final String ANNOTATION_LOAD_DESCRIPTOR = "Lcn/mccraft/pangu/core/loader/Load;";

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) return null;

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        if (remove(classNode.visibleAnnotations, SIDE)) {
            return null;
        }

        classNode.fields.removeIf(field -> remove(field.visibleAnnotations, SIDE));

        LambdaGatherer lambdaGatherer = new LambdaGatherer();
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while (methods.hasNext()) {
            MethodNode method = methods.next();
            if (remove(method.visibleAnnotations, SIDE)) {
                methods.remove();
                lambdaGatherer.accept(method);
            }
        }

        // remove dynamic lambda methods that are inside of removed methods
        List<Handle> dynamicLambdaHandles = lambdaGatherer.getDynamicLambdaHandles();
        if (!dynamicLambdaHandles.isEmpty()) {
            methods = classNode.methods.iterator();
            while (methods.hasNext()) {
                MethodNode method = methods.next();
                for (Handle dynamicLambdaHandle : dynamicLambdaHandles) {
                    if (method.name.equals(dynamicLambdaHandle.getName()) && method.desc.equals(dynamicLambdaHandle.getDesc())) {
                        methods.remove();
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private boolean remove(List<AnnotationNode> anns, String side) {
        if (anns == null) return false;

        for (AnnotationNode ann : anns) {
            if (ann.desc.equals(ANNOTATION_LOAD_DESCRIPTOR)) {
                if (ann.values != null) {
                    for (int x = 0; x < ann.values.size() - 1; x += 2) {
                        Object key = ann.values.get(x);
                        Object value = ann.values.get(x + 1);
                        if (key instanceof String && key.equals("side")) {
                            if (value instanceof String[]) {
                                if (!((String[]) value)[1].equals(side)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
