package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.RegisteringItem;
import cn.mccraft.pangu.core.loader.annotation.RegBlock;
import cn.mccraft.pangu.core.util.NameBuilder;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Block register
 *
 * @author mouse
 */
public class BlockRegister extends BaseRegister<Block, RegBlock> {

    @SubscribeEvent
    public void registerItem(RegistryEvent.Register<Block> event) {
        for (RegisteringItem<Block, RegBlock> registeringItem : itemSet) {
            Block block = registeringItem.getItem();
            RegBlock regBlock = registeringItem.getAnnotation();

            String[] name = regBlock.value();
            if (name.length == 0) {
                name = NameBuilder.apart(registeringItem.getField().getName());
            }

            // start register
            event.getRegistry().register(
                    // set registry name
                    block.setRegistryName(registeringItem.buildRegistryName(name))
                            // set unlocalized name
                            .setUnlocalizedName(registeringItem.buildUnlocalizedName(name))
            );

            // check if there contains ore dict
            if (regBlock.oreDict().length > 0) {
                // for each all ore dict from RegItem
                for (String oreName : regBlock.oreDict()) {
                    // registering ore dictionary to item
                    OreDictionary.registerOre(oreName, block);
                }
            }
        }
        PanguCore.getLogger().info("Processed " + itemSet.size() + " RegBlock annotations");
    }
}
