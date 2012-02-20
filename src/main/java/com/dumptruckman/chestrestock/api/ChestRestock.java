package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.locale.Messaging;
import org.bukkit.plugin.Plugin;

public interface ChestRestock extends Messaging, Plugin {

    Config getSettings();
    
    ChestData getData();
}
