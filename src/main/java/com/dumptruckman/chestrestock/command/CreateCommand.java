package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public class CreateCommand extends TargetedChestRestockCommand {

    public CreateCommand(ChestRestock plugin) {
        super(plugin);
        this.setName(messager.getMessage(Language.CMD_CREATE_NAME));
        this.setCommandUsage(plugin.getCommandPrefixes().get(0) + " create");
        this.setArgRange(0, 0);
        for (String prefix : plugin.getCommandPrefixes()) {
            this.addKey(prefix + " create");
        }
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " create");
        this.setPermission(Perms.CMD_CREATE.getPermission());
    }

    @Override
    public void runCommand(Player player, Block block, List<String> strings) {
        InventoryHolder holder = (InventoryHolder) block.getState();
        CRChest rChest = chestManager.getChest(block);
        if (rChest != null) {
            messager.normal(Language.CMD_CREATE_ALREADY_MADE, player);
            return;
        }
        rChest = chestManager.createChest(block, holder);
        if (rChest == null) {
            messager.bad(Language.CMD_CREATE_ERROR, player);
        } else {
            messager.good(Language.CMD_CREATE_SUCCESS, player, rChest.get(CRChest.PERIOD));
        }
    }
}
