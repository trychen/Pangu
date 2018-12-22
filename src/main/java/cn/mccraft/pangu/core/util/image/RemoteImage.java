package cn.mccraft.pangu.core.util.image;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RemoteImage implements TextureProvider {
    private final ResourceLocation missingTexture;

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(3);
    private static File CACHE_DIR = new File(Loader.instance().getConfigDir(), "../cache/");

    static {
        if (!CACHE_DIR.exists()) {
            CACHE_DIR.mkdir();
        }
    }

    private final URL url;
    private String id;

    private File cachedFilePath;
    private Future<BufferedImage> bufferedImage;
    private ResourceLocation resourceLocation;
    private DynamicTexture dynamicTexture;

    public RemoteImage(String urlPath, ResourceLocation missingTexture) throws MalformedURLException {
        this.url = new URL(urlPath);
        this.id = Base64.getEncoder().encodeToString(urlPath.getBytes());
        this.missingTexture = missingTexture;
        this.cachedFilePath = new File(CACHE_DIR, id);

        bufferedImage = EXECUTOR.submit(() -> {
                if (!cachedFilePath.exists()) {
                    DataInputStream dataInputStream = new DataInputStream(url.openStream());
                    FileOutputStream fileOutputStream = new FileOutputStream(cachedFilePath);
                    ByteArrayOutputStream output = new ByteArrayOutputStream();

                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = dataInputStream.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }
                    fileOutputStream.write(output.toByteArray());
                    dataInputStream.close();
                    fileOutputStream.close();
                }
                return ImageIO.read(cachedFilePath);
            });
    }

    @Override
    public ResourceLocation getTexture() {
        if (!bufferedImage.isDone()) return missingTexture;
        if (resourceLocation == null) {
            try {
                resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("custommenu_banner_" + id, this.dynamicTexture = new DynamicTexture(this.bufferedImage.get()));
            } catch (Exception e) {
                e.printStackTrace();
                bufferedImage = new CompletableFuture<>();
                bufferedImage.cancel(false);
                return missingTexture;
            }
        }
        return resourceLocation;
    }

    public static TextureProvider of(String url, ResourceLocation missingTexture) {
        try {
            return new RemoteImage(url, missingTexture);
        } catch (Exception e){
            e.printStackTrace();
            return new BuiltinImage(missingTexture);
        }
    }
}
