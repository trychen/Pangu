package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.loader.annotation.RegItem;
import cn.mccraft.pangu.core.util.NameBuilder;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
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

import java.lang.reflect.Field;

/**
 * Register to register item with RegItem
 *
 * @author trychen
 * @since 1.0.0.3
 */
@AutoWired(registerCommonEventBus = true)
public class ItemRegister extends StoredElementRegister<Item, RegItem> {
    @Override
    public void registerField(Field field, Item item, RegItem regItem, String domain) {
        String[] name = regItem.value();
        if (name.length == 0) {
            name = NameBuilder.apart(field.getName());
        }
        // set registry name
        item.setRegistryName(PanguResLoc.of(domain, NameBuilder.buildRegistryName(name)))
                // set unlocalized name
                .setUnlocalizedName(NameBuilder.buildUnlocalizedName(name));

        super.registerField(field, item, regItem, domain);
    }

    /**
     * forge build-in event holder.
     * the implementation of {@link RegItem}
     */
    @SubscribeEvent
    public void registerItem(RegistryEvent.Register<Item> event) {
        items.forEach(element -> {
            try {
                // start register
                event.getRegistry().register(element.getInstance());

                // for each all ore dict from RegItem
                for (String oreName : element.getAnnotation().oreDict())
                    // registering ore dictionary to item
                    OreDictionary.registerOre(oreName, element.getInstance());
            } catch (Exception ex) {
                PanguCore.getLogger().error("Unable to register " + element.getField().toGenericString(), ex);
            }
        });

        PanguCore.getLogger().info("Processed " + items.size() + " @RegItem annotations");
    }

    /**
     * Registering model
     */
    @SideOnly(Side.CLIENT)
    @Load(value = LoaderState.INITIALIZATION, side = Side.CLIENT)
    public void registerModel() {
        ItemModelMesher masher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        items.stream()
                .filter(it -> it.getAnnotation().registerModel())
                .forEach(it -> {
                    try {
                        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(it.getInstance().getRegistryName(), "inventory");
                        ModelLoader.registerItemVariants(it.getInstance(), modelResourceLocation);
                        masher.register(it.getInstance(), 0, modelResourceLocation);
                    } catch (Exception ex) {
                        PanguCore.getLogger().error("Unable to register model of " + it.getField().toGenericString(), ex);
                    }
                });
        PanguCore.getLogger().info("Processed " + items.size() + " items' model with @RegItem");
    }
}
