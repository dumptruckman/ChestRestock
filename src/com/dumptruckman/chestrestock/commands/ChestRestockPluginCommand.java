package com.dumptruckman.chestrestock.commands;

import com.dumptruckman.chestrestock.ChestData;
import com.dumptruckman.chestrestock.ChestRestock;
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
public class ChestRestockPluginCommand implements CommandExecutor {

    private final ChestRestock plugin;

    public ChestRestockPluginCommand(ChestRestock plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Only in-game players may use this command!");
            return true;
        }
        if (!sender.isOp()) {
            sender.sendMessage("You do not have permission to access this command!");
            return true;
        }

        if (args.length == 0) {
            return false;
        }

        if (args[0].equalsIgnoreCase("help")) {
            // Help command
            if (args.length == 1) {
                // General help
                sender.sendMessage("ChestRestock allows you to set chests up to"
                        + " automatically restock the items currently in them"
                        + " at specified periods.");
                sender.sendMessage("Type /chestrestock help [sub-command] to "
                        + "get information about a specific sub-command.");
            } else if (args.length == 2) {
                if (args[1].equalsIgnoreCase("check")) {
                    sender.sendMessage("This will check to see if the chest is "
                            + "managed by ChestRestock and if so will show the"
                            + " period at which the chest will restock and "
                            + "the modes of the chest.");
                } else if (args[1].equalsIgnoreCase("set")) {
                    sender.sendMessage("This will initialize a chest to restock"
                            + " at the default period of 15 minutes and with "
                            + "the items it currently has inside.");
                } else if (args[1].equalsIgnoreCase("period")) {
                    sender.sendMessage("This will change the period at which "
                            + "the chest restocks it's contents.");
                } else if (args[1].equalsIgnoreCase("update")) {
                    sender.sendMessage("This will make the current contents of "
                            + "the chest the items it will auto-restock with.");
                } else if (args[1].equalsIgnoreCase("disable")) {
                    sender.sendMessage("This will disable auto-matic restocking"
                            + " of a chest.");
                } else if (args[1].equalsIgnoreCase("periodmode")) {
                    sender.sendMessage("This will change the period mode of the"
                            + " chest.  Options are \"player\" and "
                            + "\"settime\".");
                    sender.sendMessage("Player mode means the chest will "
                            + "restock every Period amount of seconds after a "
                            + "player checks a restocked chest.");
                    sender.sendMessage("SetTime mode means the chest will "
                            + "restock every Period amount of seconds after "
                            + "configuration, regardless of player interaction.");
                } else if (args[1].equalsIgnoreCase("restockmode")) {
                    sender.sendMessage("This will change the restock mode of "
                            + "the chest.  Options are \"add\" and \"replace\".");
                    sender.sendMessage("Add mode means the restock items will"
                            + "be added to whatever is in the chest.");
                    sender.sendMessage("Replace mode means the restock items "
                            + "will replace anything in the chest");
                } else if (args[1].equalsIgnoreCase("reload")) {
                    sender.sendMessage("This will reload ChestRestock's config "
                            + "file.  Use this when you change config values "
                            + "manually.");
                } else if (args[1].equalsIgnoreCase("preserveslots")) {
                    sender.sendMessage("This will set the default setting for"
                            + " whether or not to preserve the item slots of "
                            + "chests that are restocked.  That is to say, the "
                            + "position of restocked items will stay the same.");
                } else if (args[1].equalsIgnoreCase("indestructible")) {
                    sender.sendMessage("This will cause a chest managed by "
                            + "ChestRestock to become indestructible except by"
                            + " ops.");
                } else if (args[1].equalsIgnoreCase("playerlimit")) {
                    sender.sendMessage("This will change the number of times "
                            + "a single player can loot the chest.  Ops will "
                            + "not be limited.");
                    sender.sendMessage("-1 = no limit");
                    sender.sendMessage("0 = ops only");
                    sender.sendMessage("1 (or greater) = number of times a "
                            + "chest will restock for a player.");
                } else if (args[1].equalsIgnoreCase("restock")) {
                    sender.sendMessage("Restocks a configured chest "
                            + "immediately.");
                } else if (args[1].equalsIgnoreCase("name")) {
                    sender.sendMessage("Sets the name of a chest for easier "
                            + "manipulation through the config file.");
                } else {
                    sender.sendMessage("Unknown sub-command!");
                }
            } else {
                sender.sendMessage("The usage is: help [sub-command]");
            }
            return true;
        } else if (args[0].equalsIgnoreCase("check")) {
            // This will display information about the chest
            ChestData chest = plugin.getTargetedChest(sender);
            if (chest == null) {
                sender.sendMessage("You must be targetting a chest to use this command!");
                return true;
            }
            if (chest.isInConfig()) {
                // Chest is already configured
                if (chest.getName() != null) {
                    sender.sendMessage("Chest name: " + chest.getName());
                }
                sender.sendMessage("Chest restocks every " + chest.getPeriod() + " seconds.");
                sender.sendMessage("Chest restock mode: " + chest.getRestockMode());
                sender.sendMessage("Chest period mode: " + chest.getPeriodMode());
                sender.sendMessage("Chest will preserve slots: " + chest.getPreserveSlots());
                sender.sendMessage("Chest is indestructible: " + chest.isIndestructible());
                sender.sendMessage("Chest player limit: " + chest.getPlayerLimit());
                return true;
            } else {
                // Chest isn't configured
                sender.sendMessage("This chest is not configured for auto-restocking.");
                return true;
            }
        } else if (args[0].equalsIgnoreCase("set")) {
            ChestData chest = plugin.getTargetedChest(sender);
            if (chest == null) {
                sender.sendMessage("You must be targetting a chest to use this command!");
                return true;
            }
            if (chest.isInConfig()) {
                sender.sendMessage("This chest is already configured! Make use "
                        + "of the other sub-commands to change the options for"
                        + " this chest.");
                return true;
            }

            chest.setPeriod(plugin.config.getString("defaults.period"));
            chest.setRestockMode(plugin.config.getString("defaults.restockmode"));
            chest.setPeriodMode(plugin.config.getString("defaults.periodmode"));
            chest.setPreserveSlots(plugin.config.getString("defaults.preserveslots"));
            chest.setIndestructible(plugin.config.getString("defaults.indestructible"));
            chest.setPlayerLimit(plugin.config.getString("defaults.playerlimit"));
            chest.setRestockTimeNow();
            chest.setItems();
            plugin.config.save();
            sender.sendMessage("This chest will restock with the items currently"
                    + " inside of it, every " 
                    + plugin.config.getString("defaults.period") + " seconds. "
                    + "(default)");
            return true;
        } else if (args[0].equalsIgnoreCase("period")) {
            if (args.length < 2) {
                sender.sendMessage("The usage is: period <time>");
                sender.sendMessage("You must specify an amount of time in "
                        + "seconds to restock the chest!");
                return true;
            } else {
                ChestData chest = plugin.getTargetedChest(sender);
                if (chest == null) {
                    sender.sendMessage("You must be targetting a chest to use "
                            + "this command!");
                    return true;
                }
                if (!chest.isInConfig()) {
                    sender.sendMessage("You must set up this chest with the set "
                            + "command first!");
                    return true;
                }
                try {
                    Integer.parseInt(args[1]);
                    chest.setPeriod(args[1]);
                    chest.setRestockTimeNow();
                    plugin.config.save();
                    sender.sendMessage("Chest will now restock every " + args[1]
                            + " seconds.");
                    return true;
                } catch (NumberFormatException ignore) {
                    sender.sendMessage("You must specify a number of seconds!");
                    return true;
                }
            }
        } else if (args[0].equalsIgnoreCase("update")) {
            ChestData chest = plugin.getTargetedChest(sender);
            if (chest == null) {
                sender.sendMessage("You must be targetting a chest to use "
                        + "this command!");
                return true;
            }
            if (!chest.isInConfig()) {
                sender.sendMessage("You must set up this chest with the set "
                        + "command first!");
                return true;
            }
            chest.setRestockTimeNow();
            chest.setItems();
            plugin.config.save();
            sender.sendMessage("Chest will now restock with what is currently "
                    + "inside of it.");
            return true;
        } else if (args[0].equalsIgnoreCase("disable")) {
            ChestData chest = plugin.getTargetedChest(sender);
            if (chest == null) {
                sender.sendMessage("You must be targetting a chest to use "
                        + "this command!");
                return true;
            }
            if (!chest.isInConfig()) {
                sender.sendMessage("This chest is not already set up to "
                        + "auto-restock!");
                return true;
            }
            chest.disable();
            plugin.config.save();
            sender.sendMessage("Chest will no longer be automatically "
                    + "restocked.");
            return true;
        } else if (args[0].equalsIgnoreCase("periodmode")) {
            if (args.length < 2) {
                sender.sendMessage("The usage is: periodmode <player|settime>");
                sender.sendMessage("You must specify the mode to set, player or"
                        + " settime!");
                return true;
            } else {
                ChestData chest = plugin.getTargetedChest(sender);
                if (chest == null) {
                    sender.sendMessage("You must be targetting a chest to use "
                            + "this command!");
                    return true;
                }
                if (!chest.isInConfig()) {
                    sender.sendMessage("You must set up this chest with the set"
                            + " command first!");
                    return true;
                }
                if (args[1].equalsIgnoreCase("player")) {
                    sender.sendMessage("This chest will now restock every "
                            + chest.getPeriod() + " seconds after a player has"
                            + "checked it.");
                    chest.setPeriodMode("player");
                    plugin.config.save();
                } else if (args[1].equalsIgnoreCase("settime")) {
                    sender.sendMessage("This chest will now restock every "
                            + chest.getPeriod() + " seconds, regardless of "
                            + "player interaction.");
                    chest.setPeriodMode("settime");
                    plugin.config.save();
                } else {
                    sender.sendMessage("Invalid period mode!");
                }
                return true;
            }
        } else if (args[0].equalsIgnoreCase("restockmode")) {
            if (args.length < 2) {
                sender.sendMessage("The usage is: restockmode <add|replace>");
                sender.sendMessage("You must specify the mode to set, add or "
                        + "replace!");
                return true;
            } else {
                ChestData chest = plugin.getTargetedChest(sender);
                if (chest == null) {
                    sender.sendMessage("You must be targetting a chest to use "
                            + "this command!");
                    return true;
                }
                if (!chest.isInConfig()) {
                    sender.sendMessage("You must set up this chest with the set"
                            + " command first!");
                    return true;
                }
                if (args[1].equalsIgnoreCase("add")) {
                    sender.sendMessage("When restocking, items will be ADDED to"
                            + " this chest.");
                    chest.setRestockMode("add");
                    plugin.config.save();
                } else if (args[1].equalsIgnoreCase("replace")) {
                    sender.sendMessage("When restocking, items will be REPLACED"
                            + " in this chest.");
                    chest.setRestockMode("replace");
                    plugin.config.save();
                } else {
                    sender.sendMessage("Invalid restock mode!");
                }
                return true;
            }
        } else if (args[0].equalsIgnoreCase("reload")) {
            plugin.reload(true);
            sender.sendMessage("Reloaded ChestRestock configuration/data");
            return true;
        } else if (args[0].equalsIgnoreCase("preserveslots")) {
            if (args.length < 2) {
                sender.sendMessage("The usage is: preserveslots <true|false>");
                sender.sendMessage("You must specify the mode to set, true or "
                        + "false!");
                return true;
            } else {
                ChestData chest = plugin.getTargetedChest(sender);
                if (chest == null) {
                    sender.sendMessage("You must be targetting a chest to use "
                            + "this command!");
                    return true;
                }
                if (!chest.isInConfig()) {
                    sender.sendMessage("You must set up this chest with the set"
                            + " command first!");
                    return true;
                }
                if (args[1].equalsIgnoreCase("true")) {
                    sender.sendMessage("This chest will now keep the position "
                            + "of each item intact when it restocks.");
                    chest.setPreserveSlots("true");
                    plugin.config.save();
                } else if (args[1].equalsIgnoreCase("false")) {
                    sender.sendMessage("This chest will not try to keep the "
                            + "position of each item intact when it restocks.");
                    chest.setPreserveSlots("false");
                    plugin.config.save();
                } else {
                    sender.sendMessage("Invalid preserve slots setting!");
                }
                return true;
            }
        } else if (args[0].equalsIgnoreCase("indestructible")) {
            if (args.length < 2) {
                sender.sendMessage("The usage is: indestructible <true|false>");
                sender.sendMessage("You must specify the mode to set, true or "
                        + "false!");
                return true;
            } else {
                ChestData chest = plugin.getTargetedChest(sender);
                if (chest == null) {
                    sender.sendMessage("You must be targetting a chest to use "
                            + "this command!");
                    return true;
                }
                if (!chest.isInConfig()) {
                    sender.sendMessage("You must set up this chest with the set"
                            + " command first!");
                    return true;
                }
                if (args[1].equalsIgnoreCase("true")) {
                    sender.sendMessage("This chest will now be indestructible "
                            + "except by ops.");
                    chest.setIndestructible("true");
                    plugin.config.save();
                } else if (args[1].equalsIgnoreCase("false")) {
                    sender.sendMessage("This chest will will be destroyable by "
                            + "anyone unless you have further protection against"
                            + " such things.");
                    chest.setIndestructible("false");
                    plugin.config.save();
                } else {
                    sender.sendMessage("Invalid indestructible setting!");
                }
                return true;
            }
        } else if (args[0].equalsIgnoreCase("playerlimit")) {
            if (args.length < 2) {
                sender.sendMessage("The usage is: playerlimit <-1|0|X> (where X"
                        + " is a positive integer.)");
                sender.sendMessage("You must specify the limit!");
                return true;
            } else {
                ChestData chest = plugin.getTargetedChest(sender);
                if (chest == null) {
                    sender.sendMessage("You must be targetting a chest to use "
                            + "this command!");
                    return true;
                }
                if (!chest.isInConfig()) {
                    sender.sendMessage("You must set up this chest with the set"
                            + " command first!");
                    return true;
                }
                try {
                    int limit = Integer.parseInt(args[1]);
                    if (limit < -1) {
                        throw new NumberFormatException();
                    }
                    if (limit == -1) {
                        sender.sendMessage("Player limit has been removed for "
                                + "this chest!");
                    } else {
                        sender.sendMessage("This chest will restock " + limit +
                                " times for each player!");
                    }
                    chest.setPlayerLimit(args[1]);
                    plugin.config.save();
                } catch (NumberFormatException nfe) {
                    sender.sendMessage("You must specify an integer! -1, 0, or "
                            + "anything greater than 0.");
                }
                return true;
            }
        } else if (args[0].equalsIgnoreCase("restock")) {
            ChestData chest = plugin.getTargetedChest(sender);
            if (chest == null) {
                sender.sendMessage("You must be targetting a chest to use "
                        + "this command!");
                return true;
            }
            if (!chest.isInConfig()) {
                sender.sendMessage("This chest is not already set up to "
                        + "auto-restock!  It must be configured first with the "
                        + "set subcommand.");
                return true;
            }
            chest.restock();
            sender.sendMessage("Chest has been restocked!");
            return true;
        } else if (args[0].equalsIgnoreCase("name")) {
            if (args.length < 2) {
                sender.sendMessage("The usage is: name <chestname>");
                sender.sendMessage("You must specify the name only!");
                return true;
            } else {
                ChestData chest = plugin.getTargetedChest(sender);
                if (chest == null) {
                    sender.sendMessage("You must be targetting a chest to use "
                            + "this command!");
                    return true;
                }
                if (!chest.isInConfig()) {
                    sender.sendMessage("You must set up this chest with the set"
                            + " command first!");
                    return true;
                }
                chest.setName(args[1]);
                sender.sendMessage("Chest renamed to: " + args[1]);
                plugin.config.save();
                return true;
            }
        }

        return false;
    }


}
