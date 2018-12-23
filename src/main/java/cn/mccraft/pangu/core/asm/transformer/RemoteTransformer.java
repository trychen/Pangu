package cn.mccraft.pangu.core.asm.transformer;

import cn.mccraft.pangu.core.asm.util.ASMHelper;
import cn.mccraft.pangu.core.network.RemoteHandler;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.FileUtils;
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
import java.util.*;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.commons.GeneratorAdapter.EQ;
import static org.objectweb.asm.commons.GeneratorAdapter.NE;

public class RemoteTransformer implements IClassTransformer {
    public static final Side SIDE = FMLLaunchHandler.side();
    public static final String REMOTE_ANNOTATION = "Lcn/mccraft/pangu/core/network/Remote;";
    protected static final String
            BYTE_MESSAGE_NAME = ASMHelper.getClassName("cn.mccraft.pangu.core.network.ByteMessage"),
            REMOTE_HANDLER_NAME = ASMHelper.getClassName("cn.mccraft.pangu.core.network.RemoteHandler"),
            MINECRAFT_THREADING_NAME = ASMHelper.getClassName("cn.mccraft.pangu.core.util.MinecraftThreading");
    protected static final Type
            TYPE_BYTE_MESSAGE = Type.getObjectType(BYTE_MESSAGE_NAME),
            TYPE_REMOTE_HANDLER = Type.getObjectType(REMOTE_HANDLER_NAME),
            TYPE_MINECRAFT_THREADING = Type.getObjectType(MINECRAFT_THREADING_NAME);
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
        for (MethodNode method : classNode.methods) {
            if (method.visibleAnnotations == null) continue;
            Optional<AnnotationNode> remote = method.visibleAnnotations.stream().filter(it -> REMOTE_ANNOTATION.equals(it.desc)).findAny();
            if (!remote.isPresent()) continue;

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

            Type[] args = Type.getArgumentTypes(method.desc);

            String messageClassName = classNode.name + "$Message_" + method.name;
            String transformedNameMessageClassName = transformedName + "$Message_" + method.name;
            byte[] messageClass = createMessage(messageClassName);

            if (SIDE == Side.SERVER && side == Side.CLIENT) {
                MethodNode newMethod = new MethodNode(method.access, method.name, method.desc, method.signature, method.exceptions.toArray(new String[0]));
                ASMHelper.MethodGenerator generator = ASMHelper.MethodGenerator.fromMethodNode(newMethod);
                generator.push(transformedNameMessageClassName);
                generator.loadArgArray();
                generator.invokeStatic(TYPE_REMOTE_HANDLER, SEND);
                generator.returnValue();
                generator.endMethod();
                method.instructions.clear();
                method.instructions.add(newMethod.instructions);
            } else if (SIDE == Side.CLIENT && side == Side.SERVER) {
                MethodNode newMethod = new MethodNode(method.access, method.name, method.desc, method.signature, method.exceptions.toArray(new String[0]));
                ASMHelper.MethodGenerator generator = ASMHelper.MethodGenerator.fromMethodNode(newMethod);
                Label label = generator.newLabel(), end = generator.newLabel();
                generator.push(transformedNameMessageClassName);
                generator.loadArgArray();
                generator.invokeStatic(TYPE_REMOTE_HANDLER, SEND);
                generator.ifZCmp(EQ, label);
                generator.returnValue();
                generator.mark(label);
                method.instructions.insertBefore(method.instructions.getFirst(), newMethod.instructions);
                method.instructions.insert(new LabelNode(end));
            }
            RemoteMessage remoteMessage = new RemoteMessage(id, messageClass, transformedNameMessageClassName, transformedName, method.name, args, side, sync);
            if (messages != null)
                messages.add(remoteMessage);
            else
                RemoteHandler.registerMessage(remoteMessage);
        }

        ClassWriter cw = ASMHelper.newClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
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

    public static class RemoteMessage {
        public final int id;
        public final byte[] messageClassBytes;
        public final String messageClassName;
        public final String ownerClass;
        public final String methodName;
        public final Type[] methodArgs;
        public final Side side;
        public final boolean sync;

        public RemoteMessage(int id, byte[] messageClassBytes, String messageClassName, String ownerClass, String methodName, Type[] methodArgs, Side side, boolean sync) {
            this.id = id;
            this.messageClassBytes = messageClassBytes;
            this.messageClassName = messageClassName;
            this.ownerClass = ownerClass;
            this.methodName = methodName;
            this.methodArgs = methodArgs;
            this.side = side;
            this.sync = sync;
        }
    }
}
