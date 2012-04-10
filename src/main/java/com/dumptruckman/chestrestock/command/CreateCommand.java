package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.api.CRConfig;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public class CreateCommand extends TargetedChestRestockCommand {

    public CreateCommand(ChestRestockPlugin plugin) {
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
        CRChest rChest = chestManager.getChest(block, holder);
        if (rChest != null) {
            messager.normal(Language.CMD_CREATE_ALREADY_MADE, player);
            return;
        }
        rChest = chestManager.newChest(block, holder);
        if (rChest == null) {
            messager.bad(Language.CMD_CREATE_ERROR, player);
            return;
        }
        rChest.set(CRChest.PERIOD, plugin.config().get(CRConfig.PERIOD));
        rChest.set(CRChest.PERIOD_MODE, plugin.config().get(CRConfig.PERIOD_MODE));
        rChest.set(CRChest.RESTOCK_MODE, plugin.config().get(CRConfig.RESTOCK_MODE));
        rChest.set(CRChest.INDESTRUCTIBLE, plugin.config().get(CRConfig.INDESTRUCTIBLE));
        rChest.set(CRChest.PLAYER_LIMIT, plugin.config().get(CRConfig.PLAYER_LIMIT));
        rChest.set(CRChest.UNIQUE, plugin.config().get(CRConfig.UNIQUE));
        rChest.set(CRChest.PRESERVE_SLOTS, plugin.config().get(CRConfig.PRESERVE_SLOTS));
        rChest.set(CRChest.NAME, plugin.config().get(CRConfig.NAME));
        rChest.set(CRChest.REDSTONE, plugin.config().get(CRConfig.REDSTONE));
        rChest.set(CRChest.GLOBAL_MESSAGE, plugin.config().get(CRConfig.GLOBAL_MESSAGE));

        rChest.set(CRChest.LAST_RESTOCK, System.currentTimeMillis());
        rChest.update(null);
        messager.good(Language.CMD_CREATE_SUCCESS, player, rChest.get(CRChest.PERIOD));
    }
}
