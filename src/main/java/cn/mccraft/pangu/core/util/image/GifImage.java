package cn.mccraft.pangu.core.util.image;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.util.render.OpenGL;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public abstract class GifImage extends OpenGLTextureProvider {
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
    protected int[] sequences;

    @Getter
    protected int duration;

    @Getter
    protected transient int width, height;

    /**
     * 解码 GIF
     */
    public void decode() {
        frames = LOADING_EXECUTOR.submit(this::decodeFrames);
    }

    /**
     * 加载到 TextureManager
     */
    public void upload() throws ExecutionException, InterruptedException {
        sequences = new int[duration];
        int index = 0;
        for (Frame frame : frames.get()) {
            int id = OpenGL.uploadTexture(frame.getImage(), width, height);
            Arrays.fill(sequences, index, index + frame.delay, id);
            index += frame.delay;
        }
        frames = null;
    }

    public int getFromSequences() {
        return sequences[(int) (System.currentTimeMillis() % this.duration)];
    }

    public int getTextureID() {
        if (sequences != null) {
            return getFromSequences();
        }
        if (exception) return -1;
        if (frames == null) {
            decode();
            return 0;
        }
        if (!frames.isDone()) return 0;
        try {
            upload();
        } catch (Exception e) {
            PanguCore.getLogger().error("Couldn't load gif for " + id, e);
            exception = true;
            return -1;
        }
        return getFromSequences();
    }

    public abstract void readGifImage(GifDecoder decoder);

    public List<Frame> decodeFrames() {
        GifDecoder d = new GifDecoder();
        readGifImage(d);
        int time = 0;
        List<Frame> frames = new ArrayList<>(d.getFrameCount() * 2);
        for (int i = 0; i < d.getFrameCount(); i++) {
            BufferedImage frame = d.getFrame(i);
            frames.add(new Frame(OpenGL.buildARGB(frame), d.getDelay(i)));
            time += d.getDelay(i);
        }
        width = d.getFrameSize().width;
        height = d.getFrameSize().height;
        duration = time;
        if (duration == 0) throw new IllegalArgumentException("GIF duration can not be zero " + id);
        return frames;
    }

    @Getter
    @AllArgsConstructor
    public static class Frame {
        protected int[] image;
        protected int delay;
    }
}
