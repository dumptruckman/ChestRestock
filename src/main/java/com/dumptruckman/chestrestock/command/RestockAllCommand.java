package com.dumptruckman.chestrestock.command;

import com.dumptruckman.chestrestock.ChestRestockPlugin;
import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.chestrestock.util.Perms;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RestockAllCommand extends CRCommand {


    public RestockAllCommand(ChestRestockPlugin plugin) {
        super(plugin);
        this.setName(messager.getMessage(Language.CMD_RESTOCKALL_NAME));
        this.setCommandUsage(plugin.getCommandPrefixes().get(0) + " restockall [name] [-w:worldname]");
        this.setArgRange(0, 2);
        this.addPrefixedKey(" restockall");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " restockall phatlootz");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " restockall -w:hungergames");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " restockall cornucopia -w:hungergames");
        this.setPermission(Perms.CMD_RESTOCKALL.getPermission());
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {

        World world = null;
        String name = null;
        for (String arg : args) {
            if (arg.startsWith("-w:")) {
                String worldName = arg.substring(3);
                world = Bukkit.getWorld(worldName);
                if (world == null) {
                    messager.bad(Language.CMD_RESTOCKALL_INVALID_WORLD, sender, worldName);
                    return;
                }
                break;
            } else {
                name = arg;
            }
        }

        messager.normal(Language.CMD_RESTOCKALL_PATIENCE, sender);
        if (world == null) {
            plugin.getChestManager().cacheAllChests();
        } else {
            plugin.getChestManager().cacheChests(world.getName());
        }
        for (CRChest chest : plugin.getChestManager().getAllChests()) {
            if (world != null && !chest.getLocation().getWorldName().equals(world.getName())) {
                continue;
            }
            if (name != null && !chest.get(CRChest.NAME).equalsIgnoreCase(name)) {
                continue;
            }
            if (chest.isValid()) {
                chest.restockAllInventories();
            }
        }
    }
}
