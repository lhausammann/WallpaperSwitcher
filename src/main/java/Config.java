package Wallpaper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by luzius on 24.04.17.
 */

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix="app")
public class Config {

    public String test;
    public String strategyClass;
    public int interval = 5000;

    public main.java.service.OsStrategy strategy;


    public String getTest() {
        System.out.println("gettint test!");
        return test;
    }

    public void setInterval(int interval) {
        System.out.println("Setting timeout to: " + interval);
        this.interval = interval;
    }

    public void setStrategyClass(String name) {
        try {
            strategy = (main.java.service.OsStrategy) new main.java.service.FileUtils().createInstance(name);
        } catch (Exception e) {
            System.out.println("Could not create class for name: " + name);
        }

    }

    public void setTest(String test) {
        System.out.println("setting test!" + test);
        this.test = test;
    }
}
