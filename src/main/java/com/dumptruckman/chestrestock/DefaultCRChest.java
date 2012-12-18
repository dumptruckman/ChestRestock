package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.CRChest;
import com.dumptruckman.chestrestock.api.CRPlayer;
import com.dumptruckman.chestrestock.api.ChestRestock;
import com.dumptruckman.chestrestock.api.LootTable;
import com.dumptruckman.chestrestock.util.BlockLocation;
import com.dumptruckman.chestrestock.util.InventoryTools;
import com.dumptruckman.chestrestock.util.Perms;
import com.dumptruckman.minecraft.pluginbase.config.AbstractYamlConfig;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
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
        super(plugin, false, true, configFile, configClasses);
        this.plugin = plugin;
        this.location = location;
        Block block = location.getBlock();
        if (block == null) {
            throw new IllegalStateException("The world '" + location.getWorldName() + "' is not loaded!");
        }
        if (!(block.getState() instanceof InventoryHolder)) {
            if (plugin.hasChestManagerLoaded()) {
                plugin.getChestManager().removeChest(location);
                throw new IllegalStateException("The location '" + location.toString() + "' is not a inventory block!");
            }
        }
        save();
        Logging.finer("Finished object initialization of " + this);
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

    public boolean isValid() {
        Block block = getLocation().getBlock();
        if (block == null || !(block.getState() instanceof InventoryHolder)) {
            return false;
        }
        return true;
    }

    @Override
    public Inventory getInventory(HumanEntity player) {
        Logging.finest("Getting inventory for: " + player);
        Inventory inventory;
        if (player != null && get(UNIQUE)) {
            inventory = playerInventories.get(player.getName());
            if (inventory == null) {
                if (getInventoryHolder().getInventory().getType() == InventoryType.CHEST) {
                    inventory = Bukkit.createInventory(getInventoryHolder(),
                            getInventoryHolder().getInventory().getSize());
                    Logging.finer("Created new chest inventory for: " + player);
                } else {
                    Logging.fine("Workaround: Non-chest unique inventories are currently disabled due to Bukkit bug! returning physical inventory...");
                    inventory = getInventoryHolder().getInventory();
                    return inventory;
                    /* TODO Re-add this when https://bukkit.atlassian.net/browse/BUKKIT-1929 is fixed.
                    inventory = Bukkit.createInventory(getInventoryHolder(),
                            getInventoryHolder().getInventory().getType());
                    Logging.finer("Created new other inventory for: " + player);
                    */
                }
                //inventory.setContents(getInventoryHolder().getInventory().getContents());
                playerInventories.put(player.getName(), inventory);
            } else {
                Logging.finer("Got existing unique inventory for: " + player);
            }
        } else {
            Logging.finer("Got non-unique physical inventory");
            inventory = getInventoryHolder().getInventory();
        }
        return inventory;
    }

    @Override
    public void update(HumanEntity player) {
        Inventory inventory = getInventory(player);
        ItemStack[] items = InventoryTools.fillWithAir(new ItemStack[CRChest.Constants.getMaxInventorySize()]);
        ItemStack[] chestContents = inventory.getContents();
        for (int i = 0; i < chestContents.length; i++) {
            items[i] = new ItemStack(chestContents[i]);
        }
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
        save();
    }
    
    private boolean maybeRestock(HumanEntity player, CRPlayer crPlayer, Inventory inventory) {
        boolean restock = false;
        long accessTime = System.currentTimeMillis();
        long lastRestock = get(LAST_RESTOCK);
        if (get(PERIOD) < 1) {
            Logging.fine("Chest does use period based restocking");
            return restock;
        }
        if (crPlayer != null && get(UNIQUE)) {
            lastRestock = crPlayer.getLastRestockTime();
            //if (lastRestock == 0) {
            //    lastRestock = get(LAST_RESTOCK);
            //}
        }
        if (player == null || get(PLAYER_LIMIT) < 0 || hasLootBypass(player) || crPlayer.getLootCount() < get(PLAYER_LIMIT)) {
            Logging.finer("Last restock (unique: " + get(UNIQUE) + "): " + lastRestock + "  Access time: " + accessTime + "  Time diff: " + (accessTime - lastRestock));
            if (accessTime < lastRestock + (get(PERIOD) * 1000)) {
                Logging.finest("Not time to restock");
                return restock;
            }
            Logging.finest("Preparing to restock...");
            long missedPeriods = (accessTime - lastRestock) / (get(PERIOD) * 1000);
            Logging.finest("Missed " + missedPeriods + " restock periods");
            if (get(PERIOD_MODE).equalsIgnoreCase(PERIOD_MODE_PLAYER)) {
                if (crPlayer != null && player != null && get(UNIQUE)) {
                    Logging.finest("Setting last restock for '" + player.getName() + "' to " + accessTime);
                    crPlayer.setLastRestockTime(accessTime);
                } else {
                    Logging.finest("Setting last restock to " + accessTime);
                    set(LAST_RESTOCK, accessTime);
                }
            } else {
                long newRestockTime = lastRestock + (missedPeriods * (get(PERIOD) * 1000));
                if (crPlayer != null && player != null && get(UNIQUE)) {
                    Logging.finest("Setting fixed last restock for '" + player.getName() + "' to " + newRestockTime);
                    crPlayer.setLastRestockTime(newRestockTime);
                } else {
                    Logging.finest("Setting fixed last restock to " + newRestockTime);
                    set(LAST_RESTOCK, newRestockTime);
                }
            }
            if (get(ONLY_RESTOCK_EMPTY)) {
                if (InventoryTools.isEmpty(inventory.getContents())) {
                    restock = true;
                }
            } else {
                restock = true;
            }
            if (restock) {
                restock(inventory);
                if (crPlayer != null && player != null) {
                    Logging.finest("Increasing loot count for '" + player.getName() + "'");
                    crPlayer.setLootCount(crPlayer.getLootCount() + 1);
                    updatePlayerData(player.getName(), crPlayer);
                }
            }
        } else {
            Logging.finer("'" + player.getName() + "' no longer allowed to loot this chest!");
        }
        save();
        return restock;
    }
    
    public void restock(Inventory inventory) {
        if (get(RESTOCK_MODE).equalsIgnoreCase(RESTOCK_MODE_REPLACE)) {
            Logging.finest("Clearing inventory before restock");
            inventory.clear();
        }
        Logging.finer("Restocking " + inventory);
        ItemStack[] restockItems = get(ITEMS);
        if (get(PRESERVE_SLOTS)) {
            for (int i = 0; i < restockItems.length && i < inventory.getSize(); i++) {
                ItemStack existingItem = inventory.getItem(i);
                ItemStack restockItem = restockItems[i];
                if (existingItem != null
                        && existingItem.getType() == restockItem.getType()
                        && existingItem.getDurability() == restockItem.getDurability()
                        && existingItem.getEnchantments().equals(restockItem.getEnchantments())) {
                    int newAmount = existingItem.getAmount();
                    newAmount += restockItem.getAmount();
                    if (newAmount > existingItem.getType().getMaxStackSize()) {
                        newAmount = existingItem.getType().getMaxStackSize();
                    }
                    restockItem.setAmount(newAmount);
                }
                inventory.setItem(i, restockItem);
            }
        } else {
            for (ItemStack item : restockItems) {
                if (item.getType() != Material.AIR) {
                    inventory.addItem(item);
                }
            }
        }
        LootTable lootTable = plugin.getLootConfig().getLootTable(get(LOOT_TABLE));
        if (lootTable != null) {
            lootTable.addToInventory(inventory);
        }
        if (!get(GLOBAL_MESSAGE).isEmpty()) {
            Bukkit.broadcastMessage(get(GLOBAL_MESSAGE));
        }
    }

    @Override
    public void restockAllInventories() {
        restock(getInventory(null));
        for (Map.Entry<String, Inventory> entry : playerInventories.entrySet()) {
            restock(entry.getValue());
        }
    }

    private boolean hasLootBypass(HumanEntity player) {
        if (!get(NAME).isEmpty()) {
            return Perms.BYPASS_LOOT_LIMIT.specific(get(NAME)).hasPermission(player);
        } else {
            return Perms.BYPASS_LOOT_LIMIT_ANY.hasPermission(player);
        }
    }

    @Override
    public boolean openInventory(HumanEntity player) {
        if (player != null) {
            CRPlayer crPlayer = getPlayerData(player.getName());
            Inventory inventory = getInventory(player);
            boolean result = maybeRestock(player, crPlayer, inventory);
            player.openInventory(inventory);
            return result;
        } else {
            return maybeRestock(null, null, getInventory(null));
        }
    }
}
