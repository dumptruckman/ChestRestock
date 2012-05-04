package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.minecraft.pluginbase.plugin.command.PluginCommand;

/**
 * An abstract ChestRestock command.
 */
public abstract class CRCommand extends PluginCommand<ChestRestock> {

    public CRCommand(ChestRestock plugin) {
        super(plugin);
    }
}
