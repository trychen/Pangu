package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.util.data.ByteStreamPersistence;
import cn.mccraft.pangu.core.util.data.JsonPersistence;
import cn.mccraft.pangu.core.util.data.Persistence;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bridge {
    /**
     * The key to identity different packet. <br/>
     * If value is empty, will use the owner's name & method's name as the value.
     */
    String value() default "";

    /**
     * The side to exec the method
     */
    Side side() default Side.SERVER;

    /**
     * Execute the method in "minecraft thread" instead "netty io thread"
     */
    boolean sync() default true;

    /**
     * Execute the method in both side
     */
    boolean also() default false;

    /**
     * Serializer / Deserializer
     */
    Class<? extends Persistence> persistence() default JsonPersistence.class;

    /**
     * In some case (ex. dynamic proxy) cannot get the parameter name
     */
    boolean persistenceByParameterOrder() default true;

    boolean hooked() default false;

    /**
     * Enable GZip compress
     */
    boolean compress() default false;

    boolean obfuscatedKeyName() default false;
}
