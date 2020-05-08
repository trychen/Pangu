package cn.mccraft.pangu.core.util.image;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.util.Games;
import cn.mccraft.pangu.core.util.Http;
import cn.mccraft.pangu.core.util.LocalCache;
import cn.mccraft.pangu.core.util.render.OpenGL;
import cn.mccraft.pangu.core.util.render.Rect;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import com.trychen.bytedatastream.ByteDeserializable;
import com.trychen.bytedatastream.ByteSerializable;
import com.trychen.bytedatastream.DataOutput;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.IntBuffer;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class RemoteImage extends OpenGLTextureProvider implements ByteDeserializable, ByteSerializable {

    protected static final ExecutorService FETCHER_EXECUTOR = Executors.newFixedThreadPool(5, new ThreadFactory() {
        private ThreadGroup group = new ThreadGroup("Pangu Remote Image Fetcher Threads");
        private int index = 1;

        public Thread newThread(Runnable r) {
            return new Thread(this.group, r, "Remote Image Thread #" + this.index++);
        }
    });

    protected static final ExecutorService LOADING_EXECUTOR = Executors.newFixedThreadPool(5, new ThreadFactory() {
        private ThreadGroup group = new ThreadGroup("Pangu Remote Image Fetcher Threads");
        private int index = 1;

        public Thread newThread(Runnable r) {
            return new Thread(this.group, r, "Remote Image Thread #" + this.index++);
        }
    });

    private static Map<String, RemoteImage> cachedImages = new HashMap<>();

    @Getter
    protected final String urlPath;
    @Getter
    protected final URI url;
    @Getter
    protected String id;
    @Getter
    protected transient boolean exception = false, loaded = false;
    @Getter
    protected File cachedFilePath;

    @Getter
    @Setter
    protected boolean keepBufferedImage;

    @Getter
    protected int width, height;

    protected Future<BufferedImage> bufferedImage;
    protected CompletableFuture<int[]> imageBuffer = new CompletableFuture<>();

    protected RemoteImage(String urlPath) throws URISyntaxException {
        this.urlPath = urlPath;
        this.url = new URI(urlPath);
        this.id = Base64.getEncoder().encodeToString(urlPath.getBytes());
        this.cachedFilePath = createCachedFilePath();
        LocalCache.markFileUsed(cachedFilePath.toPath());

        FETCHER_EXECUTOR.submit(() -> {
            try {
                if (!cachedFilePath.exists()) {
                    PanguCore.getLogger().debug("Start fetching image from " + url.toString());
                    saveImage();
                    PanguCore.getLogger().debug("Saved " + urlPath + " to " + cachedFilePath.getAbsolutePath());
                } else
                    PanguCore.getLogger().debug("Loading image " + urlPath + " from local " + cachedFilePath.getAbsolutePath());
            } catch (Exception e) {
                PanguCore.getLogger().error("Error while fetching or reading image " + url.toString(), e);
                exception = true;
            }
            loaded = true;
        });
    }

    public static RemoteImage deserialize(DataInput out) throws IOException {
        return of(out.readUTF());
    }

    public static TextureProvider of(String url, ResourceLocation missingTexture) {
        RemoteImage image = of(url);
        if (image == null) return new BuiltinImage(missingTexture);
        return image;
    }

    public static RemoteImage of(String url) {
        RemoteImage remoteImage = cachedImages.get(url);
        if (remoteImage == null) {
            try {
                remoteImage = new RemoteImage(url);
                cachedImages.put(url, remoteImage);
            } catch (Exception e) {
                PanguCore.getLogger().error("Couldn't load remote resourceLocation", e);
                return null;
            }
        }
        return remoteImage;
    }

    @Override
    public ResourceLocation getTexture(ResourceLocation loading, ResourceLocation error) {
        return error;
    }

    protected int textureID = 0;

    public int getTextureID() {
        if (textureID > 0) return textureID;
        if (!loaded) return 0;
        if (exception) return -1;
        if (bufferedImage == null) {
            bufferedImage = LOADING_EXECUTOR.submit(() -> {
                BufferedImage image = readImage();
                imageBuffer.complete(OpenGL.buildARGB(image));
                PanguCore.getLogger().debug("Built image for " + url.toString());
                return image;
            });
            return 0;
        }
        if (!bufferedImage.isDone()) return 0;
        try {
            width = bufferedImage.get().getWidth();
            height = bufferedImage.get().getHeight();
            textureID = OpenGL.uploadTexture(imageBuffer.get(), width, height);
            PanguCore.getLogger().debug("Uploaded image for " + url.toString());
            if (!keepBufferedImage) this.bufferedImage = null;
        } catch (Exception e) {
            PanguCore.getLogger().error("Couldn't load remote image from url " + url.toString(), e);
            exception = true;
            return -1;
        }
        return textureID;
    }

    public BufferedImage getBufferedImage() {
        if (!bufferedImage.isDone()) return null;
        try {
            return bufferedImage.get();
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void serialize(DataOutput out) throws IOException {
        out.writeUTF(urlPath);
    }

    protected BufferedImage readImage() throws IOException {
        return ImageIO.read(cachedFilePath);
    }

    protected void saveImage() throws IOException {
        Http.download(url, cachedFilePath);
    }

    public File createCachedFilePath() {
        return LocalCache.getCachePath("images", id);
    }
}