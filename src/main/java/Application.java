package Wallpaper;

import main.java.service.FileUtils;
import main.java.service.MacOsStrategy;
import main.java.service.WallpaperSwitcher;

/**
 * Created by luzius on 21.04.17.
 */
public class Application {

    public static void main(String[] args) {
        try {
            final FileUtils files = new FileUtils();
            String homeDir = files.getHomeDirectory();
            while (true) {
                System.out.println("Getting new unsplash image and set it to background.");
                WallpaperSwitcher ws = new WallpaperSwitcher(homeDir, new MacOsStrategy());
                // do delete already downloaded files.
                files.cleanDirectory(homeDir, ws.getPrefix() + "-" + ".*\\.jpg");
                ws.setWallpaper(ws.downloadImage("https://source.unsplash.com/random/1600x900"));
                Thread.sleep(5000);
            }
        } catch(Exception e) {
            System.out.println("Message: " + e.getMessage());
        }
    }

}
