package com.dumptruckman.dchest;

import java.io.File;
import java.io.IOException;
import org.bukkit.util.config.Configuration;

/**
 *
 * @author dumptruckman
 */
public class DChestIO {

    private Configuration data;

    public DChestIO(File file) {
        if(!file.exists()) {
            try {
                System.out.println("creating " + file.getName());
                file.createNewFile();
            } catch (IOException e) { }
        }
        this.data = new Configuration(file);
    }

    public DChestIO(Configuration data) {
        this.data = data;
    }

    public void save() {
        Thread save = new Thread() {
            @Override public void run() {
                data.save();
            }
        };
        save.start();
    }

    public Configuration load() {
        data.load();
        return data;
    }
}
