package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.minecraft.pluginbase.plugin.command.PluginCommand;

/**
 * An abstract ChestRestock command.
 */
public abstract class CRCommand extends PluginCommand<ChestRestockPlugin> {
    public CRCommand(ChestRestockPlugin plugin) {
        super(plugin);
    }
}
