package cn.mccraft.pangu.core.loader;

import cn.mccraft.pangu.core.util.NameBuilder;
import net.minecraft.util.ResourceLocation;

/**
 * use to storage cached item
 *
 * @since .3
 * @author trychen
 */
public class RegisteringItem<T, A> {
    private final T key;
    private final String domain;
    private final A annotation;

    public RegisteringItem(T key, String domain, A annotation) {
        this.key = key;
        this.domain = domain;
        this.annotation = annotation;
    }

    public T getItem() {
        return key;
    }

    public String getDomain() {
        return domain;
    }

    public A getAnnotation() {
        return annotation;
    }

    public ResourceLocation buildRegistryName(String name) {
        if (domain == null || domain.isEmpty()){
            return new ResourceLocation(name);
        }
        return new ResourceLocation(domain, name);
    }

    public ResourceLocation buildRegistryName(String... name) {
        return buildRegistryName(NameBuilder.buildRegistryName(name));
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
