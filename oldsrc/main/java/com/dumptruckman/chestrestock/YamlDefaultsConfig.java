package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.CRDefaults;
import com.dumptruckman.minecraft.pluginbase.properties.YamlProperties;

import java.io.File;
import java.io.IOException;

class YamlDefaultsConfig extends YamlProperties implements CRDefaults {

    public YamlDefaultsConfig(boolean autoDefaults, File configFile) throws IOException {
        super(true, autoDefaults, configFile, CRDefaults.class);
    }
}
