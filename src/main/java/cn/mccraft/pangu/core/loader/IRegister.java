package cn.mccraft.pangu.core.loader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @since 1.0.3
 * @author trychen
 * @deprecated {@link cn.mccraft.pangu.core.loader.AnnotationRegister}
 */
@Deprecated
public interface IRegister<ANNOTATION extends Annotation, INSTANCE> extends AnnotationRegister<ANNOTATION, INSTANCE> {
}
