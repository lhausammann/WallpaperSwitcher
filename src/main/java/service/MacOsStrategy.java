package main.java.service;

import java.io.File;
import java.io.IOException;

/**
 * Created by luzius on 21.04.17.
 *
 */
public class MacOsStrategy implements OsStrategy {
    @Override
    public void switchWallPaper(File file) {
        String as[] = {
                "osascript",
                "-e", "tell application \"Finder\"",
                "-e", "set desktop picture to POSIX file \"" + file.getAbsolutePath() + "\"",
                "-e", "end tell"
        };
        Runtime runtime = Runtime.getRuntime();
        Process process;
        try {
            process = runtime.exec(as);
            //System.out.println(process.exitValue());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Set wallpaper to POSIX file:" + file.getAbsolutePath());
    }
}
