package cn.mccraft.pangu.core.util.image;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.util.LocalCache;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.*;

public class RemoteImage implements TextureProvider {
    @Getter
    @Setter
    private ResourceLocation missingTexture;

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    @Getter
    private final URL url;
    @Getter
    private String id;

    @Getter
    private File cachedFilePath;

    @Getter
    private DynamicTexture dynamicTexture;

    private Future<BufferedImage> bufferedImage;
    private ResourceLocation resourceLocation;

    public RemoteImage(String urlPath, ResourceLocation missingTexture) throws MalformedURLException {
        this.url = new URL(urlPath);
        this.id = Base64.getEncoder().encodeToString(urlPath.getBytes());
        this.missingTexture = missingTexture;
        this.cachedFilePath = LocalCache.getCachePath("images", id);

        bufferedImage = EXECUTOR.submit(() -> {
                if (!cachedFilePath.exists()) {
                    PanguCore.getLogger().info("Start fetching image from " + url.toString());
                    DataInputStream dataInputStream = new DataInputStream(url.openStream());
                    FileOutputStream fileOutputStream = new FileOutputStream(cachedFilePath);
                    ByteArrayOutputStream output = new ByteArrayOutputStream();

                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = dataInputStream.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }
                    fileOutputStream.write(output.toByteArray());
                    fileOutputStream.flush();
                    dataInputStream.close();
                    fileOutputStream.close();
                    PanguCore.getLogger().info("Saved " + urlPath + " to " + cachedFilePath.getAbsolutePath());
                } else PanguCore.getLogger().info("Loading image " + urlPath + " from local " + cachedFilePath.getAbsolutePath());
                return ImageIO.read(cachedFilePath);
            });
    }

    @Override
    public ResourceLocation getTexture() {
        if (!bufferedImage.isDone()) return missingTexture;
        if (resourceLocation == null) {
            LocalCache.markFileUsed(cachedFilePath.toPath());
            try {
                resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("custommenu_banner_" + id, this.dynamicTexture = new DynamicTexture(this.bufferedImage.get()));
            } catch (Exception e) {
                PanguCore.getLogger().error("Couldn't load remote image from url " + url.toString(), e);
                bufferedImage = new Future<BufferedImage>() {
                    @Override
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        return false;
                    }

                    @Override
                    public boolean isCancelled() {
                        return false;
                    }

                    @Override
                    public boolean isDone() {
                        return false;
                    }

                    @Override
                    public BufferedImage get() {
                        return null;
                    }

                    @Override
                    public BufferedImage get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                        return null;
                    }
                };
                return missingTexture;
            }
        }
        return resourceLocation;
    }

    public static TextureProvider of(String url, ResourceLocation missingTexture) {
        try {
            return new RemoteImage(url, missingTexture);
        } catch (Exception e){
            PanguCore.getLogger().error("Couldn't load remote resourceLocation",  e);
            return new BuiltinImage(missingTexture);
        }
    }

    public BufferedImage getBufferedImage() {
        if (!bufferedImage.isDone()) return null;
        try {
            return bufferedImage.get();
        } catch (Exception e) {
        }
        return null;
    }
}
