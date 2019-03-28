package cn.mccraft.pangu.core.asm.transformer;

import cn.mccraft.pangu.core.asm.PanguPlugin;
import cn.mccraft.pangu.core.asm.util.ASMHelper;
import cn.mccraft.pangu.core.network.RemoteHandler;
import cn.mccraft.pangu.core.util.Sides;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.commons.GeneratorAdapter.EQ;
import static org.objectweb.asm.commons.GeneratorAdapter.NE;

public class RemoteTransformer implements IClassTransformer {
    public static final Side SIDE = Sides.commonSide();
    public static final String REMOTE_ANNOTATION = "Lcn/mccraft/pangu/core/network/Remote;";
    protected static final String
            BYTE_MESSAGE_NAME = ASMHelper.getClassName("cn.mccraft.pangu.core.network.ByteMessage"),
            REMOTE_HANDLER_NAME = ASMHelper.getClassName("cn.mccraft.pangu.core.network.RemoteHandler");

    protected static final Type
            TYPE_BYTE_MESSAGE = Type.getObjectType(BYTE_MESSAGE_NAME),
            TYPE_REMOTE_HANDLER = Type.getObjectType(REMOTE_HANDLER_NAME);

    protected static final Method
            SEND = new Method("send", Type.BOOLEAN_TYPE, new Type[]{ASMHelper.TYPE_STRING, ASMHelper.getArrayType(ASMHelper.TYPE_OBJECT)});

    public static transient List<RemoteMessage> messages = new ArrayList<>();

    public static byte[] createMessage(String className) {
        ClassWriter cw = ASMHelper.newClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_6, ACC_PUBLIC | ACC_SUPER | ACC_SYNTHETIC, className, null, BYTE_MESSAGE_NAME, new String[0]);
        ASMHelper.MethodGenerator generator = ASMHelper.MethodGenerator.visitMethod(cw, ACC_PUBLIC | ACC_SYNTHETIC, ASMHelper._INIT_,
                ASMHelper.VOID_METHOD_DESC, null, null);
        generator.loadThis();
        generator.invokeConstructor(TYPE_BYTE_MESSAGE, new Method(ASMHelper._INIT_, ASMHelper.VOID_METHOD_DESC));
        generator.returnValue();
        generator.endMethod();

        cw.visitEnd();
        return cw.toByteArray();
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        boolean edited = false;
        for (MethodNode method : classNode.methods) {
            // 空检测，检测是否存在注解
            if (method.visibleAnnotations == null) continue;
            Optional<AnnotationNode> remote = method.visibleAnnotations.stream().filter(it -> REMOTE_ANNOTATION.equals(it.desc)).findAny();
            if (!remote.isPresent()) continue;

            // note: @version 1.4.4 无需检测，支持 @AutoWired
            // 检测是否为 static
//            if ((method.access & ACC_STATIC) == 0) {
//                PanguPlugin.getLogger().error("Couln't use @Remote in a non-static method: " + classNode.name + "#" + method.name + method.signature, classNode, method);
//                continue;
//            }

            // 获取注解信息
            Map<String, Object> values = mapAnnotationValues(remote.get());
            int id = (int) values.get("value");
            Side side;
            Object sideObj = values.get("side");
            if (sideObj == null) {
                side = Side.SERVER;
            } else {
                side = Side.valueOf(((String[]) values.get("side"))[1]);
            }

            boolean sync = (boolean) values.getOrDefault("sync", true);
            boolean also = (boolean) values.getOrDefault("also", false);

            Type[] args = Type.getArgumentTypes(method.desc);

            // 生成类名
            String messageClassName = classNode.name + "$Message_" + method.name;
            String transformedNameMessageClassName = transformedName + "$Message_" + method.name;
            byte[] messageClass = createMessage(messageClassName);

            PanguPlugin.getLogger().debug("Created IMessage " + transformedNameMessageClassName);
            if (SIDE == Side.SERVER && side == Side.CLIENT) {
                MethodNode newMethod = new MethodNode(method.access, method.name, method.desc, method.signature, method.exceptions.toArray(new String[0]));
                ASMHelper.MethodGenerator generator = ASMHelper.MethodGenerator.fromMethodNode(newMethod);
                Label label = generator.newLabel(), end = generator.newLabel();
                generator.push(transformedNameMessageClassName);
                generator.loadArgArray();
                generator.invokeStatic(TYPE_REMOTE_HANDLER, SEND);
                if (!also) {
                    generator.ifZCmp(EQ, label);
                    generator.returnValue();
                    generator.mark(label);
                } else {
                    generator.pop();
                }
                generator.endMethod();
                method.instructions.insertBefore(method.instructions.getFirst(), newMethod.instructions);
                method.instructions.insert(new LabelNode(end));
                PanguPlugin.getLogger().debug("Replaced method " + classNode.name + "#" + method.name + method.signature + " with ");
            } else if (SIDE == Side.CLIENT && side == Side.SERVER) {
                MethodNode newMethod = new MethodNode(method.access, method.name, method.desc, method.signature, method.exceptions.toArray(new String[0]));
                ASMHelper.MethodGenerator generator = ASMHelper.MethodGenerator.fromMethodNode(newMethod);
                Label label = generator.newLabel(), end = generator.newLabel();
                generator.push(transformedNameMessageClassName);
                generator.loadArgArray();
                generator.invokeStatic(TYPE_REMOTE_HANDLER, SEND);
                if (!also) {
                    generator.ifZCmp(EQ, label);
                    generator.returnValue();
                    generator.mark(label);
                } else {
                    generator.pop();
                }
                method.instructions.insertBefore(method.instructions.getFirst(), newMethod.instructions);
                method.instructions.insert(new LabelNode(end));
                PanguPlugin.getLogger().debug("Inserted into method " + classNode.name + "#" + method.name + method.signature + " with ");
            }
            RemoteMessage remoteMessage = new RemoteMessage(id, messageClass, transformedNameMessageClassName, transformedName, method.name, args, side, sync, also);

            if (messages != null)
                messages.add(remoteMessage);
            else
                RemoteHandler.registerMessage(remoteMessage);
            edited = true;
            PanguPlugin.getLogger().debug("Hook @Remote method: " + classNode.name + "#" + method.name + method.desc + "");
        }
        if (!edited) return basicClass;
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);
        return cw.toByteArray();
    }

    Map<String, Object> mapAnnotationValues(AnnotationNode ann) {
        Map<String, Object> map = new HashMap<>();
        if (ann.values == null) return map;
        for (int x = 0; x < ann.values.size() - 1; x += 2) {
            Object key = ann.values.get(x);
            Object value = ann.values.get(x + 1);
            map.put((String) key, value);
        }
        return map;
    }

    @Data
    @AllArgsConstructor
    public static class RemoteMessage {
        public final int id;
        public byte[] messageClassBytes;
        public final String messageClassName;
        public final String ownerClass;
        public final String methodName;
        public final Type[] methodArgTypes;
        public final Side side;
        public final boolean sync;
        public final boolean also;
    }
}
