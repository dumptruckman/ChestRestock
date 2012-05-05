package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.api.CRChestOptions;
import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.util.Null;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CheckCommand extends TargetedChestRestockCommand {

    private static final List<ConfigEntry> PROPS_LIST = new LinkedList<ConfigEntry>();
    private static final List<String> CHECK_MESSAGES = new LinkedList<String>();

    static {
        //int count = 2;
        for (Field field : CRChestOptions.class.getFields()) {
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

    public CheckCommand(ChestRestock plugin) {
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
    public void runCommand(Player player, Block block, List<String> strings) {
        CRChest rChest = chestManager.getChest(block);
        if (rChest == null) {
            messager.normal(Language.CMD_NOT_RCHEST, player);
            return;
        }
        List<String> messages = new ArrayList<String>(CHECK_MESSAGES);
        for (int i = 0; i < messages.size(); i++) {
            messages.set(i, String.format(messages.get(i), rChest.get(PROPS_LIST.get(i))));
        }
        messages.add(0, messager.getMessage(Language.CMD_CHECK_INFO, rChest.getLocation()));
        messager.sendMessages(player, messages);
    }
}
