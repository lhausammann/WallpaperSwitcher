package Wallpaper;

import main.java.service.*;
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
    public Wallpaper.Config config;

    public void setConfig(Wallpaper.Config config) {
        this.config = config;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public void run(String... args) {
        try {
            Config c = config;
            System.out.println("Test:" + c + c.getTest());
            final FileUtils files = new main.java.service.FileUtils();
            String homeDir = files.getHomeDirectory();
            while (true) {
                System.out.println("Getting new unsplash image and set it to background.");
                main.java.service.WallpaperSwitcher ws = new main.java.service.WallpaperSwitcher(homeDir, config.strategy);
                // do delete already downloaded files.
                files.cleanDirectory(homeDir, ws.getPrefix() + "-" + ".*\\.jpg");
                ws.applyWallpaper(ws.downloadImage("https://source.unsplash.com/random/1600x900"));
                Thread.sleep(config.interval);
            }
        } catch (InterruptedException e) {
            System.out.println("Message: " + e.getMessage());
        }
    }

}
