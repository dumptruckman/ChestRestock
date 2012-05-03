package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.api.CRChestOptions;
import com.dumptruckman.chestrestock.api.CRDefaults;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.lang.reflect.Field;
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
        CRDefaults defaults = plugin.getDefaults(player.getWorld().getName());
        for (Field field : CRChestOptions.class.getFields()) {
            if (!ConfigEntry.class.isAssignableFrom(field.getType())) {
                continue;
            }
            try {
                ConfigEntry entry = (ConfigEntry) field.get(null);
                rChest.set(entry, defaults.get(entry));
                //count++;
            } catch (IllegalAccessException ignore) { }
        }

        rChest.set(CRChest.LAST_RESTOCK, System.currentTimeMillis());
        rChest.update(null);
        messager.good(Language.CMD_CREATE_SUCCESS, player, rChest.get(CRChest.PERIOD));
        chestManager.pollingCheckIn(rChest);
    }
}
