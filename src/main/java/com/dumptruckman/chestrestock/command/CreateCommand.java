package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.api.RestockableChest;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

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
    public void runCommand(Player player, Chest chest, List<String> strings) {
        RestockableChest rChest = chestManager.getChest(chest);
        if (rChest != null) {
            messager.normal(Language.CMD_CREATE_ALREADY_MADE, player);
            return;
        }
        rChest = chestManager.newChest(chest);
        if (rChest == null) {
            messager.bad(Language.CMD_CREATE_ERROR, player);
            return;
        }
        rChest.update();
        messager.good(Language.CMD_CREATE_SUCCESS, player, rChest.get(RestockableChest.PERIOD));
    }
}
