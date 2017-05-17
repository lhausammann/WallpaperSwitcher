package ch.luzlutz.wallpaper;

import ch.luzlutz.service.rest.DownloadService;
import ch.luzlutz.wallpaper.service.FileUtils;
import ch.luzlutz.wallpaper.service.OsStrategy;
import ch.luzlutz.wallpaper.service.WallpaperSwitcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public List<Download> downloads = new ArrayList<Download>();

    public String activeDownloadName = "";



    public Download getActiveDownload() {
        for(Download d : downloads) {

            if (d.getName().equals(activeDownloadName)) return d;
        }

        throw new IllegalArgumentException("Could not find downlad class: " + activeDownloadName);
    }


    public List<Download> getDownloads() {
        log.error("downloads: " + downloads);
        return this.downloads;
    }

    public void setDownloads(List<Download> d) {
        log.error("adding: " + d);
        this.downloads = d;
    }

    public String getActiveDownloadName() {
        return activeDownloadName;
    }

    public void setActiveDownloadName(String activeDownloadName) {
        this.activeDownloadName = activeDownloadName;
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

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
            WallpaperSwitcher s = new WallpaperSwitcher(homeDir, strategy, getActiveDownload());
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

    private Download setActiceDownload(String name) {

        throw new RuntimeException("Could not found download strategy by name. Registered downloads were: ");
    }



    public static class Download {
        @Override
        public String toString() {
            return "Download{" +
                    "downloadClass='" + downloadClass + '\'' +
                    ", endpoint='" + endpoint + '\'' +
                    ", prefix='" + prefix + '\'' +
                    ", targetDir='" + targetDir + '\'' +
                    ", parameters=" + parameters +
                    ", name='" + name + '\'' +
                    '}';
        }

        private String downloadClass;
        private String endpoint;
        private String prefix;
        private String targetDir;
        private Map<String, String> parameters = new HashMap<String, String>();

        public void setName(String name) {
            this.name = name;
        }

        private String name;


        // TODO: return same instance everytime.
        public DownloadService createInstance() {
            DownloadService downloadService = null;
            System.out.println(parameters);
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
            downloadService.setEndpoint(resolveParameters(endpoint, parameters));
            downloadService.setPrefix(resolveParameters(prefix, parameters));
            return downloadService;
        }

        public void setDownloadClass(String downloadClass) {
            this.downloadClass = downloadClass;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }


        public String getTargetDir() {
            return targetDir;
        }

        public void setTargetDir(String targetDir) {
            this.targetDir = targetDir;
        }


        public Map<String, String> getParameters() {
            System.out.println(parameters);
            return parameters;
        }


        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getName() {

            return name;
        }


    }

    public static String resolveParameters(String source, Map<String, String> parameters) {
        String resolved = source;
        for (String param : parameters.keySet()) {
            String placeholder = "$" + param;
            System.out.println("param is: " + placeholder);

            String replacement = parameters.get(param);
            System.out.println("replace is: " + replacement);
            resolved = resolved.replace(placeholder, replacement);
        }

        return resolved;
    }
}
