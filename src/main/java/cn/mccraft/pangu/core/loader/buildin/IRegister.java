package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.loader.RegisteringItem;

/**
 * @since .3
 * @author trychen
 */
public interface IRegister {
    boolean preRegister(RegisteringItem registeringItem);
}
