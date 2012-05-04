package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.Players;
import com.dumptruckman.chestrestock.util.BlockLocation;
import com.dumptruckman.minecraft.pluginbase.config.Config;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.EntryBuilder;
import com.dumptruckman.minecraft.pluginbase.config.EntrySerializer;
import com.dumptruckman.minecraft.pluginbase.config.MappedConfigEntry;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public interface CRChest extends Config, CRChestOptions {

    class Constants {

        /**
         * The minimum for max inventory size.
         */
        public static final int MIN_MAX_INVENTORY_SIZE = 54;

        private static int MAX_INVENTORY_SIZE = MIN_MAX_INVENTORY_SIZE;

        /**
         * Sets the maximum size of any inventory for use in ChestRestock.
         *
         * @param size a value no less than {@link Constants#MIN_MAX_INVENTORY_SIZE}.
         */
        public static void setMaxInventorySize(int size) {
            if (size < MIN_MAX_INVENTORY_SIZE) {
                throw new IllegalArgumentException("Size may not be less than " + MIN_MAX_INVENTORY_SIZE);
            }
            MAX_INVENTORY_SIZE = size;
        }

        /**
         * @return the max size for any inventory for use in ChestRestock.
         */
        public static int getMaxInventorySize() {
            return MAX_INVENTORY_SIZE;
        }
    }

    /**
     * Indicates the maximum size of an inventory.
     * @deprecated as of release 2.3.  Use {@link CRConfig#MAX_INVENTORY_SIZE} and
     * {@link com.dumptruckman.chestrestock.api.CRChest.Constants#getMaxInventorySize()} instead.
     */
    @Deprecated
    int MAX_SIZE = 54;

    /**
     * The items this chest restocks with.
     */
    ConfigEntry<ItemStack[]> ITEMS = new EntryBuilder<ItemStack[]>(ItemStack[].class, "items")
            .def(new ItemStack[Constants.MAX_INVENTORY_SIZE]).serializer(new EntrySerializer<ItemStack[]>() {
                @Override
                public ItemStack[] deserialize(Object o) {
                    return DataStrings.parseInventory(o.toString(), Constants.MAX_INVENTORY_SIZE);
                }

                @Override
                public Object serialize(ItemStack[] itemStacks) {
                    return DataStrings.valueOf(itemStacks);
                }
            }).build();

    /**
     * Data pertaining to players interacting with this chest.  See {@link CRPlayer}
     */
    MappedConfigEntry<CRPlayer> PLAYERS = new EntryBuilder<CRPlayer>(CRPlayer.class, "players")
            .serializer(new EntrySerializer<CRPlayer>() {

                @Override
                public CRPlayer deserialize(Object o) {
                    int lootCount = 0;
                    long lastRestockTime = 0;
                    try {
                        if (o instanceof ConfigurationSection) {
                            o = ((ConfigurationSection) o).getValues(false);
                        }
                        Map<String, Object> map = (Map<String, Object>) o;
                        if (map == null) {
                            map = new HashMap<String, Object>();
                        }
                        Object obj = map.get("restockCount");
                        if (obj == null) {
                            obj = 0;
                        }
                        lootCount = Integer.valueOf(obj.toString());
                        obj = map.get("lastRestockTime");
                        if (obj == null) {
                            obj = 0L;
                        }
                        lastRestockTime = Long.valueOf(obj.toString());
                    } catch (ClassCastException e) {
                        Logging.warning("Error in player data!");
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        Logging.warning("Error in player data!");
                        e.printStackTrace();
                    }
                    return Players.newCRPlayer(lootCount, lastRestockTime);
                }

                @Override
                public Object serialize(CRPlayer crPlayer) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("restockCount", crPlayer.getLootCount());
                    map.put("lastRestockTime", crPlayer.getLastRestockTime());
                    return map;
                }
            }).buildMap();

    /**
     * The last time this chest was restocked (when not {@link CRChest#UNIQUE}).
     */
    ConfigEntry<Long> LAST_RESTOCK = new EntryBuilder<Long>(Long.class, "lastRestockTime").def(0L).stringSerializer().build();

    /**
     * @return The location of this chest.
     */
    BlockLocation getLocation();

    /**
     * @return true if the block for this CRChest is an InventoryHolder.
     */
    boolean isValid();

    /**
     * @return The InventoryHolder for this CRChest.
     */
    InventoryHolder getInventoryHolder();

    Inventory getInventory(HumanEntity player);

    void update(HumanEntity player);
    
    void restock(Inventory inventory);

    void restockAllInventories();

    void openInventory(HumanEntity player);
    
    CRPlayer getPlayerData(String name);
}
