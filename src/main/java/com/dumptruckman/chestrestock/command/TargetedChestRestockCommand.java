package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.api.ChestManager;
import com.dumptruckman.chestrestock.util.Language;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class TargetedChestRestockCommand extends CRCommand {

    protected ChestManager chestManager;

    public TargetedChestRestockCommand(ChestRestockPlugin plugin) {
        super(plugin);
        chestManager = plugin.getChestManager();
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player)) {
            messager.bad(Language.IN_GAME_ONLY, sender);
            return;
        }
        Player player = (Player) sender;
        Block holder = null;
        try {
            holder = chestManager.getTargetedInventoryHolder(player);
        } catch (IllegalStateException e) {
            messager.sendMessage(sender, e.getMessage());
            return;
        }
        runCommand(player, holder, args);
    }

    public abstract void runCommand(Player player, Block holder, List<String> args);
}
