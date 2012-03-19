package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.api.ChestManager;
import com.dumptruckman.chestrestock.util.Language;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class TargetedChestRestockCommand extends CRCommand {

    protected ChestManager chestManager;

    public TargetedChestRestockCommand(ChestRestockPlugin plugin) {
        super(plugin);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player)) {
            messager.bad(Language.IN_GAME_ONLY, sender);
            return;
        }
        Player player = (Player) sender;
        Chest chest = null;
        try {
            chest = chestManager.getTargetedChest(player);
        } catch (IllegalStateException e) {
            messager.sendMessage(sender, e.getMessage());
            return;
        }
        runCommand(player, chest, args);
    }

    public abstract void runCommand(Player player, Chest rChest, List<String> args);
}
