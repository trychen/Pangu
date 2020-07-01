package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.CommandRegister;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@RegisteringHandler(value = CommandRegister.class)
public @interface RegCommand {
}
