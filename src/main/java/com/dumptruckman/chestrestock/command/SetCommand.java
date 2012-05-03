package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.api.CRChestOptions;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.util.Null;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetCommand extends TargetedChestRestockCommand {

    private static final Map<String, ConfigEntry> PROPS_MAP = new HashMap<String, ConfigEntry>();
    private static String propsString = "";

    static {
        for (Field field : CRChestOptions.class.getFields()) {
            if (!ConfigEntry.class.isAssignableFrom(field.getType())) {
                continue;
            }
            try {
                ConfigEntry entry = (ConfigEntry) field.get(null);
                if (entry.getType().equals(Null.class)) {
                    continue;
                }
                PROPS_MAP.put(field.getName().toLowerCase(), entry);
            } catch (IllegalAccessException ignore) { }
        }
        for (String key : PROPS_MAP.keySet()) {
            if (!propsString.isEmpty()) {
                propsString += ", ";
            }
            propsString += key;
        }
    }

    public SetCommand(ChestRestockPlugin plugin) {
        super(plugin);
        this.setName(messager.getMessage(Language.CMD_SET_NAME));
        this.setCommandUsage("/" + plugin.getCommandPrefixes().get(0) + " set [property [value]]");
        this.addPrefixedKey(" set");
        this.setArgRange(0, 500);
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " set");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " set unique");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " set period 300");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " set restockmode fixed");
        this.setPermission(Perms.CMD_SET.getPermission());
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
            ConfigEntry configEntry = PROPS_MAP.get(args.get(0).toLowerCase());
            if (configEntry == null) {
                messager.bad(Language.CMD_SET_INVALID_PROP, player, args.get(0));
                return;
            }
            if (configEntry.getDescription() != null) {
                messager.normal(configEntry.getDescription(), player);
            }
            if (configEntry.getType().equals(Boolean.class)) {
                messager.normal(Language.CMD_SET_POSSIBLE_VALUES, player, configEntry.getName(), "true/false");
            } else if (configEntry.getType().equals(Integer.class)) {
                messager.normal(Language.CMD_SET_POSSIBLE_VALUES, player, configEntry.getName(), "a number");
            } else if (configEntry.getType().equals(String.class)) {
                messager.normal(Language.CMD_SET_POSSIBLE_VALUES, player, configEntry.getName(), "a word");
            }
        } else if (args.size() > 1) {
            ConfigEntry configEntry = PROPS_MAP.get(args.get(0).toLowerCase());
            if (configEntry == null) {
                messager.bad(Language.CMD_SET_INVALID_PROP, player, args.get(0));
                return;
            }
            if (rChest == null) {
                messager.normal(Language.CMD_NOT_RCHEST, player);
                return;
            }
            String value;
            if (args.size() == 2) {
                value = args.get(1).toLowerCase();
            } else {
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < args.size(); i++) {
                    if (!builder.toString().isEmpty()) {
                        builder.append(" ");
                    }
                    builder.append(args.get(i));
                }
                value = builder.toString();
            }
            if (configEntry == CRChest.GLOBAL_MESSAGE && value.equalsIgnoreCase("clear")) {
                value = "";
            }
            if (!configEntry.isValid(value)) {
                messager.bad(Language.CMD_SET_INVALID_VALUE, player, plugin.getCommandPrefixes().get(0) + " set "
                        + args.get(0));
                return;
            }
            try {
                rChest.set(configEntry, configEntry.deserialize(value));
                rChest.save();
                chestManager.pollingCheckIn(rChest);
            } catch (NumberFormatException e) {
                messager.bad(Language.CMD_SET_INVALID_VALUE, player, plugin.getCommandPrefixes().get(0) + " set "
                        + args.get(0));
                return;
            }
            messager.good(Language.CMD_SET_SUCCESS, player, configEntry.getName(), rChest.get(configEntry));
        }
    }
}
