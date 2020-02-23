package cn.mccraft.pangu.core.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public interface Http {
    static void download(URI uri, File saveTo) throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);
        HttpResponse response = client.execute(httpget);
        if (response.getEntity() == null || response.getStatusLine().getStatusCode() != 200) {
            throw new IOException("Couldn't download from " + uri.toString() + ", status code: " + response.getStatusLine().getStatusCode() + ", reason: "  + response.getStatusLine().getReasonPhrase());
        }
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        FileOutputStream fileOutputStream = new FileOutputStream(saveTo);
        byte[] buffer = new byte[1024 * 10];
        int ch;
        while ((ch = is.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, ch);
        }
        is.close();
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
