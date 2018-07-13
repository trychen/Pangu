package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.annotation.RegBlock;
import cn.mccraft.pangu.core.util.NameBuilder;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Field;

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
                .setUnlocalizedName(NameBuilder.buildUnlocalizedName(name));

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
        items.stream()
                // check if need register ItemBlock
                .filter(it -> it.getAnnotation().isRegisterItemBlock())
                // create ItemBlock and set RegistryName
                .map(it -> new ItemBlock(it.getInstance()).setRegistryName(it.getInstance().getRegistryName()))
                // registering
                .forEach(event.getRegistry()::register);
    }
}
