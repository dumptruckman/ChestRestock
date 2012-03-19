package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.api.RestockableChest;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.List;

public class SetCommand extends TargetedChestRestockCommand {

    public SetCommand(ChestRestockPlugin plugin) {
        super(plugin);
        this.setName(messager.getMessage(Language.CMD_SET_NAME));
        this.setCommandUsage(plugin.getCommandPrefixes().get(0) + " set");
        this.setArgRange(0, 0);
        for (String prefix : plugin.getCommandPrefixes()) {
            this.addKey(prefix + " set");
            this.addKey(prefix + " create");
        }
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " set");
        this.setPermission(Perms.CMD_SET.getPermission());
    }

    @Override
    public void runCommand(Player player, Chest chest, List<String> strings) {
        RestockableChest rChest = chestManager.getChest(chest);
        if (rChest != null) {
            messager.normal(Language.CMD_SET_ALREADY_SET, player);
            return;
        }
        rChest = chestManager.newChest(chest);
        if (rChest == null) {
            messager.bad(Language.CMD_SET_ERROR, player);
            return;
        }
        rChest.update();
        messager.good(Language.CMD_SET_SUCCESS, player, rChest.get(RestockableChest.PERIOD));
    }
}
