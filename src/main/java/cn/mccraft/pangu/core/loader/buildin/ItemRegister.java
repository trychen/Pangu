package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.BaseRegister;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.loader.RegisteringItem;
import cn.mccraft.pangu.core.loader.annotation.RegItem;
import cn.mccraft.pangu.core.util.NameBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Register to register item with RegItem
 *
 * @author trychen
 * @since 1.0.0.3
 */
public class ItemRegister extends BaseRegister<Item, RegItem> {
    /**
     * forge build-in event holder.
     * the implementation of {@link RegItem}
     */
    @SubscribeEvent
    public void registerItem(RegistryEvent.Register<Item> event) {
        for (RegisteringItem<Item, RegItem> registeringItem : itemSet) try {
            Item item = registeringItem.getItem();
            RegItem regItem = registeringItem.getAnnotation();

            String[] name = regItem.value();
            if (name.length == 0) {
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
        } catch (Exception ex) {
            PanguCore.getLogger().error("Unable to register " + registeringItem.getField().toGenericString(), ex);
        }
        PanguCore.getLogger().info("Processed " + itemSet.size() + " @RegItem annotations");
    }

    /**
     * Registering model
     */
    @SideOnly(Side.CLIENT)
    @Load(value = LoaderState.INITIALIZATION, side = Side.CLIENT)
    public void registerModel() {
        ItemModelMesher masher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        itemSet.stream()
                .filter(it -> it.getAnnotation().registerModel())
                .forEach(it -> {
                    try {
                        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(it.getItem().getRegistryName(), "inventory");
                        ModelLoader.registerItemVariants(it.getItem(), modelResourceLocation);
                        masher.register(it.getItem(), 0, modelResourceLocation);
                    } catch (Exception ex) {
                        PanguCore.getLogger().error("Unable to register model of " + it.getField().toGenericString(), ex);
                    }
                });
        PanguCore.getLogger().info("Processed " + itemSet.size() + " items' model with @RegItem");
    }
}
