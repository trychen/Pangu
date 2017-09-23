package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.RegisteringItem;
import cn.mccraft.pangu.core.loader.annotation.RegItem;
import cn.mccraft.pangu.core.util.NameBuilder;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;
import java.util.Set;

/**
 * Register to register item with RegItem
 *
 * @author trychen
 * @since .3
 */
@Mod.EventBusSubscriber(modid = PanguCore.MODID)
public class ItemRegister extends BaseRegister<Item, RegItem> {
    /**
     * forge build-in event holder
     * @param event
     */
    @SubscribeEvent
    public void registerItem(RegistryEvent.Register<Item> event) {
        for (RegisteringItem<Item, RegItem> registeringItem : itemSet) {
            Item item = registeringItem.getItem();
            RegItem regItem = registeringItem.getAnnotation();

            // start register
            event.getRegistry().register(
                    // set registry name
                    item.setRegistryName(
                            registeringItem.buildRegistryName(regItem.value())
                    )
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
