package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.api.RestockableChest;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.List;

public class RestockCommand extends TargetedChestRestockCommand {


    public RestockCommand(ChestRestockPlugin plugin) {
        super(plugin);
        this.setName(messager.getMessage(Language.CMD_RESTOCK_NAME));
        this.setCommandUsage(plugin.getCommandPrefixes().get(0) + " restock");
        this.setArgRange(0, 0);
        for (String prefix : plugin.getCommandPrefixes()) {
            this.addKey(prefix + " restock");
        }
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " restock");
        this.setPermission(Perms.CMD_RESTOCK.getPermission());
    }

    @Override
    public void runCommand(Player player, Chest chest, List<String> strings) {
        RestockableChest rChest = chestManager.getChest(chest);
        if (rChest == null) {
            messager.normal(Language.CMD_NOT_RCHEST, player);
            return;
        }
        // TODO once restocking methods are complete for chests.
        messager.good(Language.CMD_RESTOCK_SUCCESS, player);
    }
}
