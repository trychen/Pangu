package cn.mccraft.pangu.core.util.encrypt;

import java.io.IOException;
import java.io.InputStream;

public class SimpleEncryptInputStream extends InputStream {
    private final InputStream inputStream;
    private int i = -1;
    protected int resetCount = 1;

    public SimpleEncryptInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        i++;
        if (i == resetCount) {
            i = -1;
            return inputStream.read() ^ getKey();
        } else {
            return inputStream.read();
        }
    }

    protected int getKey() {
        return 1787;
    }
}
