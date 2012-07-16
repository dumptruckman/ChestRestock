package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public class UpdateCommand extends TargetedChestRestockCommand {

    public UpdateCommand(ChestRestock plugin) {
        super(plugin);
        this.setName(messager.getMessage(Language.CMD_UPDATE_NAME));
        this.setCommandUsage("/" + plugin.getCommandPrefixes().get(0) + " update");
        this.setArgRange(0, 0);
        for (String prefix : plugin.getCommandPrefixes()) {
            this.addKey(prefix + " update");
        }
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " update");
        this.setPermission(Perms.CMD_UPDATE.getPermission());
    }

    @Override
    public void runCommand(Player player, Block block, List<String> strings) {
        CRChest rChest = chestManager.getChest(block);
        if (rChest == null) {
            messager.normal(Language.CMD_NOT_RCHEST, player);
            return;
        }
        if (!rChest.get(CRChest.ENABLED)) {
            messager.normal(Language.CMD_CHEST_DISABLED, player);
            return;
        }
        rChest.update(player);
        messager.good(Language.CMD_UPDATE_SUCCESS, player);
    }
}
