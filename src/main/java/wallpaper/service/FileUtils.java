package wallpaper.service;

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
    public void cleanDirectory(String dir, String regexPattern) {
        final String regex = regexPattern;
        final File folder = new File(dir);
        final File[] files = folder.listFiles( new FilenameFilter() {
            @Override
            public boolean accept( final File dir,
                                   final String name ) {
                System.out.println("File is: " + dir.getAbsolutePath() + " pattern: " + regex );
                return name.matches(regex);
            }
        } );
        System.out.println(files.length);
        for (final File file : files) {
            System.out.println("Trying deleting folder: " + file.getAbsolutePath());
            if (file.delete()) {
                System.out.println("folder has benn deleted.");
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
                System.out.println("File is: " + dir.getAbsolutePath() + " pattern: " + regex );
                return name.matches(regex);
            }
        } );

        Arrays.sort(files, new Comparator<File>(){
            public int compare(File a, File b) {
                return Long.valueOf(b.lastModified()).compareTo(a.lastModified());
            }});

        for (int i = keepNewest; i < files.length; i++) {
            final File file = files[i];
            System.out.println("Trying deleting file: " + file.getAbsolutePath());
            if (file.delete()) {
                System.out.println("folder has benn deleted.");
            }
        }
    }

    public String getHomeDirectory() {
        return FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
    }

    // TODO: this belongs not really to fileUtils...
    public Object createInstance(String className) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = Class.forName(className);
        Constructor<?> ctor = clazz.getConstructor();
        return ctor.newInstance();
    }

}