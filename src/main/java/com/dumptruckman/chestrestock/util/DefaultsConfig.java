package com.dumptruckman.chestrestock.util;

import com.dumptruckman.chestrestock.api.CRDefaults;
import com.dumptruckman.minecraft.pluginbase.config.AbstractYamlConfig;
import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;

import java.io.File;
import java.io.IOException;

public class DefaultsConfig extends AbstractYamlConfig<CRDefaults> implements CRDefaults {

    public DefaultsConfig(BukkitPlugin plugin, boolean doComments, File configFile, Class<? extends CRDefaults>... configClasses) throws IOException {
        super(plugin, doComments, configFile, configClasses);
    }

    @Override
    protected String getHeader() {
        return "";
    }
}
