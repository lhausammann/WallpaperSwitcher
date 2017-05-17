package ch.luzlutz.wallpaper.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by luzius on 23.04.17.
 *
 * Clean a directory by deleting all files using a regex pattern.
 */

@Service
@ComponentScan
public class FileUtils {
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public void cleanDirectory(String dir, String regexPattern) {
        final String regex = regexPattern;
        final File folder = new File(dir);
        final File[] files = folder.listFiles( new FilenameFilter() {
            @Override
            public boolean accept( final File dir,
                                   final String name ) {
                log.debug("File is: " + dir.getAbsolutePath() + " pattern: " + regex );
                return name.matches(regex);
            }
        } );
        log.debug("" + files.length);
        for (final File file : files) {
            log.info("Trying deleting folder: " + file.getAbsolutePath());
            if (file.delete()) {
                log.info("folder has benn deleted.");
            }
        }
    }

    /**
     * Keeps the keepNewest newest files
     * @param dir The dir to delete files
     * @param regex The regex to match
     * @param keepNewest The n newest files to keep
     */
    public void cleanDirectoryAndKeepNewest(String dir, final String regex, int keepNewest) {
        final File folder = new File(dir);
        final File[] files = folder.listFiles( new FilenameFilter() {
            @Override
            public boolean accept( final File dir,
                                   final String name ) {
                log.info("File is: " + dir.getAbsolutePath() + " pattern: " + regex );
                return name.matches(regex);
            }
        } );

        Arrays.sort(files, new Comparator<File>(){
            public int compare(File a, File b) {
                return Long.valueOf(b.lastModified()).compareTo(a.lastModified());
            }});

        for (int i = keepNewest; i < files.length; i++) {
            final File file = files[i];
            log.info("Trying deleting file: " + file.getAbsolutePath());
            if (file.delete()) {
                log.info("folder has been deleted.");
            }
        }
    }

    public String getHomeDirectory() {
        return FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
    }

    public String resolve(String path) {
        log.info("path is: " + path);
        if (path.trim().startsWith("~")) {

            return getHomeDirectory() + File.separator + path.substring(1);
        }

        return path;
    }

    // TODO: this belongs not really to fileUtils...
    public Object createInstance(String className) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = Class.forName(className);
        Constructor<?> ctor = clazz.getConstructor();
        return ctor.newInstance();
    }

}
