package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.api.CRPlayer;
import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.util.BlockLocation;
import com.dumptruckman.chestrestock.util.InventoryTools;
import com.dumptruckman.chestrestock.util.Perms;
import com.dumptruckman.minecraft.pluginbase.config.AbstractYamlConfig;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class DefaultCRChest extends AbstractYamlConfig<CRChest> implements CRChest {
    
    private ChestRestock plugin;
    private BlockLocation location;
    
    private Map<String, Inventory> playerInventories = new HashMap<String, Inventory>();

    DefaultCRChest(ChestRestock plugin, BlockLocation location, File configFile, Class<? extends CRChest>... configClasses) throws IOException {
        super(plugin, false, configFile, configClasses);
        this.plugin = plugin;
        this.location = location;
        Block block = location.getBlock();
        if (block == null) {
            throw new IllegalStateException("The world '" + location.getWorldName() + "' is not loaded!");
        }
        if (!(block.getState() instanceof InventoryHolder)) {
            plugin.getChestManager().removeChest(location);
            throw new IllegalStateException("The location '" + location.toString() + "' is not a inventory block!");
        }
        save();
    }

    @Override
    public BlockLocation getLocation() {
        return location;
    }

    @Override
    public InventoryHolder getInventoryHolder() {
        Block block = getLocation().getBlock();
        if (block == null || !(block.getState() instanceof InventoryHolder)) {
            plugin.getChestManager().removeChest(getLocation());
            return null;
        }
        return (InventoryHolder) block.getState();
    }

    @Override
    public void update() {
        InventoryHolder holder = getInventoryHolder();
        if (holder == null) {
            return;
        }
        ItemStack[] items = InventoryTools.fillWithAir(new ItemStack[MAX_SIZE]);
        ItemStack[] chestContents = holder.getInventory().getContents();
        System.arraycopy(chestContents, 0, items, 0, chestContents.length);
        set(ITEMS, items);
        save();
    }

    @Override
    public CRPlayer getPlayerData(String name) {
        assert(name != null);
        CRPlayer player = get(PLAYERS.specific(name));
        if (player == null) {
            player = Players.newCRPlayer();
        }
        return player;
    }
    
    private void updatePlayerData(String name, CRPlayer player) {
        assert(name != null);
        assert(player != null);
        set(PLAYERS.specific(name), player);
    }

    @Override
    public Long getLastAccess() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    
    private void maybeRestock(HumanEntity player, CRPlayer crPlayer) {
        
        if (get(PLAYER_LIMIT) >= 0 && (crPlayer.getLootCount() < get(PLAYER_LIMIT) || Perms.)) {
            
        }
    }

    @Override
    public void openInventory(HumanEntity player) {
        assert(player != null);
        CRPlayer crPlayer = getPlayerData(player.getName());
        int playerRestockCount = chest.getPlayerRestockCount(event.getPlayer().getName());
        if (chest.getPlayerLimit() != -1) {
            if (timesrestockedforplayer >= chest.getPlayerLimit()) {
                if (!event.getPlayer().isOp()) {
                    return;
                }
            }
        }

        Long accesstime = new Date().getTime() / 1000;
        if (accesstime < (chest.getLastRestockTime() +
                Integer.parseInt(chest.getPeriod()))) {
            return;
        }

        event.setCancelled(true);

        int missedperiods = 1;
        if (chest.getPeriodMode() != null && chest.getPeriodMode().equalsIgnoreCase("player")) {
            missedperiods = (int)Math.floor((new Long(accesstime).doubleValue()
                    - new Long(chest.getLastRestockTime()).doubleValue())
                    / Integer.parseInt(chest.getPeriod()));
            chest.setRestockTime(accesstime);
        } else if (chest.getPeriodMode().equalsIgnoreCase("settime")) {
            chest.setRestockTime(new Double(Math.floor((
                    new Long(accesstime).doubleValue()
                            - new Long(chest.getLastRestockTime()).doubleValue())
                    / Integer.parseInt(chest.getPeriod()))
                    * Integer.parseInt(chest.getPeriod())).longValue()
                    + chest.getLastRestockTime());
        }

        ItemStack[] oldchestcontents = chest.getChest().getInventory().getContents();

        if (chest.getRestockMode().equalsIgnoreCase("replace")) {
            chest.getChest().getInventory().clear();
        }

        if (chest.getRestockMode().equalsIgnoreCase("add")) {
            for (int i = 0; i < missedperiods; i++) {
                chest.restock();
            }
        } else {
            chest.restock();
        }

        plugin.getServer().getPluginManager().callEvent(new PlayerInventoryEvent(event.getPlayer(), chest.getChest().getInventory()));

        chest.getChest().getInventory().setContents(oldchestcontents);

        if (missedperiods != 0) {
            timesrestockedforplayer++;
            chest.setPlayerRestockCount(event.getPlayer().getName(), timesrestockedforplayer);
        }
    }

    /*
    
    public String getConfigPath() { return configPath; }

    public boolean isInConfig() { return inConfig; }

    public void disable() {
        if (isInConfig()) {
            plugin.chestConfig.removeProperty(configPath);
            plugin.chestData.removeProperty(configPath);
        }
    }

    public List<ChestItem> getItemsByPath(Configuration config, String path) {
        List<Object> items = config.getList(path);
        List<ChestItem> itemstack = new ArrayList<ChestItem>();
        for (int i = 0; i < items.size(); i++) {
            String[] item = items.get(i).toString().split("\\s");
            int itemid = Integer.parseInt(item[1].split(",")[0]);
            ChestItem itemdata = new ChestItem(itemid);
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

    public List<ChestItem> getItems() {
        return getItemsByPath(plugin.chestConfig, configPath + ".items");
    }

    public Inventory getRestockedInventory(List<ChestItem> items) {
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
    
    public void restock(List<ChestItem> items) {
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
        }
    }

    public void setItems() {
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

    public List<ChestItem> getPlayerItems(String name) {
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
    */
}
