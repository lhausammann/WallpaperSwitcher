package ch.luzlutz.service.rest;

import java.io.File;
import java.util.Map;

/**
 * Created by luzius on 26.04.17.
 */
public interface DownloadService {
    // TODO: Move general logic here and subclass (unsplash, instagram, flickr...)
    public void setEndpoint(String endpoint);

    public void setPrefix(String prefix);

    public String getPrefix();

    public void setParameters(Map<String, String> properties);

    /**
     * Uploads an image into the given directory.
     * @param dir The directory to upload the image into
     * @return The uploaded file
     */
    public File downloadImage(String dir);
}
