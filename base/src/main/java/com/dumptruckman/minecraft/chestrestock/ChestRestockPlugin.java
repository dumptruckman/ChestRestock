package com.dumptruckman.minecraft.chestrestock;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;

public interface ChestRestockPlugin extends PluginBase {

    /**
     * Returns the API object for the ChestRestock plugin.
     * <p/>
     * All useful stuff pertaining to ChestRestock things will be found within the {@link ChestRestockAPI}.
     *
     * @return the API object for the ChestRestock plugin.
     */
    @NotNull
    ChestRestockAPI getChestRestock();
}
