import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by luzius on 21.04.17.
 */
public class WallpaperSwitcher {
    private String prefix = "_wallpaper_";
    
    OsStrategy switchStrategy = null;

    public WallpaperSwitcher(OsStrategy switchStrategy) {
        if (switchStrategy == null) {
            throw new IllegalArgumentException("Cannot be null.");
        }

        this.switchStrategy = switchStrategy;
    }

    public void setWallpaper(File file)
    {
        /* depending on the os, we delgate to the strategy to actually switch the wallpaper. */
        switchStrategy.switchWallPaper(file);

    }

    public File downloadImage(String address) {
        System.out.println("Downloading image from address:" + address);
        Image image = null;
        URL url = null;
        try {
             url = new URL(address);

            image = ImageIO.read(url);

            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            int rnd = (int) (Math.random() * 32000);
            String target = getHomeDirectory() + "/" + prefix + "-" + Integer.toString(rnd) + ".jpg";
            FileOutputStream fos = new FileOutputStream(target);
            fos.write(response);
            fos.close();

            File f  = new File(target);
            System.out.println("Saving to:" + f.getAbsolutePath());
            return f;
        }

        catch (IOException e) {
            e.printStackTrace();

        }

        System.out.println("Error");
        return null;
    }

    private String getHomeDirectory() {
        return FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
    }

    private void cleanDirectory(String dir, String regexPattern) {
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

    /*
     * URL.connect does not resolve redirects, therefore resolve recursively:
     */
    private String resolveUrlWithRedirects(String url)  {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();

            con.setInstanceFollowRedirects(false);
            con.connect();
            con.getInputStream();

            if (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                String redirectUrl = con.getHeaderField("Location");
                return resolveUrlWithRedirects(redirectUrl);
            }
            System.out.println("Resolved url is:" +url.toString());
            return url;


            } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            while (true) {
                System.out.println("Getting new unsplash image and set it to background.");
                WallpaperSwitcher ws = new WallpaperSwitcher(new MacOsStrategy());
                ws.cleanDirectory(ws.getHomeDirectory(), ws.getPrefix() + "-" + ".*\\.jpg");
                ws.setWallpaper(ws.downloadImage(ws.resolveUrlWithRedirects("https://source.unsplash.com/random/1600x900")));
                Thread.sleep(60000);
            }
        } catch(Exception e) {
            System.out.println("Message: " + e.getMessage());
        }
    }

    public String getPrefix() {
        return prefix;
    }
}
