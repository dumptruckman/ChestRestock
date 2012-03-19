package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.api.RestockableChest;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.List;

public class UpdateCommand extends TargetedChestRestockCommand {

    public UpdateCommand(ChestRestockPlugin plugin) {
        super(plugin);
        this.setName(messager.getMessage(Language.CMD_UPDATE_NAME));
        this.setCommandUsage(plugin.getCommandPrefixes().get(0) + " update");
        this.setArgRange(0, 0);
        for (String prefix : plugin.getCommandPrefixes()) {
            this.addKey(prefix + " update");
        }
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " update");
        this.setPermission(Perms.CMD_UPDATE.getPermission());
    }

    @Override
    public void runCommand(Player player, Chest chest, List<String> strings) {
        RestockableChest rChest = chestManager.getChest(chest);
        if (rChest == null) {
            messager.normal(Language.CMD_NOT_RCHEST, player);
            return;
        }
        rChest.update();
        messager.good(Language.CMD_UPDATE_SUCCESS, player);
    }
}
