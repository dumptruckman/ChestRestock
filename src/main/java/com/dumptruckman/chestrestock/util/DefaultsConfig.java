package com.dumptruckman.chestrestock.util;

import com.dumptruckman.chestrestock.api.CRDefaults;
import com.dumptruckman.minecraft.pluginbase.config.AbstractYamlConfig;
import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;

import java.io.File;
import java.io.IOException;

public class DefaultsConfig extends AbstractYamlConfig<CRDefaults> implements CRDefaults {

    public DefaultsConfig(BukkitPlugin plugin, boolean autoDefaults, File configFile) throws IOException {
        super(plugin, true, autoDefaults, configFile, CRDefaults.class);
    }

    @Override
    protected String getHeader() {
        return "";
    }
}
