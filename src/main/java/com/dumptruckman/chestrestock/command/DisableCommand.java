package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public class DisableCommand extends TargetedChestRestockCommand {

    public DisableCommand(ChestRestockPlugin plugin) {
        super(plugin);
        this.setName(messager.getMessage(Language.CMD_DISABLE_NAME));
        this.setCommandUsage(plugin.getCommandPrefixes().get(0) + " disable");
        this.setArgRange(0, 0);
        for (String prefix : plugin.getCommandPrefixes()) {
            this.addKey(prefix + " disable");
        }
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " disable");
        this.setPermission(Perms.CMD_DISABLE.getPermission());
    }

    @Override
    public void runCommand(Player player, Block block, List<String> strings) {
        CRChest rChest = chestManager.getChest(block, (InventoryHolder) block.getState());
        if (rChest == null) {
            messager.normal(Language.CMD_NOT_RCHEST, player);
            return;
        }
        chestManager.removeChest(rChest.getLocation());
        messager.good(Language.CMD_DISABLE_SUCCESS, player);
    }
}
