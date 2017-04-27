package wallpaper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import wallpaper.service.FileUtils;
import wallpaper.service.OsStrategy;
import wallpaper.service.WallpaperSwitcher;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by luzius on 24.04.17.
 */

@Component
@EnableConfigurationProperties
@ComponentScan("main.java.service, wallpaper")
@ConfigurationProperties(prefix="app")
public class Config {

    public String test;
    public String strategyClass;
    public int interval = 5000;
    public OsStrategy strategy;
    public boolean cleanup = false;

    private Map<String,WallpaperSwitcher> switchers = new HashMap<String, WallpaperSwitcher>();

    public String getTest() {
        System.out.println("gettint test!");
        return test;
    }

    public void setInterval(int interval) {
        System.out.println("Setting timeout to: " + interval);
        this.interval = interval;
    }

    public void setCleanup(boolean cleanup) {
        System.out.println("Cleanup is: " + cleanup);
        this.cleanup = cleanup;
    }

    public void setStrategyClass(String name) {
        try {
            strategy = (OsStrategy) new FileUtils().createInstance(name);
        } catch (Exception e) {
            System.out.println("Could not create class for name: " + name);
            System.out.println("Exception:" + e.getMessage());
        }
    }



    public void setTest(String test) {
        System.out.println("setting test!" + test);
        this.test = test;
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

}
