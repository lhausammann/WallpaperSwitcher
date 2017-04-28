package wallpaper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import wallpaper.service.FileUtils;
import wallpaper.service.WallpaperSwitcher;

/**
 * Created by luzius on 21.04.17.
 */

@SpringBootApplication
@Controller
public class Application implements CommandLineRunner {
    @Autowired
    private wallpaper.Config config;
    @Autowired
    private FileUtils fileUtils;

    private WallpaperSwitcher switcher;

    public void setConfig(wallpaper.Config config) {
        this.config = config;
    }

    public void setUtils(FileUtils utils) {
        this.fileUtils = utils;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public void run(String... args) {
        try {
            Config c = config;
            String homeDir = fileUtils.getHomeDirectory();
            while (true) {
                System.out.println("Getting new unsplash image and set it to background.");
                WallpaperSwitcher ws = c.createSwitcher(homeDir);
                // do delete already downloaded files.
                if (c.cleanup != null) {
                    fileUtils.cleanDirectoryAndKeepNewest(homeDir, ws.getPrefix() + "-" + ".*\\.jpg",
                            c.cleanup.getKeepNewestFiles());
                }
                ws.applyWallpaper(ws.downloadImage("https://source.unsplash.com/random/1600x900"));
                Thread.sleep(config.interval);
            }
        } catch (InterruptedException e) {
            System.out.println("Message: " + e.getMessage());
        }
    }

}
