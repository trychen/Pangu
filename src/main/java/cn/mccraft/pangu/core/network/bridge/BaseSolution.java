package cn.mccraft.pangu.core.network.bridge;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.InstanceHolder;
import cn.mccraft.pangu.core.network.Bridge;
import cn.mccraft.pangu.core.network.BridgeHandler;
import cn.mccraft.pangu.core.util.Sides;
import cn.mccraft.pangu.core.util.data.Persistence;
import com.github.mouse0w0.fastreflection.FastReflection;
import com.github.mouse0w0.fastreflection.MethodAccessor;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;

public class BaseSolution implements BridgeHandler.Solution {
    @NonNull
    private Bridge bridge;
    @Getter(lazy = true)
    private final Persistence persistence = InstanceHolder.getOrNewInstance(bridge.persistence());
    @NonNull
    private Method method;
    @Getter(lazy = true)
    private final Object ownerInstance = InstanceHolder.getInstance(method.getDeclaringClass());
    @Getter(lazy = true)
    private final String[] actualParameterNames = Arrays.stream(method.getParameters()).map(Parameter::getName).toArray(String[]::new);
    @Getter
    private boolean withEntityPlayerParameter;
    @Getter
    private Type[] actualParameterTypes;
    @Getter
    private boolean isStatic;
    @Getter
    private MethodAccessor methodAccessor;
    @Getter
    private boolean persistenceByParameterOrder;

    public BaseSolution(@NonNull Bridge bridge, @NonNull Method method) throws Exception {
        this.bridge = bridge;
        this.method = method;
        this.isStatic = Modifier.isStatic(method.getModifiers());
        this.withEntityPlayerParameter = method.getParameterTypes().length > 0 && method.getParameterTypes()[0] == EntityPlayer.class;
        this.actualParameterTypes = this.withEntityPlayerParameter ? Arrays.copyOfRange(method.getGenericParameterTypes(), 1, method.getGenericParameterTypes().length) : method.getGenericParameterTypes();
        this.methodAccessor = FastReflection.create(method);
        this.persistenceByParameterOrder = bridge.persistenceByParameterOrder();
        if (method.getParameterCount() > 0 && method.getParameters()[0].isNamePresent()) {
            this.persistenceByParameterOrder = false;
        }
    }

    @Override
    public void solve(EntityPlayer player, byte[] data) throws Exception {
        Object[] objects = getPersistence().deserialize(getActualParameterNames(), data, actualParameterTypes);
        if (isWithEntityPlayerParameter()) objects = ArrayUtils.add(objects, 0, player);
        getMethodAccessor().invoke(getOwnerInstance(), objects);
    }

    @Override
    public void solve(Object[] objects) throws IOException {
        Object[] actualParameters;

        //处理玩家参数
        if (isWithEntityPlayerParameter())
            actualParameters = Arrays.copyOfRange(objects, 1, objects.length);
        else
            actualParameters = objects;

        // 序列化
        byte[] bytes = getPersistence().serialize(getActualParameterNames(), actualParameters, actualParameterTypes, persistenceByParameterOrder);

        if (bytes.length > 30000) {
            MultiPartPacketBuffer buffer = bridge.compress() ? new CompressMultiPartPacketBuffer(bridge.value(), bytes) : new MultiPartPacketBuffer(bridge.value(), bytes);

            for (MultiPartPacket packet : buffer.getPackets()) {
                if (side().isClient()) {
                    if (isWithEntityPlayerParameter())
                        PanguCore.getNetwork().sendTo(packet, (EntityPlayerMP) objects[0]);
                    else
                        PanguCore.getNetwork().sendToAll(packet);
                } else {
                    PanguCore.getNetwork().sendToServer(packet);
                }
            }
        } else {
            IMessage packet = bridge.compress() ? new CompressPacket(bridge.value(), bytes) : new Packet(bridge.value(), bytes);

            // 发包
            if (side().isClient()) {
                if (isWithEntityPlayerParameter())
                    PanguCore.getNetwork().sendTo(packet, (EntityPlayerMP) objects[0]);
                else
                    PanguCore.getNetwork().sendToAll(packet);
            } else {
                PanguCore.getNetwork().sendToServer(packet);
            }
        }
    }

    @Override
    public boolean isContinue() {
        return bridge.side() == Sides.currentThreadSide();
    }

    @Override
    public boolean isSync() {
        return bridge.sync();
    }

    @Override
    public Side side() {
        return bridge.side();
    }
}
