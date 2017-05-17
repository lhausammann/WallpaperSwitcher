package ch.luzlutz.wallpaper.service;

import ch.luzlutz.service.rest.DownloadService;
import ch.luzlutz.wallpaper.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by luzius on 23.04.17.
 */

public class WallpaperSwitcher {

    private String prefix = "_wallpaper_";
    private String dir = "";
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private DownloadService downloadService;


    OsStrategy switchStrategy = null;

    public WallpaperSwitcher(String directory, OsStrategy switchStrategy, Config.Download active) {
        if (switchStrategy == null) {

            throw new IllegalArgumentException("Cannot be null.");
        }



        prefix = active.getPrefix();

        if (! directory.endsWith("/")) {
            directory = directory + "/";
        }

        this.downloadService = active.createInstance();
        this.dir = directory;
        this.switchStrategy = switchStrategy;
    }

    public void applyWallpaper(File file)
    {
        /* depending on the os, we delgate to the strategy to actually switch the wallpaper. */
        switchStrategy.switchWallPaper(file);

    }

    public File downloadImage() {
        return downloadService.downloadImage(dir);
    }


    public String getPrefix() {
        return prefix;
    }
}
