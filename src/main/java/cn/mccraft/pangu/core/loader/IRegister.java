package cn.mccraft.pangu.core.loader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @since 1.0.0.3
 * @author trychen
 */
public interface IRegister<ANNO extends Annotation, OBJ> {
    void registerField(Field field, OBJ instance, ANNO annotation);
    void registerClass(Class<? extends OBJ> clazz, ANNO annotation);
}
