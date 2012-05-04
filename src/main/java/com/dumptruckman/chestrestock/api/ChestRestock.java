package com.dumptruckman.chestrestock.api;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import org.bukkit.plugin.Plugin;

public interface ChestRestock extends BukkitPlugin<CRConfig>, Plugin {

    ChestManager getChestManager();

    CRDefaults getDefaults(String world);

    void createDefaultIfNoneExists(String world);

    boolean defaultsExistForWorld(String world);

    LootConfig getLootConfig();

    boolean hasChestManagerLoaded();
}
