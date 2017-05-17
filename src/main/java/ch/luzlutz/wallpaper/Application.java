package ch.luzlutz.wallpaper;

import ch.luzlutz.wallpaper.service.FileUtils;
import ch.luzlutz.wallpaper.service.WallpaperSwitcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

/**
 * Created by luzius on 21.04.17.
 */

@SpringBootApplication
@Controller
public class Application implements CommandLineRunner {
    @Autowired
    private Config config;
    @Autowired
    private FileUtils fileUtils;

    private WallpaperSwitcher switcher;

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public void setConfig(Config config) {
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
            // TODO: move the homeDir to the service.
            String homeDir = fileUtils.resolve(config.getActiveDownload().getTargetDir());
            while (true) {
                log.info("Getting new unsplash image and set it to background.");
                WallpaperSwitcher ws = c.createSwitcher(homeDir);
                // do delete already downloaded files.
                if (c.cleanup != null) {
                    fileUtils.cleanDirectoryAndKeepNewest(homeDir, ws.getPrefix() + "-" + ".*\\.jpg",
                            c.cleanup.getKeepNewestFiles());
                }

                ws.applyWallpaper(ws.downloadImage());
                Thread.sleep(config.interval);
            }
        } catch (InterruptedException e) {
            log.error("Message: " + e.getMessage());
        }
    }

}
