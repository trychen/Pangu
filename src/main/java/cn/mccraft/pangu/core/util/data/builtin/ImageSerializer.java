package cn.mccraft.pangu.core.util.data.builtin;

import com.trychen.bytedatastream.ByteDeserializer;
import com.trychen.bytedatastream.ByteSerializer;
import com.trychen.bytedatastream.DataInput;
import com.trychen.bytedatastream.DataOutput;
import net.minecraft.item.ItemStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public enum ImageSerializer implements ByteSerializer<BufferedImage>, ByteDeserializer<BufferedImage> {
    INSTANCE;

    @Override
    public BufferedImage deserialize(DataInput in) throws IOException {
        return ImageIO.read(in);
    }

    @Override
    public void serialize(DataOutput out, BufferedImage object) throws IOException {
        ImageIO.write(object, "jpg", out);
    }
}
