package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.loader.annotation.RegBlock;
import cn.mccraft.pangu.core.util.NameBuilder;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Block register
 *
 * @author mouse
 */
@AutoWired(registerCommonEventBus = true)
public class BlockRegister extends StoredElementRegister<Block, RegBlock> {
    @Override
    public void registerField(Field field, Block block, RegBlock regBlock, String domain) {
        String[] name = regBlock.value();
        if (name.length == 0) {
            name = NameBuilder.apart(field.getName());
        }
        // set registry name
        block.setRegistryName(PanguResLoc.of(domain, NameBuilder.buildRegistryName(name)))
                // set unlocalized name
                .setTranslationKey(NameBuilder.buildTranslationKey(name));

        super.registerField(field, block, regBlock, domain);
    }

    @SubscribeEvent
    public void registerBlock(RegistryEvent.Register<Block> event) {
        items.forEach(element -> {
            try {
                event.getRegistry().register(element.getInstance());

                // check if there contains ore dict
                if (element.getAnnotation().oreDict().length > 0) {
                    // for each all ore dict from RegItem
                    for (String oreName : element.getAnnotation().oreDict()) {
                        // registering ore dictionary to item
                        OreDictionary.registerOre(oreName, element.getInstance());
                    }
                }
            } catch (Exception ex) {
                PanguCore.getLogger().error("Unable to register model of " + element.getField().toGenericString(), ex);
            }
        });

        PanguCore.getLogger().info("Processed " + items.size() + " RegBlock annotations");
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void registerItemBlocks(RegistryEvent.Register<Item> event) {
        for(FieldElement item : items) {
            if (item.getAnnotation().registerItemBlock()) {
                try {
                    Constructor<? extends ItemBlock> itemBlockConstructor = item.getAnnotation().itemBlockClass().getConstructor(Block.class);
                    itemBlockConstructor.setAccessible(true);
                    event.getRegistry().register(itemBlockConstructor.newInstance(item.getInstance()).setRegistryName(item.getInstance().getRegistryName()));
                } catch (ReflectiveOperationException ex) {
                    PanguCore.getLogger().error("Unable to register itemblock of " + item.getField().toGenericString(), ex);
                }
            }
        }
    }

    /**
     * Registering itemblock model
     */
    @Load(value = LoaderState.INITIALIZATION, side = Side.CLIENT)
    public void registerModel() {
        ItemModelMesher masher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        items.stream()
                .filter(it -> it.getAnnotation().registerRenderer())
                .forEach(it -> {
                    try {
                        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(it.getInstance().getRegistryName(), "inventory");
                        ModelLoader.registerItemVariants(Item.getItemFromBlock(it.getInstance()), modelResourceLocation);
                        masher.register(Item.getItemFromBlock(it.getInstance()), 0, modelResourceLocation);
                    } catch (Exception ex) {
                        PanguCore.getLogger().error("Unable to register model of " + it.getField().toGenericString(), ex);
                    }
                });
        PanguCore.getLogger().info("Processed " + items.size() + " itemblocks' model with @RegBlock");
    }
}
