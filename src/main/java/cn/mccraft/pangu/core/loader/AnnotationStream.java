package cn.mccraft.pangu.core.loader;

import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A simple way to operate ASMDataTable.ASMData
 * @since 1.0.2
 */
public class AnnotationStream<T extends Annotation> {
    private final String typeName;
    private final Set<ASMDataTable.ASMData> asmDatas;

    public AnnotationStream(String typeName) {
        this.typeName = typeName;
        this.asmDatas = AnnotationInjector.INSTANCE.getDiscoverer().getASMTable().getAll(typeName);
    }

    public static <T extends Annotation> AnnotationStream<T> of(String typeName) {
        return new AnnotationStream<>(typeName);
    }

    public static <T extends Annotation> AnnotationStream<T> of(Class<T> annoClass) {
        return new AnnotationStream<>(annoClass.getName());
    }

    public String getTypeName() {
        return typeName;
    }

    public Set<ASMDataTable.ASMData> getASMDatas() {
        return asmDatas;
    }

    public Stream<ASMDataTable.ASMData> stream() {
        return asmDatas.stream();
    }

    public Stream<? extends Class<?>> classStream() {
        return asmDatas
                .stream()
                // get class name
                .map(ASMDataTable.ASMData::getClassName)
                .distinct()
                // map class entity
                .map(className -> {
                    try {
                        return Class.forName(className);
                    } catch (Exception e) {
                        return null;
                    }
                })
                // clean class could get instance
                .filter(Objects::nonNull);
    }

    @SuppressWarnings("unchecked")
    public Stream<? extends Class<?>> typeStream() {
        return asmDatas
                .stream()
                // filter that clean non-class object
                .filter(it -> it.getClassName().equals(it.getObjectName()))
                // get class name
                .map(ASMDataTable.ASMData::getClassName)
                .distinct()
                // map class entity
                .map(className -> {
                    try {
                        return Class.forName(className);
                    } catch (Exception e) {
                        return null;
                    }
                })
                // clean class could get instance
                .filter(Objects::nonNull);
    }

    public Stream<Field> fieldStream() {
        return asmDatas
                .stream()
                // filter that clean non-class object
                .filter(it -> !it.getClassName().equals(it.getObjectName()) && !it.getObjectName().contains("("))
                // map class entity
                .map(it -> {
                    try {
                        return Class.forName(it.getClassName()).getDeclaredField(it.getObjectName());
                    } catch (Exception e) {
                        return null;
                    }
                })
                // clean class could get instance
                .filter(Objects::nonNull);
    }

    public Stream<Field> methodStream() {
        // TODO: NotImpl Method
        return null;
    }
}
