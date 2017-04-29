package ch.luzlutz.wallpaper;

import ch.luzlutz.wallpaper.service.OsStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import ch.luzlutz.wallpaper.service.FileUtils;
import ch.luzlutz.wallpaper.service.WallpaperSwitcher;

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
            WallpaperSwitcher s = new WallpaperSwitcher(homeDir, strategy);
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

}
