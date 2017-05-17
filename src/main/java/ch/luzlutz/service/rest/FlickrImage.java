package ch.luzlutz.service.rest;

/**
 * Created by luzius on 09.05.17.
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luzius on 29.04.17.
 */
public class FlickrImage extends AbstractImage implements DownloadService {

    private String endpoint;
    private String prefix;
    private String parameters;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private DownloadService downloadService;

    @Override
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public void setParameters(Map<String, String> properties) {
        // no credentials needed - do nothing.
    }

    /**
     * This method will access the first 500 public images by the given person and
     *  choose one from there.
     *  @param dir The directory to save the images.
     */
    @Override
    public File downloadImage(String dir) {
        String address = resolveUrlWithRedirects(this.endpoint); // follow redirects
        log.info("Downloading image from address:" + endpoint);
        Image image = null;
        URL url = null;
        try {
            // read data into stream
            url = new URL(endpoint + "&per_page=1&page=1");
            Map<String, ? extends Object> data = getInfo(url);
            Map<String, ? extends Object> info = (Map<String, ? extends Object>) data.get("photos");
            Integer page = (Integer) info.get("pages");
            // get a random page to access.
            Integer rnd = (int) Math.floor((Math.random() * page)) + 1;
            log.error("Downloading image from address:" + page.toString());

            // access a random page
            url = new URL(endpoint + "&per_page=1&page=" + rnd.toString());
            log.error("url is:" + url);
            Map<String, ? extends Object> i = getInfo(url);
            Map<String, ? extends Object> im = (Map<String, ? extends Object>) i.get("photos");
            List<Map<String, String>> photo = (List<Map<String, String>>) im.get("photo");
            Map<String, ? extends Object> img = photo.get(0);
            log.error("Img is: " + img);
            String secret = (String) img.get("secret");
            log.error("secret is: " + secret);
            Integer farm = (Integer) img.get("farm");
            log.error("farm is: " + farm);
            String server = (String) img.get("server");
            log.error("server is: " + server);
            String id = (String) img.get("id");
            log.error("id is: " + id);

            String downloadUrl = getFlickrUrl(farm.toString(), server.toString(), secret, id.toString());
            log.error("url is: " + downloadUrl);
            return downloadImage(dir, prefix, downloadUrl);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }


    }

    protected Map<String, ? extends Object>getInfo(URL url) throws IOException {
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1 != (n = in.read(buf))) {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(out.toString(), new TypeReference<HashMap>(){});
    }

    private String getFlickrUrl(String farmId, String serverId, String secret, String imageId) {
        log.error(farmId, serverId, imageId, secret);
        //String ret = "https://farm%i.staticflickr.com/%s/%s_%s _k.jpg".format(farmId, serverId, imageId, secret);
        String url = "https://farm" + farmId + ".staticflickr.com/" + serverId + "/" + imageId + "_" + secret + "_b" +
                ".jpg";
        log.error("url:" + url);
        return url;
        // https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg

    }

}

