package cn.mccraft.pangu.core.util.image;

import cn.mccraft.pangu.core.PanguCore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public abstract class GifImage implements TextureProvider {
    protected static final ExecutorService LOADING_EXECUTOR = Executors.newFixedThreadPool(5, new ThreadFactory() {
        private ThreadGroup group = new ThreadGroup("Pangu Gif Loader Threads");
        private int index = 1;

        public Thread newThread(Runnable r) {
            return new Thread(this.group, r, "Pangu Gif Loader #" + this.index++);
        }
    });

    protected String id;
    protected Future<List<Frame>> frames;

    @Getter
    protected transient boolean exception = false;

    @Getter
    protected List<ResourceLocation> sequences;

    @Getter
    protected int duration;

    @Getter
    protected transient int width, height;

    public void decode() {
        frames = LOADING_EXECUTOR.submit(() -> {
            GifDecoder d = new GifDecoder();
            readGifImage(d);
            int time = 0;
            List<Frame> frames = new ArrayList<>(d.getFrameCount() * 2);
            for (int i = 0; i < d.getFrameCount(); i++) {
                BufferedImage frame = d.getFrame(i);
                frames.add(new Frame(frame, d.getDelay(i)));
                time += d.getDelay(i);
            }
            width = d.getFrameSize().width;
            height = d.getFrameSize().height;
            duration = time;
            return frames;
        });
    }

    public void upload() throws ExecutionException, InterruptedException {
        List<ResourceLocation> sequences = new ArrayList<>(duration);
        int count = 0;
        for (Frame frame : frames.get()) {

            ResourceLocation resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("pangu_gif_image_" + id + "_" + count, new DynamicTexture(frame.image));

            for (int i = 0; i < frame.delay; i++) {
                sequences.add(resourceLocation);
            }
            count++;
            frame.image = null;
        }
        frames = null;
        this.sequences = sequences;
    }

    @Override
    public ResourceLocation getTexture() {
        return getTexture(null, null);
    }

    @Override
    public ResourceLocation getTexture(ResourceLocation loading) {
        return getTexture(loading, null);
    }

    @Override
    public ResourceLocation getTexture(ResourceLocation loading, ResourceLocation error) {
        if (sequences != null) {
            return getFromSequences();
        }
        if (exception) return error;
        if (frames == null) {
            decode();
            return loading;
        }
        if (!frames.isDone()) return loading;
        try {
            upload();
        } catch (Exception e) {
            PanguCore.getLogger().error("Couldn't load gif for " + id, e);
            exception = true;
            return error;
        }
        return getFromSequences();
    }

    public ResourceLocation getFromSequences() {
        return sequences.get((int) (System.currentTimeMillis() % this.duration));
    }

    public abstract void readGifImage(GifDecoder decoder);

    @Getter
    @AllArgsConstructor
    public static class Frame {
        protected BufferedImage image;
        protected int delay;
    }
}
