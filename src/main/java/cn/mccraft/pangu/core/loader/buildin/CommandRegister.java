package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.loader.annotation.RegCommand;
import net.minecraft.command.CommandBase;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@AutoWired
public class CommandRegister extends StoredElementRegister<CommandBase, RegCommand> {
    @Override
    public void registerClass(Class<? extends CommandBase> clazz, RegCommand annotation, String domain) {
        try {
            CommandBase command = clazz.newInstance();
            registerField(null, command, annotation, domain);
        } catch (Exception e) {
            PanguCore.getLogger().info("Error while newInstance of " + clazz.toGenericString() + " for register command", e);
        }
    }

    @Load
    public void register(FMLServerStartingEvent event) {
        for (FieldElement item : items) {
            event.registerServerCommand(item.getInstance());
        }
    }
}
