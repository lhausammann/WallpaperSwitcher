package ch.luzlutz.service.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by luzius on 13.05.17.
 */
public abstract class AbstractImage implements DownloadService {



    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public File downloadImage(String dir, String prefix, String endpoint) {
        String address = resolveUrlWithRedirects(endpoint); // follow redirects
        log.info("Downloading image from address:" + endpoint);
        Image image = null;
        URL url = null;
        try {
            url = new URL(endpoint);

            image = ImageIO.read(url);

            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            int rnd = (int) (Math.random() * 32000);
            String target = dir + prefix + "-" + Integer.toString(rnd) + ".jpg";
            FileOutputStream fos = new FileOutputStream(target);
            fos.write(response);
            fos.close();

            File f  = new File(target);
            log.info("Saving to:" + f.getAbsolutePath());
            return f;
        }

        catch (IOException e) {

            log.error(e.getMessage(), e);
        }

        return null;
    }

    /*
    * URL.connect does not resolve redirects, therefore resolve recursively:
    */
    protected String resolveUrlWithRedirects(String url)  {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();

            con.setInstanceFollowRedirects(false);
            con.connect();
            con.getInputStream();

            if (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                String redirectUrl = con.getHeaderField("Location");
                return resolveUrlWithRedirects(redirectUrl);
            }
            log.info("Resolved url is:" +url.toString());
            return url;


        } catch (IOException e) {
            log.error("Exception while resolving url: " + url, e);
            return null;
        }}
}
