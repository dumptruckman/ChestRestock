package com.dumptruckman.dchest.commands;

import com.dumptruckman.dchest.ChestData;
import com.dumptruckman.dchest.DChest;
import com.nijiko.permissions.PermissionHandler;
import java.util.logging.Level;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author dumptruckman
 */
public class DChestPluginCommand implements CommandExecutor {

    private final DChest plugin;

    public DChestPluginCommand(DChest plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PermissionHandler pH = DChest.permissionHandler;
        //if (!(sender instanceof Player)) {
        if (sender instanceof ConsoleCommandSender) {
            plugin.sendMessage("commands.ingame", sender);
            return true;
        }
        
        if (args.length == 0) {
            return false;
        }
        if(pH==null){
            if(!sender.isOp()){
                plugin.sendMessage("commands.noperm", sender);
                return true;
            }
        }
        else if(!pH.has((Player)sender,"dChest."+args[0].toLowerCase())){
            plugin.sendMessage("commands.noperm", sender);
            return true;
        }/*
        if (!sender.isOp()&&!pH.has((Player)sender,"dChest."+args[0].toLowerCase())) {
            plugin.sendMessage("commands.noperm", sender);
            return true;
        }*/
        if (args[0].equalsIgnoreCase("help")) {
            // Help command
            if (args.length == 1) {
                // General help
                plugin.sendMessage("commands.help.general", sender);
            } else if (args.length == 2) {
                if (args[1].equalsIgnoreCase("check")) {
                    plugin.sendMessage("commands.help.check", sender);
                } else if (args[1].equalsIgnoreCase("set")) {
                    plugin.sendMessage("commands.help.set", sender,
                            plugin.config.getString("defaults.period"));
                } else if (args[1].equalsIgnoreCase("period")) {
                    plugin.sendMessage("commands.help.period", sender);
                } else if (args[1].equalsIgnoreCase("update")) {
                    plugin.sendMessage("commands.help.update", sender);
                } else if (args[1].equalsIgnoreCase("disable")) {
                    plugin.sendMessage("commands.help.disable", sender);
                } else if (args[1].equalsIgnoreCase("periodmode")) {
                    plugin.sendMessage("commands.help.periodmode", sender);
                } else if (args[1].equalsIgnoreCase("restockmode")) {
                    plugin.sendMessage("commands.help.restockmode", sender);
                } else if (args[1].equalsIgnoreCase("reload")) {
                    plugin.sendMessage("commands.help.reload", sender);
                } else if (args[1].equalsIgnoreCase("preserveslots")) {
                    plugin.sendMessage("commands.help.preserveslots", sender);
                } else if (args[1].equalsIgnoreCase("indestructible")) {
                    plugin.sendMessage("commands.help.indestructible", sender);
                } else if (args[1].equalsIgnoreCase("playerlimit")) {
                    plugin.sendMessage("commands.help.playerlimit", sender);
                } else if (args[1].equalsIgnoreCase("restock")) {
                    plugin.sendMessage("commands.help.restock", sender);
                } else if (args[1].equalsIgnoreCase("name")) {
                    plugin.sendMessage("commands.help.name", sender);
                } else if (args[1].equalsIgnoreCase("unique")) {
                    plugin.sendMessage("commands.help.unique", sender);
                } else {
                    plugin.sendMessage("commands.help.unknown", sender);
                }
            } else {
                plugin.sendMessage("commands.help.usage", sender);
            }
            return true;
        } else if (args[0].equalsIgnoreCase("check")) {
            // This will display information about the chest
            ChestData chest = plugin.getTargetedChest(sender);
            if (chest == null) {
                plugin.sendMessage("commands.targetting", sender);
                return true;
            }
            if (chest.isInConfig()) {
                // Chest is already configured
                if (chest.getName() != null) {
                    plugin.sendMessage("chest.name", sender, chest.getName());
                }
                plugin.sendMessage("commands.check", sender, chest.getPeriod(),
                        chest.getRestockMode(), chest.getPeriodMode(),
                        chest.getPreserveSlots(), chest.isIndestructible().toString(),
                        chest.getPlayerLimit().toString(), chest.isUnique().toString());
                return true;
            } else {
                // Chest isn't configured
                plugin.sendMessage("chest.noconfig", sender);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("set")) {
            ChestData chest = plugin.getTargetedChest(sender);
            if (chest == null) {
                plugin.sendMessage("commands.targetting", sender);
                return true;
            }
            if (chest.isInConfig()) {
                plugin.sendMessage("commands.set.alreadyset", sender);
                return true;
            }

            chest.setPeriod(plugin.config.getString("defaults.period"));
            chest.setRestockMode(plugin.config.getString("defaults.restockmode"));
            chest.setPeriodMode(plugin.config.getString("defaults.periodmode"));
            chest.setPreserveSlots(plugin.config.getString("defaults.preserveslots"));
            chest.setIndestructible(plugin.config.getString("defaults.indestructible"));
            chest.setPlayerLimit(plugin.config.getString("defaults.playerlimit"));
            chest.setUnique(plugin.config.getString("defaults.unique"));
            chest.setRestockTimeNow();
            chest.setItems();
            plugin.saveChestConfig();
            plugin.sendMessage("commands.set.success", sender,
                    plugin.config.getString("defaults.period"));
            return true;
        } else if (args[0].equalsIgnoreCase("period")) {
            if (args.length < 2) {
                plugin.sendMessage("commands.period.usage", sender);
                return true;
            } else {
                ChestData chest = plugin.getTargetedChest(sender);
                if (chest == null) {
                    plugin.sendMessage("commands.targetting", sender);
                    return true;
                }
                if (!chest.isInConfig()) {
                    plugin.sendMessage("chest.noconfig", sender);
                    return true;
                }
                try {
                    if (Integer.parseInt(args[1]) <= 0)
                        throw new NumberFormatException();
                    chest.setPeriod(args[1]);
                    chest.setRestockTimeNow();
                    plugin.saveChestConfig();
                    plugin.sendMessage("commands.period.success", sender, args[1]);
                    return true;
                } catch (NumberFormatException ignore) {
                    plugin.sendMessage("commands.period.inalid", sender);
                    return true;
                }
            }
        } else if (args[0].equalsIgnoreCase("update")) {
            ChestData chest = plugin.getTargetedChest(sender);
            if (chest == null) {
                plugin.sendMessage("commands.targetting", sender);
                return true;
            }
            if (!chest.isInConfig()) {
                plugin.sendMessage("chest.noconfig", sender);
                return true;
            }
            chest.setRestockTimeNow();
            chest.setItems();
            plugin.saveChestConfig();
            plugin.sendMessage("commands.update.success", sender);
            return true;
        } else if (args[0].equalsIgnoreCase("disable")) {
            ChestData chest = plugin.getTargetedChest(sender);
            if (chest == null) {
                plugin.sendMessage("commands.targetting", sender);
                return true;
            }
            if (!chest.isInConfig()) {
                plugin.sendMessage("chest.noconfig", sender);
                return true;
            }
            chest.disable();
            plugin.saveChestConfig();
            plugin.sendMessage("commands.disable.success", sender);
            return true;
        } else if (args[0].equalsIgnoreCase("periodmode")) {
            if (args.length < 2) {
                plugin.sendMessage("commands.periodmode.usage", sender);
                plugin.sendMessage("commands.nomode", sender);
                return true;
            } else {
                ChestData chest = plugin.getTargetedChest(sender);
                if (chest == null) {
                    plugin.sendMessage("commands.targetting", sender);
                    return true;
                }
                if (!chest.isInConfig()) {
                    plugin.sendMessage("chest.noconfig", sender);
                    return true;
                }
                if (args[1].equalsIgnoreCase("player")) {
                    chest.setPeriodMode("player");
                    plugin.saveChestConfig();
                    plugin.sendMessage("commands.periodmode.player", sender,
                            chest.getPeriod());
                } else if (args[1].equalsIgnoreCase("settime")) {
                    chest.setPeriodMode("settime");
                    plugin.saveChestConfig();
                    plugin.sendMessage("commands.periodmode.settime", sender,
                            chest.getPeriod());
                } else {
                    plugin.sendMessage("commands.periodmode.invalid", sender);
                }
                return true;
            }
        } else if (args[0].equalsIgnoreCase("restockmode")) {
            if (args.length < 2) {
                plugin.sendMessage("commands.restockmode.usage", sender);
                plugin.sendMessage("commands.nomode", sender);
                return true;
            } else {
                ChestData chest = plugin.getTargetedChest(sender);
                if (chest == null) {
                    plugin.sendMessage("commands.targetting", sender);
                    return true;
                }
                if (!chest.isInConfig()) {
                    plugin.sendMessage("chest.noconfig", sender);
                    return true;
                }
                if (args[1].equalsIgnoreCase("add")) {
                    chest.setRestockMode("add");
                    plugin.saveChestConfig();
                    plugin.sendMessage("commands.restockmode.add", sender);
                } else if (args[1].equalsIgnoreCase("replace")) {
                    chest.setRestockMode("replace");
                    plugin.saveChestConfig();
                    plugin.sendMessage("commands.restockmode.replace", sender);
                } else {
                    plugin.sendMessage("commands.restockmode.invalid", sender);
                }
                return true;
            }
        } else if (args[0].equalsIgnoreCase("reload")) {
            plugin.reload(true);
            plugin.sendMessage("commands.restockmode.invalid", sender);
            return true;
        } else if (args[0].equalsIgnoreCase("preserveslots")) {
            if (args.length < 2) {
                plugin.sendMessage("commands.preserveslots.usage", sender);
                plugin.sendMessage("commands.nomode", sender);
                return true;
            } else {
                ChestData chest = plugin.getTargetedChest(sender);
                if (chest == null) {
                    plugin.sendMessage("commands.targetting", sender);
                    return true;
                }
                if (!chest.isInConfig()) {
                    plugin.sendMessage("chest.noconfig", sender);
                    return true;
                }
                if (args[1].equalsIgnoreCase("true")) {
                    chest.setPreserveSlots("true");
                    plugin.saveChestConfig();
                    plugin.sendMessage("commands.preserveslots.true", sender);
                } else if (args[1].equalsIgnoreCase("false")) {
                    chest.setPreserveSlots("false");
                    plugin.saveChestConfig();
                    plugin.sendMessage("commands.preserveslots.false", sender);
                } else {
                    plugin.sendMessage("commands.preserveslots.invalid", sender);
                }
                return true;
            }
        } else if (args[0].equalsIgnoreCase("indestructible")) {
            if (args.length < 2) {
                plugin.sendMessage("commands.indestructible.usage", sender);
                plugin.sendMessage("commands.nomode", sender);
                return true;
            } else {
                ChestData chest = plugin.getTargetedChest(sender);
                if (chest == null) {
                    plugin.sendMessage("commands.targetting", sender);
                    return true;
                }
                if (!chest.isInConfig()) {
                    plugin.sendMessage("chest.noconfig", sender);
                    return true;
                }
                if (args[1].equalsIgnoreCase("true")) {
                    chest.setIndestructible("true");
                    plugin.saveChestConfig();
                    plugin.sendMessage("commands.indestructible.true", sender);
                } else if (args[1].equalsIgnoreCase("false")) {
                    chest.setIndestructible("false");
                    plugin.saveChestConfig();
                    plugin.sendMessage("commands.indestructible.false", sender);
                } else {
                    plugin.sendMessage("commands.indestructible.invalid", sender);
                }
                return true;
            }
        } else if (args[0].equalsIgnoreCase("playerlimit")) {
            if (args.length < 2) {
                plugin.sendMessage("commands.playerlimit.usage", sender);
                return true;
            } else {
                ChestData chest = plugin.getTargetedChest(sender);
                if (chest == null) {
                    plugin.sendMessage("commands.targetting", sender);
                    return true;
                }
                if (!chest.isInConfig()) {
                    plugin.sendMessage("chest.noconfig", sender);
                    return true;
                }
                try {
                    int limit = Integer.parseInt(args[1]);
                    if (limit < -1) {
                        throw new NumberFormatException();
                    }
                    if (limit == -1) {
                        plugin.sendMessage("commands.playerlimit.unlimited", sender);
                    } else {
                        plugin.sendMessage("commands.playerlimit.limited", sender, args[1]);
                    }
                    chest.setPlayerLimit(args[1]);
                    plugin.saveChestConfig();
                } catch (NumberFormatException nfe) {
                    plugin.sendMessage("commands.playerlimit.invalid", sender);
                }
                return true;
            }
        } else if (args[0].equalsIgnoreCase("restock")) {
            ChestData chest = plugin.getTargetedChest(sender);
            if (chest == null) {
                plugin.sendMessage("commands.targetting", sender);
                return true;
            }
            if (!chest.isInConfig()) {
                plugin.sendMessage("chest.noconfig", sender);
                return true;
            }
            chest.restock(chest.getItems());
            plugin.sendMessage("commands.restock", sender);
            return true;
        } else if (args[0].equalsIgnoreCase("name")) {
            if (args.length < 2) {
                plugin.sendMessage("commands.name.usage", sender);
                return true;
            } else {
                ChestData chest = plugin.getTargetedChest(sender);
                if (chest == null) {
                    plugin.sendMessage("commands.targetting", sender);
                    return true;
                }
                if (!chest.isInConfig()) {
                    plugin.sendMessage("chest.noconfig", sender);
                    return true;
                }
                chest.setName(args[1]);
                plugin.sendMessage("commands.name.success", sender, args[1]);
                plugin.saveChestConfig();
                return true;
            }
        } else if (args[0].equalsIgnoreCase("unique")) {
            if (args.length < 2) {
                plugin.sendMessage("commands.unique.usage", sender);
                plugin.sendMessage("commands.nomode", sender);
                return true;
            } else {
                ChestData chest = plugin.getTargetedChest(sender);
                if (chest == null) {
                    plugin.sendMessage("commands.targetting", sender);
                    return true;
                }
                if (!chest.isInConfig()) {
                    plugin.sendMessage("chest.noconfig", sender);
                    return true;
                }
                if (args[1].equalsIgnoreCase("true")) {
                    chest.setUnique("true");
                    plugin.saveChestConfig();
                    plugin.sendMessage("commands.unique.true", sender);
                } else if (args[1].equalsIgnoreCase("false")) {
                    chest.setUnique("false");
                    plugin.saveChestConfig();
                    plugin.sendMessage("commands.unique.false", sender);
                } else {
                    plugin.sendMessage("commands.unique.invalid", sender);
                }
                return true;
            }
        }
        
        return false;
    }

}
