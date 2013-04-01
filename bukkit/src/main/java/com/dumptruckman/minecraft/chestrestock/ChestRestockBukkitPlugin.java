package com.dumptruckman.minecraft.chestrestock;

import com.dumptruckman.minecraft.pluginbase.bukkit.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.bukkit.properties.YamlProperties;
import com.dumptruckman.minecraft.pluginbase.messages.PluginBaseException;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ChestRestockBukkitPlugin extends AbstractBukkitPlugin implements ChestRestockPlugin {

    private static final String COMMAND_PREFIX = "cr";

    /** {@inheritDoc} */
    @NotNull
    @Override
    protected Properties getNewConfig() throws PluginBaseException {
        return new YamlProperties.Loader(getLog(), new File(getDataFolder(), "config.yml"), CRConfig.class).load();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getCommandPrefix() {
        return COMMAND_PREFIX;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean useDatabase() {
        return false;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public ChestRestockAPI getChestRestock() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
