package com.dumptruckman.dchest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.block.CraftChest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.config.Configuration;
import org.bukkitcontrib.block.ContribChest;


/**
 *
 * @author dumptruckman
 */
public class ChestData {

    public static final int CHESTSIZE = 27;
    
    private DChest plugin;
    private Chest chest;
    private Block block;
    private Chest chestPair;
    private String configPath;
    private String configName;
    private boolean inConfig;

    public ChestData(Block block, DChest plugin) {
        //this.block = block;
        this.plugin = plugin;
        this.block = block;
        chest = (Chest)block.getState();

        // Set the path of this chest for the config file
        configName = chest.getX() + "-" + chest.getY() + "-" + chest.getZ();
        configPath = chest.getWorld().getName() + "." + configName;

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
        if (plugin.chestConfig.getNode(configPath) != null) {
            inConfig = true;
        } else {
            // If there's a pair, check for it in the config
            if (chestPair != null) {
                String chestpairconfigname = chestPair.getX() + "-" 
                        + chestPair.getY() + "-" + chestPair.getZ();
                String chestpairconfigpath = chestPair.getWorld().getName()
                        + "." + chestpairconfigname;
                if (plugin.chestConfig.getNode(chestpairconfigpath) != null) {
                    // And switch with the original chest info if found
                    Chest temp = chest;
                    chest = chestPair;
                    chestPair = temp;
                    configName = chestpairconfigname;
                    configPath = chestpairconfigpath;
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
            if (isUnique() == null) {
                setUnique(plugin.config.getString("defaults.unique"));
            }
        }
    }

    public Inventory getInventory(boolean giveDouble) {
        if (giveDouble) {
            ContribChest contribchest = (ContribChest)chest;
            return contribchest.getFullInventory();
        } else {
            return chest.getInventory();
        }
    }

    public Inventory getFullInventory() {
        ContribChest contribchest = (ContribChest)chest;
        return contribchest.getFullInventory();
    }

    public Inventory getInventory() {
        return getInventory(false);
    }

    public Location getLocation() {
        return chest.getBlock().getLocation();
    }

    public Chest getChest() { return chest; }

    public boolean isDouble() {
        if (chestPair != null) {
            return true;
        } else {
            return false;
        }
    }
    
    public String getConfigPath() { return configPath; }

    public boolean isInConfig() { return inConfig; }

    public void disable() {
        if (isInConfig()) {
            plugin.chestConfig.removeProperty(configPath);
            plugin.chestData.removeProperty(configPath);
        }
    }

    public List<ItemData> getItemsByPath(Configuration config, String path) {
        List<Object> items = config.getList(path);
        List<ItemData> itemstack = new ArrayList<ItemData>();
        for (int i = 0; i < items.size(); i++) {
            String[] item = items.get(i).toString().split("\\s");
            int itemid = Integer.parseInt(item[1].split(",")[0]);
            ItemData itemdata = new ItemData(itemid);
            try {
                itemdata.setSlot(Integer.parseInt(item[0]));
            } catch (Exception ignore) { }
            try {
                itemdata.setDurability(Short.parseShort(item[1].split(",")[1]));
            } catch (Exception ignore) { }
            try {
                itemdata.setAmount(Integer.parseInt(item[2]));
            } catch (Exception ignore) {
                itemdata.setAmount(1);
            }
            itemstack.add(itemdata);
        }
        return itemstack;
    }

    public List<ItemData> getItems() {
        return getItemsByPath(plugin.chestConfig, configPath + ".items");
    }

    public Inventory getRestockedInventory(List<ItemData> items) {
        Inventory inventory = getFullInventory();
        if (getRestockMode().equalsIgnoreCase("replace")) {
            inventory.clear();
        }
        for (int i = 0; i < items.size(); i++) {
            if (getPreserveSlots().equalsIgnoreCase("true")) {
                if (getRestockMode().equalsIgnoreCase("add")) {
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
        return inventory;
    }
    
    public void restock(List<ItemData> items) {
        for (int i = 0; i < items.size(); i++) {
            if (getPreserveSlots().equalsIgnoreCase("true")) {
                if (getRestockMode().equalsIgnoreCase("add")) {
                    if (items.get(i).getType().equals(chest.getInventory()
                            .getItem(items.get(i).getSlot()).getType()) && 
                            items.get(i).getDurability() == chest.getInventory()
                            .getItem(items.get(i).getSlot()).getDurability()) {
                        int newamount = items.get(i).getAmount() + chest.getInventory()
                            .getItem(items.get(i).getSlot()).getAmount();
                        if (newamount > 64) newamount = 64;
                        items.get(i).setAmount(newamount);
                    }
                }
                chest.getInventory().setItem(items.get(i).getSlot(), items.get(i));
            } else {
                chest.getInventory().addItem(items.get(i));
            }
        }
        /*
        List<Object> items = plugin.chestConfig.getList(configPath + ".items");
        for (int i = 0; i < items.size(); i++) {
            String[] item = items.get(i).toString().split("\\s");
            int itemid = Integer.parseInt(item[1].split(",")[0]);
            ItemStack itemstack = new ItemStack(itemid);
            try {
                itemstack.setDurability(Short.parseShort(item[1].split(",")[1]));
            } catch (Exception ignore) { }
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
        }*/
    }

    /*
     * The following methods all deal with chest config
     */
    public void setItems() {
        /*
        Map<String,Object> chestConfig = new HashMap<String,Object>();
        Map<String,Object> chestData = new HashMap<String,Object>();
        chestConfig.put("period", getPeriod());
        chestConfig.put("periodmode", getPeriodMode());
        chestConfig.put("restockmode", getRestockMode());
        chestData.put("lastrestock", getLastRestockTime());
        chestConfig.put("preserveslots", getPreserveSlots());
        chestConfig.put("indestructible", isIndestructible().toString());
        chestConfig.put("playerlimit", getPlayerLimit().toString());
        if (getName() != null) {
            chestConfig.put("name", getName());
        }
        List<String> players = getPlayers();
        if (players != null) {
            for(int i = 0; i < players.size(); i++) {
                Map<String,Object> attributeMap = new HashMap<String,Object>();
                attributeMap.put("restockcount", getPlayerRestockCount(players.get(i)).intValue());
                Map<String,Object> playerMap = new HashMap<String,Object>();
                playerMap.put(players.get(i), attributeMap);
                chestData.put("players", playerMap);
            }
        }
         *
         */

        List<String> chestContents = new ArrayList<String>();

        ItemStack[] items = chest.getInventory().getContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                String iteminfo = i + " "
                        + Integer.toString(items[i].getTypeId());
                iteminfo += "," + items[i].getDurability();
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
        plugin.chestConfig.setProperty(configPath + ".items", chestContents);
        //chestConfig.put("items", chestContents);

        //plugin.chestConfig.setProperty(configPath, chestConfig);
        //plugin.chestData.setProperty(configPath, chestData);
        //plugin.saveChestConfig();
        //plugin.saveChestData();
    }

    public void setPeriod(String newperiod) {
        plugin.chestConfig.setProperty(configPath + ".period", newperiod);
    }
    public String getPeriod() {
        return plugin.chestConfig.getString(configPath + ".period");
    }

    public void setPeriodMode(String newperiodmode) {
        plugin.chestConfig.setProperty(configPath + ".periodmode", newperiodmode);
    }
    public String getPeriodMode() {
        return plugin.chestConfig.getString(configPath + ".periodmode");
    }

    public void setRestockMode(String newrestockmode) {
        plugin.chestConfig.setProperty(configPath + ".restockmode", newrestockmode);
    }
    public String getRestockMode() {
        return plugin.chestConfig.getString(configPath + ".restockmode");
    }

    public void setPreserveSlots(String preserveslots) {
        plugin.chestConfig.setProperty(configPath + ".preserveslots", preserveslots);
    }
    public String getPreserveSlots() {
        return plugin.chestConfig.getString(configPath + ".preserveslots");
    }

    public void setIndestructible(String indestructible) {
        plugin.chestConfig.setProperty(configPath + ".indestructible", indestructible);
    }
    public Boolean isIndestructible() {
        String ind = plugin.chestConfig.getString(configPath + ".indestructible");
        if (ind == null) {
            return null;
        } else if (ind.equalsIgnoreCase("true")) {
            return true;
        } else {
            return false;
        }
    }

    public void setPlayerLimit(String limit) {
        plugin.chestConfig.setProperty(configPath + ".playerlimit", limit);
    }
    public Integer getPlayerLimit() {
        try {
            String limit = plugin.chestConfig.getString(configPath + ".playerlimit");
            if (limit == null) {
                return null;
            }
            return Integer.valueOf(limit);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    public void setName(String name) {
        plugin.chestConfig.setProperty(configPath + ".name", name);
    }
    public String getName() {
        return plugin.chestConfig.getString(configPath + ".name");
    }

    public void setUnique(String unique) {
        plugin.chestConfig.setProperty(configPath + ".unique", unique);
    }
    public Boolean isUnique() {
        String unique = plugin.chestConfig.getString(configPath + ".unique");
        if (unique == null) {
            return null;
        } else if (unique.equalsIgnoreCase("true")) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * The following methods all deal with setting chest data
     */
    public void setPlayerRestockCount(String playername, Integer newcount) {
        plugin.chestData.setProperty(configPath + ".players." + playername + ".restockcount", newcount.intValue());
    }
    public Integer getPlayerRestockCount(String playername) {
        String count = plugin.chestData.getString(configPath + ".players." + playername + ".restockcount");
        if (count == null) {
            return null;
        }
        try {
            return Integer.parseInt(count);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public List<String> getPlayers() {
        return plugin.chestData.getKeys(configPath + ".players");
    }

    public void setRestockTimeNow() {
        setRestockTime(new Date().getTime() / 1000);
    }
    public void setRestockTime(long time) {
        plugin.chestData.setProperty(configPath + ".lastrestock", time);
    }
    public long getLastRestockTime() {
        return Long.parseLong(plugin.chestData.getString(configPath + ".lastrestock"));
    }

    public void setPlayerRestockTimeNow(String name) {
        setPlayerRestockTime(name, new Date().getTime() / 1000);
    }
    public void setPlayerRestockTime(String name, long time) {
        plugin.chestData.setProperty(configPath + ".players." + name + ".lastrestock", time);
    }
    public Long getLastPlayerRestockTime(String name) {
        String temp = plugin.chestData.getString(configPath + ".players." + name + ".lastrestock");
        if (temp == null) {
            Long time;
            time = getLastRestockTime();
            setPlayerRestockTime(name, time);
            return time;
        } else {
            try {
                return Long.parseLong(temp);
            } catch (NumberFormatException e) {
                Long time;
                time = getLastRestockTime();
                setPlayerRestockTime(name, time);
                return time;
            }
        }
    }

    public List<ItemData> getPlayerItems(String name) {
        if (plugin.chestData.getString(configPath + ".players." + name + ".items") != null) {
            return getItemsByPath(plugin.chestData, configPath + ".players." + name + ".items");
        } else {
            return null;
        }
    }
    public void setPlayerItems(String name, Inventory inventory, Inventory inventory2) {
        List<String> chestContents = new ArrayList<String>();
        ItemStack[] items = inventory.getContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                String iteminfo = i + " "
                        + Integer.toString(items[i].getTypeId());
                iteminfo += "," + items[i].getDurability();
                iteminfo += " " + Integer.toString(items[i].getAmount());
                chestContents.add(iteminfo);
            }
        }
        if (inventory2 != null) {
            items = inventory2.getContents();
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
        plugin.chestData.setProperty(configPath + ".players." + name + ".items", chestContents);
    }
}
