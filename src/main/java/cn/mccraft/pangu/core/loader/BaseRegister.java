package cn.mccraft.pangu.core.loader;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * A base achievement of IRegister with base caching registering item
 *
 * @param <T> the registering item
 * @param <A> the annotation for registering
 */
public abstract class BaseRegister<T, A extends Annotation> implements IRegister {
    protected Set<RegisteringItem<T, A>> itemSet = new HashSet<>();

    @SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
    @Override
    public boolean preRegister(RegisteringItem registeringItem) {
        try {
            // check type
            ((T) registeringItem.getItem()).getClass();
            ((A) registeringItem.getAnnotation()).getClass();

            // add to set
            itemSet.add(registeringItem);
        } catch (ClassCastException cce) {
            // error type
            return false;
        }
        return true;
    }
}
