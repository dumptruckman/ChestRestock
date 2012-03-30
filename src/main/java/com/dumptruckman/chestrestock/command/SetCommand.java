package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetCommand extends TargetedChestRestockCommand {
    
    private Map<String, ConfigEntry> propsMap = new HashMap<String, ConfigEntry>();
    private String propsString = "";

    public SetCommand(ChestRestockPlugin plugin) {
        super(plugin);
        this.setName(messager.getMessage(Language.CMD_SET_NAME));
        this.setCommandUsage("/" + plugin.getCommandPrefixes().get(0) + " set [property [value]]");
        this.setArgRange(0, 2);
        for (String prefix : plugin.getCommandPrefixes()) {
            this.addKey(prefix + " set");
        }
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " set");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " set unique");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " set period 300");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " set restockmode fixed");
        this.setPermission(Perms.CMD_SET.getPermission());

        propsMap.put("name", CRChest.NAME);
        propsMap.put("period", CRChest.PERIOD);
        propsMap.put("indestructible", CRChest.INDESTRUCTIBLE);
        propsMap.put("period_mode", CRChest.PERIOD_MODE);
        propsMap.put("player_limit", CRChest.PLAYER_LIMIT);
        propsMap.put("preserve_slots", CRChest.PRESERVE_SLOTS);
        propsMap.put("restock_mode", CRChest.RESTOCK_MODE);
        propsMap.put("unique", CRChest.UNIQUE);
        propsMap.put("redstone", CRChest.REDSTONE);
        
        for (String key : propsMap.keySet()) {
            if (!propsString.isEmpty()) {
                propsString += ", ";
            }
            propsString += key;
        }
    }

    @Override
    public void runCommand(Player player, Block block, List<String> args) {
        CRChest rChest = chestManager.getChest(block, (InventoryHolder) block.getState());
        if (args.size() == 0) {
            if (rChest == null) {
                messager.normal(Language.CMD_SET_NEW_CMD, player);
            } else {
                messager.normal(Language.CMD_SET_LIST_PROPS, player, propsString);
            }
        } else if (args.size() == 1) {
            ConfigEntry configEntry = propsMap.get(args.get(0).toLowerCase());
            if (configEntry == null) {
                messager.bad(Language.CMD_SET_INVALID_PROP, player, args.get(0));
                return;
            }
            if (configEntry == CRChest.PERIOD) {
                messager.normal(Language.PERIOD_DESC, player);
            } else if (configEntry == CRChest.PLAYER_LIMIT) {
                messager.normal(Language.PLAYER_LIMIT_DESC, player);
            } else if (configEntry == CRChest.INDESTRUCTIBLE) {
                messager.normal(Language.INDESTRUCTIBLE_DESC, player);
            } else if (configEntry == CRChest.PERIOD_MODE) {
                messager.normal(Language.PERIOD_MODE_DESC, player);
            } else if (configEntry == CRChest.RESTOCK_MODE) {
                messager.normal(Language.RESTOCK_MODE_DESC, player);
            } else if (configEntry == CRChest.PRESERVE_SLOTS) {
                messager.normal(Language.PRESERVE_SLOTS_DESC, player);
            } else if (configEntry == CRChest.UNIQUE) {
                messager.normal(Language.UNIQUE_DESC, player);
            } else if (configEntry == CRChest.NAME) {
                messager.normal(Language.NAME_DESC, player);
            }
            if (configEntry.getType().equals(Boolean.class)) {
                messager.normal(Language.CMD_SET_POSSIBLE_VALUES, player, configEntry.getName(), "true/false");
            } else if (configEntry.getType().equals(Integer.class)) {
                messager.normal(Language.CMD_SET_POSSIBLE_VALUES, player, configEntry.getName(), "a number");
            } else if (configEntry.getType().equals(String.class)) {
                messager.normal(Language.CMD_SET_POSSIBLE_VALUES, player, configEntry.getName(), "a word");
            }
        } else if (args.size() == 2) {
            ConfigEntry configEntry = propsMap.get(args.get(0).toLowerCase());
            if (configEntry == null) {
                messager.bad(Language.CMD_SET_INVALID_PROP, player, args.get(0));
                return;
            }
            if (rChest == null) {
                messager.normal(Language.CMD_NOT_RCHEST, player);
                return;
            }
            String value = args.get(1).toLowerCase();
            if (!configEntry.isValid(value)) {
                messager.bad(Language.CMD_SET_INVALID_VALUE, player, plugin.getCommandPrefixes().get(0) + " set "
                        + args.get(0));
                return;
            }
            try {
                rChest.set(configEntry, configEntry.deserialize(value));
                rChest.save();
            } catch (NumberFormatException e) {
                messager.bad(Language.CMD_SET_INVALID_VALUE, player, plugin.getCommandPrefixes().get(0) + " set "
                        + args.get(0));
                return;
            }
            messager.good(Language.CMD_SET_SUCCESS, player, configEntry.getName(), rChest.get(configEntry));
        }
    }
}
