package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.CRConfig;
import com.dumptruckman.minecraft.pluginbase.properties.YamlProperties;

import java.io.File;
import java.io.IOException;

class YamlChestRestockConfig extends YamlProperties implements CRConfig {

    public YamlChestRestockConfig(File configFile) throws IOException {
        super(true, true, configFile, CRConfig.class);
    }
}
