package cn.mccraft.pangu.core.util.safe;

import java.io.*;

public class EncryptInputStream extends FilterInputStream {
    protected transient int[] PASS = {0xEF, 0xC5, 0x62, 0xA9, 0x99, 0x12};
    private transient int current = 0;

    /**
     * Creates a <code>FilterInputStream</code>
     * by assigning the  argument <code>in</code>
     * to the field <code>this.in</code> so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or <code>null</code> if
     *           this instance is to be created without an underlying stream.
     */
    protected EncryptInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        if (current >= PASS.length - 1) {
            current = 0;
        }

        return super.read()^PASS[current++];

    }
}
