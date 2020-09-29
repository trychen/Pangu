package cn.mccraft.pangu.core.asm;

import cn.mccraft.pangu.core.util.Sides;
import lombok.extern.log4j.Log4j2;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({"cn.mccraft.pangu.core.asm"})
public class PanguPlugin implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        List<String> transformers = new ArrayList<>();

        transformers.add("cn.mccraft.pangu.core.asm.util.ClassManager");
        transformers.add("cn.mccraft.pangu.core.asm.transformer.DevTransformer");
//        transformers.add("cn.mccraft.pangu.core.asm.transformer.CheckPrefixTransformer");
        transformers.add("cn.mccraft.pangu.core.asm.transformer.BridgeTransformer");

        // Client side only transformer
        if (Sides.isClient()) {
            transformers.add("cn.mccraft.pangu.core.asm.transformer.GuiBackgroundColorTransformer");
        } else {
            transformers.add("cn.mccraft.pangu.core.asm.transformer.ServerSideTransformer");
        }
        return transformers.toArray(new String[0]);
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
