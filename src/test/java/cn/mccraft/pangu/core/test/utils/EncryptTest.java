package cn.mccraft.pangu.core.test.utils;

import cn.mccraft.pangu.core.util.encrypt.SimpleEncryptInputStream;
import cn.mccraft.pangu.core.util.encrypt.SimpleEncryptOutputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EncryptTest {
    @Test
    public void testSimple() throws IOException {
        String data = "Hello";
        ByteArrayOutputStream dataOutput = new ByteArrayOutputStream();
        SimpleEncryptOutputStream output = new SimpleEncryptOutputStream(dataOutput);
        output.write(data.getBytes());

        SimpleEncryptInputStream input = new SimpleEncryptInputStream(new ByteArrayInputStream(dataOutput.toByteArray()));

        byte[] dataOut = new byte[data.getBytes().length];
        input.read(dataOut);
    }
}
