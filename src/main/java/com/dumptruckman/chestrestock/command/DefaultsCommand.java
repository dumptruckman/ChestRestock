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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DefaultsCommand extends CRCommand {

    private static final List<ConfigEntry> PROPS_LIST = new LinkedList<ConfigEntry>();
    private static final List<String> CHECK_MESSAGES = new LinkedList<String>();

    static {
        //int count = 2;
        for (Field field : CRDefaults.class.getFields()) {
            if (!ConfigEntry.class.isAssignableFrom(field.getType())) {
                continue;
            }
            try {
                ConfigEntry entry = (ConfigEntry) field.get(null);
                if (entry.getType().equals(Null.class)) {
                    continue;
                }
                PROPS_LIST.add(entry);
                CHECK_MESSAGES.add("\u00a7b" + field.getName().toLowerCase() + ":\u00a7f %s");
                //count++;
            } catch (IllegalAccessException ignore) { }
        }
    }

    public DefaultsCommand(ChestRestock plugin) {
        super(plugin);
        this.setName(messager.getMessage(Language.CMD_DEFAULTS_NAME));
        this.setCommandUsage(plugin.getCommandPrefixes().get(0) + " defaults [world]");
        this.setArgRange(0, 1);
        this.addPrefixedKey(" defaults");
        this.addPrefixedKey(" defs");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " defaults");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " defs hungergames");
        this.setPermission(Perms.CMD_DEFAULTS.getPermission());
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        String world = null;
        if (args.size() > 0) {
            world = args.get(0);
            if (Bukkit.getWorld(world) == null) {
                messager.bad(Language.CMD_INVALID_WORLD, sender, world);
                return;
            }
        }
        CRDefaults defaults = plugin.getDefaults(world);
        List<String> messages = new ArrayList<String>(CHECK_MESSAGES);
        List<String> messagesToRemove = new LinkedList<String>();
        int numProps = 0;
        for (int i = 0; i < messages.size(); i++) {
            Object obj = defaults.get(PROPS_LIST.get(i));
            if (obj == null) {
                if (world == null) {
                    obj = "";
                } else {
                    messagesToRemove.add(messages.get(i));
                    continue;
                }
            }
            numProps++;
            messages.set(i, String.format(messages.get(i), obj));
        }
        messages.removeAll(messagesToRemove);
        if (world == null) {
            messages.add(0, messager.getMessage(Language.CMD_DEFAULTS_GLOBAL));
        } else {
            messages.add(0, messager.getMessage(Language.CMD_DEFAULTS_WORLD, world));
        }
        if (numProps == 0) {
            messager.info(Language.CMD_DEFAULTS_NO_DEFAULTS, sender, world);
        } else {
            messager.sendMessages(sender, messages);
        }
    }
}
