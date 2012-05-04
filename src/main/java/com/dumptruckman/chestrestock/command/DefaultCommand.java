package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.api.CRDefaults;
import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.util.Null;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCommand extends CRCommand {

    private static final Map<String, ConfigEntry> PROPS_MAP = new HashMap<String, ConfigEntry>();
    private static String propsString = "";

    static {
        for (Field field : CRDefaults.class.getFields()) {
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

    public DefaultCommand(ChestRestock plugin) {
        super(plugin);
        this.setName(messager.getMessage(Language.CMD_DEFAULT_NAME));
        this.setCommandUsage("/" + plugin.getCommandPrefixes().get(0) + " default [-w:<world>] [property [value]]");
        this.addPrefixedKey(" default");
        this.addPrefixedKey(" def");
        this.setArgRange(0, 500);
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " default");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " default -w:hungergames");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " default period 300");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " default -w:hungergames restockmode fixed");
        this.setPermission(Perms.CMD_SET.getPermission());
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        String world = null;
        int i = 0;
        if (args.size() > 0) {
            if (args.get(0).startsWith("-w:") && args.get(0).length() > 3) {
                i = 1;
                world = args.get(0).substring(3);
                if (Bukkit.getWorld(world) == null) {
                    messager.bad(Language.CMD_INVALID_WORLD, sender, world);
                    return;
                }
            }
        }
        if (world != null) {
            plugin.createDefaultIfNoneExists(world);
        }
        if (args.size() - i == 0) {
            messager.normal(Language.CMD_SET_LIST_PROPS, sender, propsString);
        } else if (args.size() - i == 1) {
            ConfigEntry configEntry = PROPS_MAP.get(args.get(i).toLowerCase());
            if (configEntry == null) {
                messager.bad(Language.CMD_SET_INVALID_PROP, sender, args.get(i));
                return;
            }
            if (configEntry.getDescription() != null) {
                messager.normal(configEntry.getDescription(), sender);
            }
            if (configEntry.getType().equals(Boolean.class)) {
                messager.normal(Language.CMD_SET_POSSIBLE_VALUES, sender, configEntry.getName(), "true/false");
            } else if (configEntry.getType().equals(Integer.class)) {
                messager.normal(Language.CMD_SET_POSSIBLE_VALUES, sender, configEntry.getName(), "a number");
            } else if (configEntry.getType().equals(String.class)) {
                messager.normal(Language.CMD_SET_POSSIBLE_VALUES, sender, configEntry.getName(), "a word");
            }
        } else if (args.size() - i > 1) {
            ConfigEntry configEntry = PROPS_MAP.get(args.get(i).toLowerCase());
            if (configEntry == null) {
                messager.bad(Language.CMD_SET_INVALID_PROP, sender, args.get(i));
                return;
            }
            String value;
            if (args.size() - i == 2) {
                value = args.get(i + 1).toLowerCase();
            } else {
                StringBuilder builder = new StringBuilder();
                for (int j = i + 1; j < args.size(); j++) {
                    if (!builder.toString().isEmpty()) {
                        builder.append(" ");
                    }
                    builder.append(args.get(i));
                }
                value = builder.toString();
            }
            if (configEntry == CRDefaults.GLOBAL_MESSAGE && value.equalsIgnoreCase("clear")) {
                value = "";
            }
            if (!configEntry.isValid(value)) {
                messager.bad(Language.CMD_SET_INVALID_VALUE, sender, plugin.getCommandPrefixes().get(0) + " default "
                        + args.get(i));
                return;
            }
            CRDefaults defaults = plugin.getDefaults(world);
            try {
                defaults.set(configEntry, configEntry.deserialize(value));
                defaults.save();
            } catch (NumberFormatException e) {
                messager.bad(Language.CMD_SET_INVALID_VALUE, sender, plugin.getCommandPrefixes().get(0) + " default "
                        + args.get(i));
                return;
            }
            if (world == null) {
                messager.good(Language.CMD_DEFAULT_SUCCESS_GLOBAL, sender, args.get(i).toLowerCase(), defaults.get(configEntry));
            } else {
                messager.good(Language.CMD_DEFAULT_SUCCESS_WORLD, sender, args.get(i).toLowerCase(), world, defaults.get(configEntry));
            }
        }
    }
}
