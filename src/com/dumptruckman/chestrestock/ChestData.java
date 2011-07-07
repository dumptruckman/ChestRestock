package com.dumptruckman.chestrestock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.config.ConfigurationNode;

/**
 *
 * @author dumptruckman
 */
public class ChestData {

    private static final int CHESTSIZE = 27;
    
    private ChestRestock plugin;
    private Chest chest;
    private Chest chestPair;
    private String configPath;
    private boolean inConfig;

    public ChestData(Block block, ChestRestock plugin) {
        //this.block = block;
        this.plugin = plugin;
        chest = (Chest)block.getState();

        // Set the path of this chest for the config file
        configPath = "chests." + chest.getWorld().getName() + "." + chest.getX() + "-" + chest.getY() + "-"
                + chest.getZ();

        // Detect chest pair
        chestPair = null;
        if (block.getRelative(BlockFace.NORTH).getType() == Material.CHEST)
            chestPair = (Chest)block.getRelative(BlockFace.NORTH).getState();
        if (block.getRelative(BlockFace.EAST).getType() == Material.CHEST)
            chestPair = (Chest)block.getRelative(BlockFace.EAST).getState();
        if (block.getRelative(BlockFace.SOUTH).getType() == Material.CHEST)
            chestPair = (Chest)block.getRelative(BlockFace.SOUTH).getState();
        if (block.getRelative(BlockFace.WEST).getType() == Material.CHEST)
            chestPair = (Chest)block.getRelative(BlockFace.WEST).getState();
            
        // See if the chest is present in the config
        if (plugin.config.getNode(configPath) != null) {
            inConfig = true;
        } else {
            // If there's a pair, check for it in the config
            if (chestPair != null) {
                String chestpairconfigname = "chests." + chestPair.getX()
                        + "-" + chestPair.getY() + "-" + chestPair.getZ();
                if (plugin.config.getNode(chestpairconfigname) != null) {
                    // And switch with the original chest info if found
                    Chest temp = chest;
                    chest = chestPair;
                    chestPair = temp;
                    configPath = chestpairconfigname;
                    inConfig = true;
                } else {
                    inConfig = false;
                }
                // See if the items are considered to be in the other chest's inventory

            } else {
                inConfig = false;
            }
        }

        if (inConfig) {
            // Ensure new config options are set for existing chest
            if (isIndestructible() == null) {
                setIndestructible(plugin.config.getString("defaults.indestructible"));
            }
            if (getPlayerLimit() == null) {
                setPlayerLimit(plugin.config.getString("defaults.playerlimit"));
            }
        }
    }

    public Chest getChest() { return chest; }
    
    public String getConfigPath() { return configPath; }

    public boolean isInConfig() { return inConfig; }

    public void setPeriod(String newperiod) {
        plugin.config.setProperty(configPath + ".period", newperiod);
    }
    public String getPeriod() {
        return plugin.config.getString(configPath + ".period");
    }

    public void setPeriodMode(String newperiodmode) {
        plugin.config.setProperty(configPath + ".periodmode", newperiodmode);
    }
    public String getPeriodMode() {
        return plugin.config.getString(configPath + ".periodmode");
    }

    public void setRestockMode(String newrestockmode) {
        plugin.config.setProperty(configPath + ".restockmode", newrestockmode);
    }
    public String getRestockMode() {
        return plugin.config.getString(configPath + ".restockmode");
    }

    public void setPreserveSlots(String preserveslots) {
        plugin.config.setProperty(configPath + ".preserveslots", preserveslots);
    }
    public String getPreserveSlots() {
        return plugin.config.getString(configPath + ".preserveslots");
    }

    public void setIndestructible(String indestructible) {
        plugin.config.setProperty(configPath + ".indestructible", indestructible);
    }
    public Boolean isIndestructible() {
        String ind = plugin.config.getString(configPath + ".indestructible");
        if (ind == null) {
            return null;
        } else if (ind.equalsIgnoreCase("true")) {
            return true;
        } else {
            return false;
        }
    }

    public void setPlayerLimit(String limit) {
        plugin.config.setProperty(configPath + ".playerlimit", limit);
    }
    public Integer getPlayerLimit() {
        try {
            String limit = plugin.config.getString(configPath + ".playerlimit");
            if (limit == null) {
                return null;
            }
            return Integer.valueOf(limit);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    public void setPlayerRestockCount(String playername, Integer newcount) {
        plugin.config.setProperty(configPath + ".players." + playername + ".restockcount", newcount.intValue());
    }
    public Integer getPlayerRestockCount(String playername) {
        String count = plugin.config.getString(configPath + ".players." + playername + ".restockcount");
        if (count == null) {
            return null;
        }
        try {
            return Integer.parseInt(count);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public void setName(String name) {
        plugin.config.setProperty(configPath + ".name", name);
    }
    public String getName() {
        return plugin.config.getString(configPath + ".name");
    }

    public List<String> getPlayers() {
        return plugin.config.getKeys(configPath + ".players");
    }

    public void setRestockTimeNow() {
        plugin.config.setProperty(configPath + ".lastrestock",
                new Date().getTime() / 1000);
    }
    public void setRestockTime(long time) {
        plugin.config.setProperty(configPath + ".lastrestock", time);
    }
    public long getLastRestockTime() {
        return Long.parseLong(plugin.config.getString(configPath + ".lastrestock"));
    }

    public void setItems() {
        Map<String,Object> chestMap = new HashMap<String,Object>();
        chestMap.put("period", getPeriod());
        chestMap.put("periodmode", getPeriodMode());
        chestMap.put("restockmode", getRestockMode());
        chestMap.put("lastrestock", getLastRestockTime());
        chestMap.put("preserveslots", getPreserveSlots());
        chestMap.put("indestructible", isIndestructible().toString());
        chestMap.put("playerlimit", getPlayerLimit().toString());
        if (getName() != null) {
            chestMap.put("name", getName());
        }
        List<String> players = getPlayers();
        if (players != null) {
            for(int i = 0; i < players.size(); i++) {
                Map<String,Object> attributeMap = new HashMap<String,Object>();
                attributeMap.put("restockcount", getPlayerRestockCount(players.get(i)).intValue());
                Map<String,Object> playerMap = new HashMap<String,Object>();
                playerMap.put(players.get(i), attributeMap);
                chestMap.put("players", playerMap);
            }
        }

        List<String> chestContents = new ArrayList<String>();

        ItemStack[] items = chest.getInventory().getContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                String iteminfo = i + " "
                        + Integer.toString(items[i].getTypeId());
                if (items[i].getData() != null) 
                    iteminfo += "," + Byte.toString(items[i].getData().getData());
                iteminfo += " " + Integer.toString(items[i].getAmount());
                chestContents.add(iteminfo);
            }
        }
        if (chestPair != null) {
            items = chestPair.getInventory().getContents();
            for (int i = 0; i < items.length; i++) {
                if (items[i] != null) {
                    int slot = i + CHESTSIZE;
                    String iteminfo = slot + " " 
                            + Integer.toString(items[i].getTypeId());
                    if (items[i].getData() != null)
                        iteminfo += "," + Byte.toString(items[i].getData().getData());
                    iteminfo += " " + Integer.toString(items[i].getAmount());
                    chestContents.add(iteminfo);
                }
            }
        }
        chestMap.put("items", chestContents);

        plugin.config.setProperty(configPath, chestMap);
    }

    public void disable() {
        if (isInConfig()) {
            plugin.config.removeProperty(configPath);
        }
    }

    public void restock() {
        List<Object> items = plugin.config.getList(configPath + ".items");
        for (int i = 0; i < items.size(); i++) {
            String[] item = items.get(i).toString().split("\\s");
            int itemid = Integer.parseInt(item[1].split(",")[0]);
            ItemStack itemstack = new ItemStack(itemid);
            try {
                itemstack.setData(new MaterialData(itemid, Byte.parseByte(item[1].split(",")[1])));
            } catch (Exception ignore) {}
            try {
                itemstack.setAmount(Integer.parseInt(item[2]));
            } catch (Exception ignore) {
                itemstack.setAmount(1);
            }
            if (getPreserveSlots().equalsIgnoreCase("true")) {
                if (getRestockMode().equalsIgnoreCase("add")) {
                    int newamount = itemstack.getAmount() + chest.getInventory()
                            .getItem(Integer.parseInt(item[0])).getAmount();
                    if (newamount > 64) newamount = 64;
                    itemstack.setAmount(newamount);
                }
                chest.getInventory().setItem(Integer.parseInt(item[0]), itemstack);
            } else {
                chest.getInventory().addItem(itemstack);
            }
            
        }
    }

    
}
