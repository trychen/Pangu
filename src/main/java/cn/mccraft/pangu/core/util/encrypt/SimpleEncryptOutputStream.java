package cn.mccraft.pangu.core.util.encrypt;

import java.io.IOException;
import java.io.OutputStream;

public class SimpleEncryptOutputStream extends OutputStream {
    private final OutputStream outputStream;
    private int i = -1;
    protected int resetCount = 1;

    public SimpleEncryptOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void write(int b) throws IOException {
        i++;
        if (i == resetCount) {
            i = -1;
            outputStream.write(b ^ getKey());
        } else {
            outputStream.write(b);
        }
    }

    protected int getKey() {
        return 1787;
    }
}
