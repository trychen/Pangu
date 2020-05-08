package cn.mccraft.pangu.core.util.image;

import cn.mccraft.pangu.core.PanguCore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.CompletableFuture;

@EqualsAndHashCode(callSuper = false)
@Data
public class FileGif extends GifImage {
    private final File file;
    private boolean decodeImmediately = false;
    private boolean uploadImmediately = false;

    public FileGif(File file) {
        this.file = file;
        this.id = file.getAbsolutePath();
    }

    public FileGif(File file, boolean decodeImmediately) {
        this(file);

        if (decodeImmediately) frames = CompletableFuture.completedFuture(decodeFrames());
    }

    @Override
    public void readGifImage(GifDecoder decoder) {
        try {
            decoder.read(new FileInputStream(file));
        } catch (Exception e) {
            PanguCore.getLogger().error("Couldn't load FileGif " + id, e);
            exception = true;
        }
    }

}
