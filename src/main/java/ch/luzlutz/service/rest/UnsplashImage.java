package ch.luzlutz.service.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

/**
 * Created by luzius on 29.04.17.
 */
public class UnsplashImage extends AbstractImage implements DownloadService {

    private String endpoint;
    private String prefix;

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

    @Override
    public File downloadImage(String dir) {
        return downloadImage(dir, prefix, endpoint);
    }


}
