package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.CRConfig;
import com.dumptruckman.chestrestock.api.ChestManager;
import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.command.CheckCommand;
import com.dumptruckman.chestrestock.command.CreateCommand;
import com.dumptruckman.chestrestock.command.DisableCommand;
import com.dumptruckman.chestrestock.command.RestockCommand;
import com.dumptruckman.chestrestock.command.SetCommand;
import com.dumptruckman.chestrestock.command.UpdateCommand;
import com.dumptruckman.chestrestock.util.CommentedConfig;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.minecraft.pluginbase.commandhandler.Command;
import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.plugin.command.HelpCommand;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ChestRestockPlugin extends AbstractBukkitPlugin<CRConfig> implements ChestRestock {

    private final List<String> cmdPrefixes = Arrays.asList("cr");

    private ChestManager chestManager = null;

    @Override
    protected CRConfig newConfigInstance() throws IOException {
        return new CommentedConfig(this, true, new File(getDataFolder(), "config.yml"), CRConfig.class);
    }

    public void preEnable() {
        Language.init();
    }
    
    public void postEnable() {
        getServer().getPluginManager().registerEvents(new ChestRestockListener(this), this);
        getCommandHandler().registerCommand(new CreateCommand(this));
        getCommandHandler().registerCommand(new UpdateCommand(this));
        getCommandHandler().registerCommand(new CheckCommand(this));
        getCommandHandler().registerCommand(new DisableCommand(this));
        getCommandHandler().registerCommand(new SetCommand(this));
        getCommandHandler().registerCommand(new RestockCommand(this));
        for (Command c : getCommandHandler().getAllCommands()) {
            if (c instanceof HelpCommand) {
                for (String key : getCommandPrefixes()) {
                    c.addKey(key);
                }
            }
        }
    }

    @Override
    public List<String> getCommandPrefixes() {
        return cmdPrefixes;
    }

    @Override
    public ChestManager getChestManager() {
        if (chestManager == null) {
            chestManager = new DefaultChestManager(this);
        }
        return chestManager;
    }
}
