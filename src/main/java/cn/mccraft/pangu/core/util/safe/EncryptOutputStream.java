package cn.mccraft.pangu.core.util.safe;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EncryptOutputStream extends FilterOutputStream {
    protected transient int[] PASS = {0xEF, 0xC5, 0x62, 0xA9, 0x99, 0x12};
    private transient int current = 0;

    /**
     * Creates an output stream filter built on top of the specified
     * underlying output stream.
     *
     * @param out the underlying output stream to be assigned to
     *            the field <tt>this.out</tt> for later use, or
     *            <code>null</code> if this instance is to be
     *            created without an underlying stream.
     */
    public EncryptOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void write(int b) throws IOException {
        if (current >= PASS.length) {
            current = 0;
        }

        super.write(b^PASS[current]);

        current++;
    }
}
