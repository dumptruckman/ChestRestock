package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.api.RestockableChest;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetCommand extends TargetedChestRestockCommand {
    
    private Map<String, ConfigEntry> propsMap = new HashMap<String, ConfigEntry>();
    private String propsString;

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

        propsMap.put("name", RestockableChest.NAME);
        propsMap.put("period", RestockableChest.PERIOD);
        propsMap.put("indestructible", RestockableChest.INDESTRUCTIBLE);
        propsMap.put("period_mode", RestockableChest.PERIOD_MODE);
        propsMap.put("player_limit", RestockableChest.PLAYER_LIMIT);
        propsMap.put("preserve_slots", RestockableChest.PRESERVE_SLOTS);
        propsMap.put("restock_mode", RestockableChest.RESTOCK_MODE);
        propsMap.put("unique", RestockableChest.UNIQUE);
        
        for (String key : propsMap.keySet()) {
            if (!propsString.isEmpty()) {
                propsString += ", ";
            }
            propsString += key;
        }
    }

    @Override
    public void runCommand(Player player, Chest chest, List<String> args) {
        RestockableChest rChest = chestManager.getChest(chest);
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
            if (configEntry == RestockableChest.PERIOD) {
                messager.normal(Language.PERIOD_DESC, player);
            } else if (configEntry == RestockableChest.PLAYER_LIMIT) {
                messager.normal(Language.PLAYER_LIMIT_DESC, player);
            } else if (configEntry == RestockableChest.INDESTRUCTIBLE) {
                messager.normal(Language.INDESTRUCTIBLE_DESC, player);
            } else if (configEntry == RestockableChest.PERIOD_MODE) {
                messager.normal(Language.PERIOD_MODE_DESC, player);
            } else if (configEntry == RestockableChest.RESTOCK_MODE) {
                messager.normal(Language.RESTOCK_MODE_DESC, player);
            } else if (configEntry == RestockableChest.PRESERVE_SLOTS) {
                messager.normal(Language.PRESERVE_SLOTS_DESC, player);
            } else if (configEntry == RestockableChest.UNIQUE) {
                messager.normal(Language.UNIQUE_DESC, player);
            } else if (configEntry == RestockableChest.NAME) {
                messager.normal(Language.NAME_DESC, player);
            }
            if (configEntry.getType().equals(Boolean.class)) {
                messager.normal(Language.CMD_SET_POSSIBLE_VALUES, player, "true/false");
            } else if (configEntry.getType().equals(Integer.class)) {
                messager.normal(Language.CMD_SET_POSSIBLE_VALUES, player, "a number");
            } else if (configEntry.getType().equals(String.class)) {
                messager.normal(Language.CMD_SET_POSSIBLE_VALUES, player, "a word");
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
            } catch (NumberFormatException e) {
                messager.bad(Language.CMD_SET_INVALID_VALUE, player, plugin.getCommandPrefixes().get(0) + " set "
                        + args.get(0));
                return;
            }
            messager.good(Language.CMD_SET_SUCCESS, player, configEntry.getName(), rChest.get(configEntry));
        }
    }
}
