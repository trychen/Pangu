package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.loader.RegisteringItem;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by trychen on 17/9/23.
 */
public class BaseRegister<T, A> implements IRegister {
    protected Set<RegisteringItem<T, A>> itemSet = new HashSet<>();

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
