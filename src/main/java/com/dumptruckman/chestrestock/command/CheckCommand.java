package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.api.RestockableChest;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.List;

import static com.dumptruckman.chestrestock.api.RestockableChest.*;

public class CheckCommand extends TargetedChestRestockCommand {

    public CheckCommand(ChestRestockPlugin plugin) {
        super(plugin);
        this.setName(messager.getMessage(Language.CMD_CHECK_NAME));
        this.setCommandUsage(plugin.getCommandPrefixes().get(0) + " check");
        this.setArgRange(0, 0);
        for (String prefix : plugin.getCommandPrefixes()) {
            this.addKey(prefix + " check");
        }
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " check");
        this.setPermission(Perms.CMD_CHECK.getPermission());
    }

    @Override
    public void runCommand(Player player, Chest chest, List<String> strings) {
        RestockableChest rChest = chestManager.getChest(chest);
        if (rChest == null) {
            messager.normal(Language.CMD_NOT_RCHEST, player);
            return;
        }
        messager.normal(Language.CMD_CHECK_SUCCESS, player,
                rChest.get(PERIOD), rChest.get(RESTOCK_MODE), rChest.get(PERIOD_MODE),
                rChest.get(PRESERVE_SLOTS), rChest.get(INDESTRUCTIBLE), rChest.get(PLAYER_LIMIT),
                rChest.get(UNIQUE));
    }
}
