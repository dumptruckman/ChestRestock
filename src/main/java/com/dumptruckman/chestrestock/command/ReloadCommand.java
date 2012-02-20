package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.util.Perm;
import com.dumptruckman.chestrestock.util.locale.Message;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Enables debug-information.
 */
public class ReloadCommand extends PluginCommand {

    public ReloadCommand(ChestRestockPlugin plugin) {
        super(plugin);
        this.setName("Reloads config file");
        this.setCommandUsage("/sc reload");
        this.setArgRange(0, 0);
        this.addKey("sc reload");
        this.addKey("screload");
        this.setPermission(Perm.COMMAND_RELOAD.getPermission());
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        plugin.reloadConfig();
        messager.normal(Message.RELOAD_COMPLETE, sender);
    }
}
