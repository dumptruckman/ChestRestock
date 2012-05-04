package com.dumptruckman.chestrestock.util;

import com.dumptruckman.chestrestock.api.CRConfig;
import com.dumptruckman.minecraft.pluginbase.config.AbstractYamlConfig;
import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;

import java.io.File;
import java.io.IOException;

public class CommentedConfig extends AbstractYamlConfig<CRConfig> implements CRConfig {

    public CommentedConfig(BukkitPlugin plugin, boolean doComments, File configFile, Class<? extends CRConfig>... configClasses) throws IOException {
        super(plugin, doComments, true, configFile, configClasses);
    }

    @Override
    protected String getHeader() {
        return "";
    }
}
