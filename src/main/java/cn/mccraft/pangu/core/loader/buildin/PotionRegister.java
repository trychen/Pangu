package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.annotation.RegPotion;
import cn.mccraft.pangu.core.util.NameBuilder;
import net.minecraft.potion.Potion;
import net.minecraft.util.StringUtils;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;

/**
 * @author trychen
 * @since 1.0.6
 */
@AutoWired(registerCommonEventBus = true)
public class PotionRegister extends StoredElementRegister<Potion, RegPotion> {
    @Override
    public void registerField(Field field, Potion potion, RegPotion regPotion, String domain) {
        String[] name = regPotion.value();
        if (name.length == 0) {
            name = NameBuilder.apart(field.getName());
        }

        potion.setRegistryName(NameBuilder.buildRegistryName(name));

        if (StringUtils.isNullOrEmpty(potion.getName()))
            potion.setPotionName("effect." + NameBuilder.buildTranslationKey(name));

        super.registerField(field, potion, regPotion, domain);
    }


    @SubscribeEvent
    public void registerPotion(RegistryEvent.Register<Potion> event) {
        items.forEach(element -> {
            try {
                event.getRegistry().register(element.getInstance());
            } catch (Exception ex) {
                PanguCore.getLogger().error("Unable to register " + element.getField().toGenericString(), ex);
            }
        });

        PanguCore.getLogger().info("Processed " + items.size() + " @RegPotion annotations");
    }
}
