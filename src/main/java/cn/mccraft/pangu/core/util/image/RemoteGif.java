package cn.mccraft.pangu.core.util.image;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.util.Http;
import cn.mccraft.pangu.core.util.LocalCache;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class RemoteGif extends GifImage {
    protected static final ExecutorService FETCHER_EXECUTOR = Executors.newFixedThreadPool(5, new ThreadFactory() {
        private ThreadGroup group = new ThreadGroup("Pangu Gif Image Fetcher Threads");
        private int index = 1;

        public Thread newThread(Runnable r) {
            return new Thread(this.group, r, "Remote Gif Image Thread #" + this.index++);
        }
    });
    @Getter
    protected final String urlPath;

    @Getter
    protected final URI url;

    @Getter
    protected transient boolean loaded = false;

    @Getter
    protected File cachedFilePath;


    protected RemoteGif(String urlPath) throws URISyntaxException {
        this.urlPath = urlPath;
        this.url = new URI(urlPath);
        this.id = Base64.getEncoder().encodeToString(urlPath.getBytes());
        this.cachedFilePath = createCachedFilePath();
        LocalCache.markFileUsed(cachedFilePath.toPath());

        FETCHER_EXECUTOR.submit(() -> {
            try {
                if (!cachedFilePath.exists()) {
                    PanguCore.getLogger().debug("Start fetching gif image from " + url.toString());
                    saveImage();
                    PanguCore.getLogger().debug("Saved gif " + urlPath + " to " + cachedFilePath.getAbsolutePath());
                } else
                    PanguCore.getLogger().debug("Loading gif image " + urlPath + " from local " + cachedFilePath.getAbsolutePath());
            } catch (Exception e) {
                PanguCore.getLogger().error("Error while fetching or reading gif image " + url.toString(), e);
                exception = true;
            }
            loaded = true;
        });
    }

    public static RemoteGif of(String url) {
        try {
            return new RemoteGif(url);
        } catch (Exception e) {
            PanguCore.getLogger().error("Couldn't load gif", e);
            return null;
        }
    }

    public static TextureProvider of(String url, ResourceLocation missingTexture) {
        RemoteGif image = of(url);
        if (image == null) return new BuiltinImage(missingTexture);
        return image;
    }

    @Override
    public int getTextureID() {
        if (!loaded) return -1;
        return super.getTextureID();
    }

    protected void saveImage() throws IOException {
        Http.download(url, cachedFilePath);
    }

    public File createCachedFilePath() {
        return LocalCache.getCachePath("gif", id);
    }

    @Override
    public void readGifImage(GifDecoder decoder) {
        try {
            decoder.read(new FileInputStream(cachedFilePath));
        } catch (FileNotFoundException e) {
            PanguCore.getLogger().error("Couldn't load gif", e);
            exception = true;
        }
    }
}
