package cn.mccraft.pangu.core.asm;

import lombok.extern.log4j.Log4j2;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({"cn.mccraft.pangu.core.asm"})
@Log4j2(topic = "Pangu Core")
public class PanguPlugin implements IFMLLoadingPlugin {
    public static Logger getLogger() {
        return log;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {
                "cn.mccraft.pangu.core.asm.transformer.DevTransformer",
                "cn.mccraft.pangu.core.asm.transformer.LoadSideTransformer",
                "cn.mccraft.pangu.core.asm.transformer.GuiBackgroundColorTransformer",
                "cn.mccraft.pangu.core.asm.transformer.RemoteTransformer"
        };
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
