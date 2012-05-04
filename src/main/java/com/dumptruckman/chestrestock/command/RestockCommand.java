package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public class RestockCommand extends TargetedChestRestockCommand {


    public RestockCommand(ChestRestock plugin) {
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
    public void runCommand(Player player, Block block, List<String> strings) {
        CRChest rChest = chestManager.getChest(block);
        if (rChest == null) {
            messager.normal(Language.CMD_NOT_RCHEST, player);
            return;
        }
        rChest.restockAllInventories();
        messager.good(Language.CMD_RESTOCK_SUCCESS, player);
    }
}
