package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.loader.RegisteringItem;
import cn.mccraft.pangu.core.loader.annotation.RegItem;
import cn.mccraft.pangu.core.util.NameBuilder;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Register to register item with RegItem
 *
 * @author trychen
 * @since .3
 */
public class ItemRegister extends BaseRegister<Item, RegItem> {
    /**
     * forge build-in event holder.
     * the implementation of {@link RegItem}
     *
     * @param event
     */
    @SubscribeEvent
    public void registerItem(RegistryEvent.Register<Item> event) {
        for (RegisteringItem<Item, RegItem> registeringItem : itemSet) {
            Item item = registeringItem.getItem();
            RegItem regItem = registeringItem.getAnnotation();

            String[] name = regItem.value();
            if (name.length == 0){
                name = NameBuilder.apart(registeringItem.getField().getName());
            }

            // start register
            event.getRegistry().register(
                    // set registry name
                    item.setRegistryName(registeringItem.buildRegistryName(name))
                            // set unlocalized name
                            .setUnlocalizedName(registeringItem.buildUnlocalizedName(name))
            );

            // check if there contains ore dict
            if (regItem.oreDict().length > 0) {
                // for each all ore dict from RegItem
                for (String oreName : regItem.oreDict()) {
                    // registering ore dictionary to item
                    OreDictionary.registerOre(oreName, item);
                }
            }
        }
    }
}
