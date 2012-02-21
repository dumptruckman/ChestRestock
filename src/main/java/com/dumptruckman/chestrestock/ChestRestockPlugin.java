package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.ChestData;
import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.api.Config;

import java.util.List;

import com.dumptruckman.chestrestock.util.Logging;
import com.dumptruckman.chestrestock.util.Perm;
import com.dumptruckman.chestrestock.util.locale.Messager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;

/**
 *
 * @author dumptruckman
 */
public class ChestRestockPlugin extends JavaPlugin implements ChestRestock {

    public void onLoad() { }

    public void onEnable() {
        Logging.init(this);
        Perm.register(this);

        // Grab the PluginManager
        final PluginManager pm = getServer().getPluginManager();
        // Register event listeners
        pm.registerEvents(new ChestRestockListener(this), this);

        // Display enable message/version info
        Logging.info("Enabled");
    }

    public void onDisable() {
        Logging.info("Disabled");
    }

    /*
     * Returns the dChest Data for the chest the player is targetting.
     * If the player is not targetting a chest, returns null.
     */
    public DefaultRestockChest getTargetedChest(CommandSender sender) {
        /*
        Player player = (Player)sender;
        Block block = player.getTargetBlock(null, 60);
        if (block.getType() == Material.CHEST) {
            return new DefaultRestockChest(block, this);
        } else {
            return null;
        }
        */
        return null;
    }

    /*
    public void convertOldConfig() {
        if (config.getKeys().contains("chests")) {
            logger.info("[" + plugname + "] Converting old data");
            List<String> worlds = config.getKeys("chests");
            if (worlds == null) return;
            for (int i = 0; i < worlds.size(); i++) {
                List<String> chests = config.getKeys("chests." + worlds.get(i));
                if (chests == null) break;
                for (int j = 0; j < chests.size(); j++) {
                    String chest = worlds.get(i) + "." + chests.get(i) + ".";
                    try {
                        chestConfig.setProperty(chest + "preserveslots", config.getString("chests." + chest + "preserveslots"));
                        config.removeProperty("chests." + chest + "preserveslots");
                    } catch (NullPointerException e) { }
                    try {
                        chestConfig.setProperty(chest + "playerlimit", config.getProperty("chests." + chest + "playerlimit"));
                        config.removeProperty("chests." + chest + "playerlimit");
                    } catch (NullPointerException e) { }
                    try {
                        chestData.setProperty(chest + "lastrestock", config.getProperty("chests." + chest + "lastrestock"));
                        config.removeProperty("chests." + chest + "lastrestock");
                    } catch (NullPointerException e) { }
                    try {
                        chestConfig.setProperty(chest + "items", config.getProperty("chests." + chest + "items"));
                        config.removeProperty("chests." + chest + "items");
                    } catch (NullPointerException e) { }
                    try {
                        chestConfig.setProperty(chest + "restockmode", config.getProperty("chests." + chest + "restockmode"));
                        config.removeProperty("chests." + chest + "restockmode");
                    } catch (NullPointerException e) { }
                    try {
                        chestConfig.setProperty(chest + "period", config.getProperty("chests." + chest + "period"));
                        config.removeProperty("chests." + chest + "period");
                    } catch (NullPointerException e) { }
                    try {
                        chestConfig.setProperty(chest + "indestructible", config.getProperty("chests." + chest + "indestructible"));
                        config.removeProperty("chests." + chest + "indestructible");
                    } catch (NullPointerException e) { }
                    try {
                        chestConfig.setProperty(chest + "periodmode", config.getProperty("chests." + chest + "periodmode"));
                        config.removeProperty("chests." + chest + "periodmode");
                    } catch (NullPointerException e) { }
                    try {
                        chestData.setProperty(chest + "players", config.getProperty("chests." + chest + "players"));
                        config.removeProperty("chests." + chest + "players");
                    } catch (NullPointerException e) { }
                    try {
                        chestConfig.setProperty(chest + "name", config.getProperty("chests." + chest + "name"));
                        config.removeProperty("chests." + chest + "name");
                    } catch (NullPointerException e) { }
                }
            }
        }
    }*/

    public void restockInventory(Inventory inventory, DefaultRestockChest chest, List<DefaultChestItem> items) {
       /* //Inventory inventory = chest.getFullInventory();
        if (chest.getRestockMode().equalsIgnoreCase("replace")) {
            inventory.clear();
        }
        for (int i = 0; i < items.size(); i++) {
            if (chest.getPreserveSlots().equalsIgnoreCase("true")) {
                if (chest.getRestockMode().equalsIgnoreCase("add")) {
                    if (items.get(i).getType().equals(inventory
                            .getItem(items.get(i).getSlot()).getType()) &&
                            items.get(i).getDurability() == inventory
                            .getItem(items.get(i).getSlot()).getDurability()) {
                        int newamount = items.get(i).getAmount() + inventory
                            .getItem(items.get(i).getSlot()).getAmount();
                        if (newamount > 64) newamount = 64;
                        items.get(i).setAmount(newamount);
                    }
                }
                inventory.setItem(items.get(i).getSlot(), items.get(i));
            } else {
                inventory.addItem(items.get(i));
            }
        }
        //return inventory;*/
    }

    public Inventory getInventoryWithItems(Inventory inventory, List<DefaultChestItem> items) {
        for (int i = 0; i < items.size(); i++) {
            inventory.setItem(items.get(i).getSlot(), items.get(i));
        }
        return inventory;
    }

    @Override
    public Config getSettings() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ChestData getData() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Messager getMessager() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setMessager(Messager messager) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
