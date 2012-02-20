package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.util.locale.Messager;
import com.pneumaticraft.commandhandler.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * A generic Multiverse-command.
 */
public abstract class PluginCommand extends Command {

    /**
     * The reference to the core.
     */
    protected ChestRestockPlugin plugin;
    /**
     * The reference to {@link Messager}.
     */
    protected Messager messager;

    public PluginCommand(ChestRestockPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
        this.messager = this.plugin.getMessager();
    }

    @Override
    public abstract void runCommand(CommandSender sender, List<String> args);

}
