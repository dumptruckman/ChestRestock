package com.dumptruckman.chestrestock.util;

import com.dumptruckman.chestrestock.api.ChestData;
import com.dumptruckman.chestrestock.api.RestockChest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * Implementation of PlayerData.
 */
public class YamlChestData implements ChestData {

    private static final String YML = ".yml";
    private File chestFolder = null;

    public YamlChestData(JavaPlugin plugin) throws IOException {
        // Make the data folders
        plugin.getDataFolder().mkdirs();

        // Check if the data file exists.  If not, create it.
        this.chestFolder = new File(plugin.getDataFolder(), "chests");
        if (!this.chestFolder.exists()) {
            if (!this.chestFolder.mkdirs()) {
                throw new IOException("Could not create chest folder!");
            }
        }
    }

    private FileConfiguration getConfigHandle(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    private File getFolder(String worldName) {
        File folder = new File(this.chestFolder, worldName);
        if (!folder.exists()) {
            folder.mkdirs();
            Logging.finer("Created chest folder for world: " + worldName);
        }
        Logging.finer("got data folder: " + folder.getPath());
        return folder;
    }

    private File getChestFile(BlockLocation blockLoc) {
        File chestFile = new File(this.getFolder(blockLoc.getWorldName()), blockLoc.toString() + YML);
        if (!chestFile.exists()) {
            try {
                chestFile.createNewFile();
            } catch (IOException e) {
                Logging.severe("Could not create necessary player file: " + blockLoc.toString() + YML);
                Logging.severe("Your data may not be saved!");
                Logging.severe(e.getMessage());
            }
        }
        return chestFile;
    }

    private BlockLocation getBlockLocation(File chestFile) {
        if (chestFile.getName().endsWith(YML)) {
            String fileName = chestFile.getName();
            fileName = fileName.substring(0, fileName.length() - YML.length());
            return BlockLocation.get(fileName);
        } else {
            return null;
        }
    }

    /*
    private File[] getWorldFolders() {
        return this.worldFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(YML);
            }
        });
    }
    */

    /**
     * {@inheritDoc}
     */
    @Override
    public RestockChest getChest(BlockLocation blockLocation) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateChest(RestockChest chest) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeChest(RestockChest chest) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

