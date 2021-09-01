package cn.mccraft.pangu.core.util.image;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.util.render.OpenGL;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public abstract class GifImage extends OpenGLTextureProvider {
    protected static final ExecutorService LOADING_EXECUTOR = Executors.newSingleThreadExecutor(new ThreadFactory() {
        private ThreadGroup group = new ThreadGroup("Pangu Gif Loader Threads");

        public Thread newThread(Runnable r) {
            return new Thread(this.group, r, "Pangu Gif Loader");
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
        frames = LOADING_EXECUTOR.submit(() -> {
            try {
                return decodeFrames();
            } catch (Exception e) {
                PanguCore.getLogger().error("Error while decode GifImage " + id, e);
                exception = true;
                return null;
            }
        });
    }

    /**
     * 加载到 TextureManager
     */
    public void upload() throws ExecutionException, InterruptedException {
        if (duration == 0 && frames.get().isEmpty()) throw new IllegalArgumentException("GIF duration can not be zero " + id);
        else if (duration == 0 && !frames.get().isEmpty()) {
            // zero duration single frame picture
            sequences = new int[1];
            for (Frame frame : frames.get()) {
                sequences[0] = OpenGL.uploadTexture(frame.getImage(), width, height);
                break;
            }
            frames = null;
            duration = 1;
        } else {
            sequences = new int[duration];
            int index = 0;
            for (Frame frame : frames.get()) {
                int id = OpenGL.uploadTexture(frame.getImage(), width, height);
                Arrays.fill(sequences, index, index + frame.delay, id);
                index += frame.delay;
            }
            frames = null;
        }
    }

    public int getFromSequences(long startTime, boolean loop) {
        long offset = (System.currentTimeMillis() - startTime);

        if (!loop && offset >= this.duration) {
            return sequences[sequences.length - 1];
        } else {
            return sequences[(int) (offset % sequences.length)];
        }
    }

    public int getFromSequences() {
        return sequences[(int) (System.currentTimeMillis() % sequences.length)];
    }

    @Override
    public int getTextureID(long startTime, boolean loop) {
        if (sequences != null) {
            return getFromSequences(startTime, loop);
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

        if (sequences.length == 0) {
            PanguCore.getLogger().error("GIF duration can not be zero " + id);
            exception = true;
            sequences = null;
            return -1;
        }

        return getFromSequences(startTime, loop);
    }

    @Override
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

        if (sequences.length == 0) {
            PanguCore.getLogger().error("GIF duration can not be zero " + id);
            exception = true;
            sequences = null;
            return -1;
        }

        return getFromSequences();
    }

    @Override
    public boolean isReady() {
        return getTextureID() > 0;
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
            int delay = d.getDelay(i);
            time += delay == 0 ? 1 : delay;
        }
        width = d.getFrameSize().width;
        height = d.getFrameSize().height;
        duration = time;
        return frames;
    }

    @Override
    public boolean free() {
        if (sequences == null) return true;
        for (int sequence : sequences) {
            GL11.glDeleteTextures(sequence);
        }
        exception = false;
        sequences = null;
        return true;
    }

    @Getter
    @AllArgsConstructor
    public static class Frame {
        protected int[] image;
        protected int delay;
    }
}
