package com.dumptruckman.chestrestock.util;

import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.api.Config;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * Commented Yaml implementation of Config.
 */
public class CommentedConfig implements Config {

    /**
     * Enum for easily keeping track of config paths, defaults and comments.
     */
    public enum Path {
        /**
         * Add a comment to the top of file.
         */
        SETTINGS("settings", null, "# ===[ SimpleCircuits Config ]==="),
        /**
         * Locale name config path, default and comments.
         */
        LANGUAGE_FILE_NAME("settings.locale", "en", "# This is the locale you wish to use."),
        /**
         * Debug Mode config path, default and comments.
         */
        DEBUG_MODE("settings.debug_level", 0, "# 0 = off, 1-3 display debug info with increasing granularity."),
        /**
         * First Run flag config path, default and comments.
         */
        FIRST_RUN("settings.first_run", true, "# Will make the plugin perform tasks only done on a first run."
                + "worlds.");

        private String path;
        private Object def;
        private String[] comments;

        Path(String path, Object def, String... comments) {
            this.path = path;
            this.def = def;
            this.comments = comments;
        }

        /**
         * Retrieves the path for a config option.
         *
         * @return The path for a config option.
         */
        private String getPath() {
            return this.path;
        }

        /**
         * Retrieves the default value for a config path.
         *
         * @return The default value for a config path.
         */
        private Object getDefault() {
            return this.def;
        }

        /**
         * Retrieves the comment for a config path.
         *
         * @return The comments for a config path.
         */
        private String[] getComments() {
            if (this.comments != null) {
                return this.comments;
            }

            String[] emptyComments = new String[1];
            emptyComments[0] = "";
            return emptyComments;
        }
    }

    private CommentedYamlConfiguration config;
    private ChestRestock plugin;

    public CommentedConfig(ChestRestock plugin) throws Exception {
        this.plugin = plugin;
        // Make the data folders
        if (this.plugin.getDataFolder().mkdirs()) {
            Logging.fine("Created data folder.");
        }

        // Check if the config file exists.  If not, create it.
        File configFile = new File(this.plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            if (configFile.createNewFile()) {
                Logging.fine("Created config file.");
            }
        }

        // Load the configuration file into memory
        config = new CommentedYamlConfiguration(configFile);
        config.load();

        // Sets defaults config values
        this.setDefaults();

        // Saves the configuration from memory to file
        config.save();
    }

    /**
     * Loads default settings for any missing config values.
     */
    private void setDefaults() {
        for (CommentedConfig.Path path : CommentedConfig.Path.values()) {
            config.addComment(path.getPath(), path.getComments());
            if (this.getConfig().get(path.getPath()) == null) {
                if (path.getDefault() != null) {
                    Logging.fine("Config: Defaulting '" + path.getPath() + "' to " + path.getDefault());
                    this.getConfig().set(path.getPath(), path.getDefault());
                }
            }
        }
    }

    private Boolean getBoolean(Path path) {
        return this.getConfig().getBoolean(path.getPath(), (Boolean) path.getDefault());
    }

    private Integer getInt(Path path) {
        return this.getConfig().getInt(path.getPath(), (Integer) path.getDefault());
    }

    private String getString(Path path) {
        return this.getConfig().getString(path.getPath(), (String) path.getDefault());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileConfiguration getConfig() {
        return this.config.getConfig();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save() {
        this.config.save();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGlobalDebug(int globalDebug) {
        this.getConfig().set(Path.DEBUG_MODE.getPath(), globalDebug);
        Logging.setDebugMode(globalDebug);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getGlobalDebug() {
        return this.getInt(Path.DEBUG_MODE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocale() {
        return this.getString(Path.LANGUAGE_FILE_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFirstRun() {
        return this.getBoolean(Path.FIRST_RUN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFirstRun(boolean firstRun) {
        this.getConfig().set(Path.FIRST_RUN.getPath(), firstRun);
    }
}
