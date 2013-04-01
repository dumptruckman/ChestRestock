package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.Players;
import com.dumptruckman.chestrestock.util.BlockLocation;
import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.properties.MappedProperty;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyFactory;
import com.dumptruckman.minecraft.pluginbase.properties.PropertySerializer;
import com.dumptruckman.minecraft.pluginbase.properties.SimpleProperty;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * An interface that represents a ChestRestock "chest".  Chest being any sort of block based inventory.
 */
public interface CRChest extends Properties, CRChestOptions {

    /**
     * Constants related to CRChest.
     */
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
    SimpleProperty<ItemStack[]> ITEMS = PropertyFactory.newProperty(ItemStack[].class, "items",
            new ItemStack[Constants.MAX_INVENTORY_SIZE])
            .serializer(new PropertySerializer<ItemStack[]>() {
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
    MappedProperty<CRPlayer> PLAYERS = PropertyFactory.newMappedProperty(CRPlayer.class, "players")
            .serializer(new PropertySerializer<CRPlayer>() {

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
            }).build();

    /**
     * The last time this chest was restocked (when not {@link CRChest#UNIQUE}).
     */
    SimpleProperty<Long> LAST_RESTOCK = PropertyFactory.newProperty(Long.class, "lastRestockTime", 0L).build();

    //ConfigEntry<Long> CREATION_TIME = new EntryBuilder<Long>(Long.class, "creationTime").def(0L).stringSerializer().build();
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

    /**
     * Retrieves the inventory for this CRChest.  If player is null or this chest is not {@link CRChest#UNIQUE} this
     * inventory will be the physical inventory of the block.  Otherwise, a unique "fake" (non-block based) inventory
     * will be returned.
     *
     * @param player The player to get the inventory for if the chest is {@link CRChest#UNIQUE} or null to always
     *               get the physical inventory.
     * @return The physical inventory or a unique inventory for the player.
     */
    Inventory getInventory(HumanEntity player);

    /**
     * Updates the items to restock with with the inventory's current contents.
     *
     * @param player The player causing the update which is needed if the chest is {@link CRChest#UNIQUE} otherwise
     *               null to update with the contents of the physical inventory.
     */
    void update(HumanEntity player);

    /**
     * Restocks the inventory passed in using all the {@link CRChestOptions} of this chest.
     *
     * @param inventory The inventory to restock.
     */
    void restock(Inventory inventory);

    /**
     * This restocks all inventories of the chest, physical and player unique using all the {@link CRChestOptions} of
     * this chest.
     */
    void restockAllInventories();

    /**
     * Opens the inventory of this chest for the player passed in.  This causes the chest to consider restocking.
     * null may be used to possibly trigger restocking on the physical inventory.  This will literally open the
     * inventory window for the player passed in.
     *
     * @param player The player to open the inventory window for or null.
     * @return true if this caused the chest to restock.
     */
    boolean openInventory(HumanEntity player);

    /**
     * @param name The name of a player.
     * @return The data for the specified player for this chest.
     */
    CRPlayer getPlayerData(String name);
}
