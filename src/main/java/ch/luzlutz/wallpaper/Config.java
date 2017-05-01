package ch.luzlutz.wallpaper;

import ch.luzlutz.service.rest.DownloadService;
import ch.luzlutz.wallpaper.service.FileUtils;
import ch.luzlutz.wallpaper.service.OsStrategy;
import ch.luzlutz.wallpaper.service.WallpaperSwitcher;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luzius on 24.04.17.
 */

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix="app")
public class Config {
    public String strategyClass;
    public int interval = 5000;
    public OsStrategy strategy;
    public Cleanup cleanup = new Cleanup();

    public Download getDownload() {
        return download;
    }

    public void setDownload(Download download) {
        this.download = download;
    }

    public Download download = new Download();
    private Map<String,WallpaperSwitcher> switchers = new HashMap<String, WallpaperSwitcher>();

    public void setInterval(int interval) {
        System.out.println("Setting timeout to: " + interval);
        this.interval = interval;
    }

    public void setStrategyClass(String name) {
        try {
            strategy = (OsStrategy) new FileUtils().createInstance(name);
        } catch (Exception e) {
            System.out.println("Could not create class for name: " + name);
            System.out.println("Exception:" + e.getMessage());
        }
    }

    public void setCleanup(Cleanup cleanup) {
        this.cleanup = cleanup;
    }

    public Cleanup getCleanup() {
        return cleanup;
    }

    /*
     * Lookup the given switcher.
     */
    public WallpaperSwitcher createSwitcher(String homeDir) {
        if (switchers.get(homeDir) == null) {
            WallpaperSwitcher s = new WallpaperSwitcher(homeDir, strategy, download.createInstance());
            switchers.put(homeDir, s);

            return s;
        }

        return switchers.get(homeDir);
    }

    public  static class Cleanup {
        private int keepNewestFiles = 0;

        public void setKeepNewestFiles(int i) {
            System.out.println("newest files: " + i);
            keepNewestFiles = i;
        }

        public int getKeepNewestFiles() {
            System.out.println("newest files: " + keepNewestFiles);

            return keepNewestFiles;
        }
    }



    public static class Download {
        private String downloadClass;

        // TODO: return same instance everytime.
        public DownloadService createInstance() {
            DownloadService downloadService = null;
            try {
                downloadService = ((DownloadService) new FileUtils().createInstance(this.downloadClass));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            downloadService.setEndpoint(endpoint);
            downloadService.setPrefix(prefix);
            return downloadService;
        }

        public void setDownloadClass(String downloadClass) {
            this.downloadClass = downloadClass;
        }

        private String endpoint;

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        private String prefix;
        private String targetDir;
        private Map<String, String> parameters;


        public Map<String, String> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
    }

}
