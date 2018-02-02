package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.SoundRegister;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RegisteringHandler(SoundRegister.class)
public @interface GeneralCreativeTab {
    /**
     * the name of the creative tabs
     */
    String value();
}
