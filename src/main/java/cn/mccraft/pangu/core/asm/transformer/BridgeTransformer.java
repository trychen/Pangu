package cn.mccraft.pangu.core.asm.transformer;

import cn.mccraft.pangu.core.asm.PanguPlugin;
import cn.mccraft.pangu.core.asm.util.ASM;
import cn.mccraft.pangu.core.asm.util.ASMHelper;
import cn.mccraft.pangu.core.asm.util.CommonNoCheckClassWriter;
import cn.mccraft.pangu.core.util.Sides;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.Side;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Optional;

import static org.objectweb.asm.commons.GeneratorAdapter.EQ;

public class BridgeTransformer implements IClassTransformer {
    public static final Side SIDE = Sides.commonSide();
    public static final String INJECTION_ANNOTATION = "Lcn/mccraft/pangu/core/network/Bridge;";

    public static final Type TYPE_HANDLER = Type.getObjectType("cn/mccraft/pangu/core/network/BridgeHandler");
    public static final Method METHOD_SEND = new Method("send", Type.BOOLEAN_TYPE, new Type[]{ASMHelper.TYPE_STRING, ASMHelper.getArrayType(ASMHelper.TYPE_OBJECT)});

    @SuppressWarnings("Duplicates")
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassNode classNode = ASMHelper.newClassNode(basicClass);
        boolean edited = false;

        for (MethodNode method : classNode.methods) {
            // 检查是否存在注解
            if (method.visibleAnnotations == null) continue;
            Optional<AnnotationNode> optionalAnnotation = method.visibleAnnotations.stream().filter(it ->  INJECTION_ANNOTATION.equals(it.desc)).findAny();
            if (!optionalAnnotation.isPresent()) continue;

            // 获取注解数据
            Map<String, Object> meta = ASM.mapAnnotationValues(optionalAnnotation.get());

            Side side = ASM.mapAnnotationSideValue("side", meta);

            edited = true;

            String key = (String) meta.get("value");
//            boolean sync = (boolean) meta.getOrDefault("sync", true);
            boolean also = (boolean) meta.getOrDefault("also", false);

            // key 不存在时通过类名方法生成
            if (key == null || key.isEmpty()) {
                key = classNode.name;
                key += ".";
                key += method.name;
                key += method.desc;

                // 修改 Key
                ASM.modifyAnnotation(optionalAnnotation.get(), "value", key);
            }

            if (side != SIDE) {

                MethodNode newMethod = new MethodNode(method.access, method.name, method.desc, method.signature, method.exceptions.toArray(new String[0]));
                GeneratorAdapter generator = new GeneratorAdapter(newMethod, newMethod.access, newMethod.name, newMethod.desc);

                generator.push(key);
                generator.loadArgArray();
                generator.invokeStatic(TYPE_HANDLER, METHOD_SEND);

                if (side.isClient() && SIDE.isServer()) {
                    generator.returnValue();
                } else {
                    if (also) {
                        generator.pop();
                    } else {
                        Label label = generator.newLabel();
                        generator.ifZCmp(EQ, label);
                        generator.returnValue();
                        generator.mark(label);
                    }
                }

                if (SIDE == Side.SERVER) {
                    generator.endMethod();
                    method.instructions = newMethod.instructions;
                } else {
                    method.instructions.insertBefore(method.instructions.getFirst(), newMethod.instructions);
                }
            }

            PanguPlugin.getLogger().debug("Hook @Bridge method: " + classNode.name + "#" + method.name + method.desc + "");
        }
        if (!edited) return basicClass;
        ClassWriter cw = new CommonNoCheckClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);
        return cw.toByteArray();
    }
}
