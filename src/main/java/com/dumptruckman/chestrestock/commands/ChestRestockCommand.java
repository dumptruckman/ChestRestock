package com.dumptruckman.chestrestock.commands;

public abstract class ChestRestockCommand {

        /*
        else if (args[0].equalsIgnoreCase("period")) {
            if (args.length < 2) {
                plugin.sendMessage("commands.period.usage", sender);
                return true;
            } else {
                DefaultRestockableChest chest = plugin.getTargetedChest(sender);
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
        } else if (args[0].equalsIgnoreCase("disable")) {
            DefaultRestockableChest chest = plugin.getTargetedChest(sender);
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
                DefaultRestockableChest chest = plugin.getTargetedChest(sender);
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
                DefaultRestockableChest chest = plugin.getTargetedChest(sender);
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
                DefaultRestockableChest chest = plugin.getTargetedChest(sender);
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
                DefaultRestockableChest chest = plugin.getTargetedChest(sender);
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
                DefaultRestockableChest chest = plugin.getTargetedChest(sender);
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
            DefaultRestockableChest chest = plugin.getTargetedChest(sender);
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
                DefaultRestockableChest chest = plugin.getTargetedChest(sender);
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
                plugin.sendMessage("commands.unqiue.usage", sender);
                plugin.sendMessage("commands.nomode", sender);
                return true;
            } else {
                DefaultRestockableChest chest = plugin.getTargetedChest(sender);
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
                    plugin.sendMessage("commands.unqiue.true", sender);
                } else if (args[1].equalsIgnoreCase("false")) {
                    chest.setUnique("false");
                    plugin.saveChestConfig();
                    plugin.sendMessage("commands.unqiue.false", sender);
                } else {
                    plugin.sendMessage("commands.unqiue.invalid", sender);
                }
                return true;
            }
        }*/

}
