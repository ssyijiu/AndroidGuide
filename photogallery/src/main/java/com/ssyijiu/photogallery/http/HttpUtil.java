package com.ssyijiu.photogallery.http;

import android.support.annotation.Nullable;
import com.ssyijiu.common.log.MLog;
import com.ssyijiu.common.util.IOUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ssyijiu on 2017/5/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class HttpUtil {

    @Nullable
    public byte[] getUrlBytes(String urlSpec) {

        HttpURLConnection connection = null;
        ByteArrayOutputStream out = null;
        InputStream in = null;

        try {
            URL url = new URL(urlSpec);
            connection = (HttpURLConnection) url.openConnection();

            out = new ByteArrayOutputStream();
            in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                    ": with " +
                    urlSpec);
            }

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }

            return out.toByteArray();
        } catch (IOException e) {
            return null;
        } finally {
            IOUtil.close(out);
            IOUtil.close(in);
            IOUtil.close(connection);
        }

    }


    public String getUrlString(String urlSpec) throws IOException {

        byte[] resultByte = getUrlBytes(urlSpec);

        if(resultByte != null) {
            String result = new String(resultByte);
            MLog.i("Fetched contents of URL: " + urlSpec + result);
            return result;
        }
        return "";
    }
}
