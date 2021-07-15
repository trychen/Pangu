package cn.mccraft.pangu.core.util.image;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.util.Base64Utils;
import cn.mccraft.pangu.core.util.Games;
import cn.mccraft.pangu.core.util.Http;
import cn.mccraft.pangu.core.util.LocalCache;
import cn.mccraft.pangu.core.util.render.OpenGL;
import com.googlecode.pngtastic.core.PngImage;
import com.trychen.bytedatastream.ByteDeserializable;
import com.trychen.bytedatastream.ByteSerializable;
import com.trychen.bytedatastream.DataOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

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
    protected transient boolean exception = false, loaded = false, requested = false;

    @Getter
    protected File cachedFilePath;

    @Getter
    @Setter
    protected boolean keepBufferedImage;

    @Getter
    protected int width, height;

    protected Future<ImageBuffer> imageBuffer;

    protected RemoteImage(String urlPath) throws URISyntaxException {
        this.urlPath = urlPath;
        this.url = new URI(urlPath);
        this.id = Base64Utils.safeUrlBase64Encode(urlPath.getBytes());
        this.cachedFilePath = createCachedFilePath();
        LocalCache.markFileUsed(cachedFilePath.toPath());

//        requested = true;
//        fetch();
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

    @Override
    public int getTextureID() {
        if (textureID > 0) return textureID;
        if (!requested) {
            requested = true;
            fetch();
        }
        if (!loaded) return 0;
        if (exception) return -1;
        if (imageBuffer == null) {
            imageBuffer = LOADING_EXECUTOR.submit(this::buildBuffer);
            return 0;
        }
        if (!imageBuffer.isDone()) return 0;
        try {
            width = imageBuffer.get().getWidth();
            height = imageBuffer.get().getHeight();
            textureID = OpenGL.uploadTexture(imageBuffer.get().getBuffer(), width, height);
            if (!isKeepBufferedImage())imageBuffer = null;
            PanguCore.getLogger().debug("Uploaded image for " + url.toString());
        } catch (Exception e) {
            PanguCore.getLogger().error("Couldn't load remote image from url " + url.toString(), e);
            exception = true;
            return -1;
        }
        return textureID;
    }

    @Override
    public boolean isReady() {
        return getTextureID() > 0;
    }

    @Override
    public void serialize(DataOutput out) throws IOException {
        out.writeUTF(urlPath);
    }

    public ImageBuffer buildBuffer() throws IOException {
        if (id.endsWith(".png")) {
            try {
                PngImage image = new PngImage(new FileInputStream(cachedFilePath));
                int[] argb = PngReader.INSTANCE.readARGB8(image);
                return new ImageBuffer(argb, (int) image.getWidth(), (int) image.getHeight());
            } catch (FileNotFoundException e) {
                exception = true;
                PanguCore.getLogger().error("Image file not exists " + url.toString());
                return null;
            } catch (Exception e) {
                PanguCore.getLogger().warn("Error while loading png " + url.toString() + " downgrade to ImageIO", e);
            }
        }

        BufferedImage image = readImage();
        if (image == null) {
            exception = true;
            PanguCore.getLogger().error("Image file not exists " + url.toString());
            return null;
        }
        return new ImageBuffer(OpenGL.buildARGB(image), image.getWidth(), image.getHeight());
    }

    @Override
    public TextureAtlasSprite asAtlasSprite() {
        if (!isLoaded()) return null;
        PackagedTextureAtlasSprite sprite = new PackagedTextureAtlasSprite();
        if (!sprite.initPackage(getCachedFilePath())) return null;
        return sprite;
    }

    @Deprecated
    protected BufferedImage readImage() throws IOException {
        return ImageIO.read(cachedFilePath);
    }

    protected void saveImage() throws IOException {
        Http.download(url, cachedFilePath);
    }

    public File createCachedFilePath() {
        return LocalCache.get("images", id).toFile();
    }

    @Override
    public boolean free() {
        if (textureID == 0) return true;
        GL11.glDeleteTextures(textureID);
        textureID = 0;
        exception = false;
        loaded = false;
        if (!isKeepBufferedImage()) imageBuffer = null;
        PanguCore.getLogger().debug("Free image " + url.toString());
        return true;
    }

    @Override
    public void remove() {
        cachedFilePath.delete();
        free();
        PanguCore.getLogger().debug("Remove image " + url.toString());
    }

    @Override
    public void refresh() {
        remove();
        fetch();
        PanguCore.getLogger().debug("Refresh image " + url.toString());
    }

    public void fetch() {
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

    @Data
    @AllArgsConstructor
    public static class ImageBuffer {
        protected int[] buffer;
        protected int width, height;
    }
}