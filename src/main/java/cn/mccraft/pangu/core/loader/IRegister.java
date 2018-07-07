package cn.mccraft.pangu.core.loader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @since 1.0.2
 * @author trychen
 */
public interface IRegister<ANNOTATION extends Annotation, INSTANCE> {
    void registerField(Field field, INSTANCE instance, ANNOTATION annotation);
    void registerClass(Class<? extends INSTANCE> clazz, ANNOTATION annotation);
}
