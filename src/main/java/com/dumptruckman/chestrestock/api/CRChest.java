package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.Players;
import com.dumptruckman.chestrestock.util.BlockLocation;
import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.minecraft.pluginbase.config.Config;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.EntryBuilder;
import com.dumptruckman.minecraft.pluginbase.config.EntrySerializer;
import com.dumptruckman.minecraft.pluginbase.config.EntryValidator;
import com.dumptruckman.minecraft.pluginbase.config.MappedConfigEntry;
import com.dumptruckman.minecraft.pluginbase.locale.Message;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public interface CRChest extends Config {
    
    final int MAX_SIZE = 54;

    ConfigEntry<Boolean> PRESERVE_SLOTS = new EntryBuilder<Boolean>(Boolean.class, "preserve_slots").def(true).stringSerializer().build();

    ConfigEntry<Boolean> INDESTRUCTIBLE = new EntryBuilder<Boolean>(Boolean.class, "indestructible").def(true).stringSerializer().build();

    ConfigEntry<Integer> PLAYER_LIMIT = new EntryBuilder<Integer>(Integer.class, "player_limit").def(-1).stringSerializer().build();

    ConfigEntry<Boolean> UNIQUE = new EntryBuilder<Boolean>(Boolean.class, "unique").def(true).stringSerializer().build();

    ConfigEntry<Boolean> REDSTONE = new EntryBuilder<Boolean>(Boolean.class, "redstone").def(false).stringSerializer().build();

    ConfigEntry<Integer> PERIOD = new EntryBuilder<Integer>(Integer.class, "period").def(900).stringSerializer().build();

    ConfigEntry<String> NAME = new EntryBuilder<String>(String.class, "name").def("").build();

    ConfigEntry<String> GLOBAL_MESSAGE = new EntryBuilder<String>(String.class, "global_message").def("").build();

    String PERIOD_MODE_PLAYER = "player";
    String PERIOD_MODE_FIXED = "fixed";
    ConfigEntry<String> PERIOD_MODE = new EntryBuilder<String>(String.class, "period_mode").def(PERIOD_MODE_PLAYER)
            .validator(new EntryValidator() {
                @Override
                public boolean isValid(Object o) {
                    String value = o.toString();
                    return value.equalsIgnoreCase(PERIOD_MODE_PLAYER) || value.equalsIgnoreCase(PERIOD_MODE_FIXED);
                }

                @Override
                public Message getInvalidMessage() {
                    return Language.PERIOD_MODE_INVALID;
                }
            }).build();

    String RESTOCK_MODE_REPLACE = "replace";
    String RESTOCK_MODE_ADD = "add";
    ConfigEntry<String> RESTOCK_MODE = new EntryBuilder<String>(String.class, "restock_mode").def(RESTOCK_MODE_REPLACE)
            .validator(new EntryValidator() {
                @Override
                public boolean isValid(Object o) {
                    String value = o.toString();
                    return value.equalsIgnoreCase(RESTOCK_MODE_ADD) || value.equalsIgnoreCase(RESTOCK_MODE_REPLACE);
                }

                @Override
                public Message getInvalidMessage() {
                    return Language.RESTOCK_MODE_INVALID;
                }
            }).build();

    ConfigEntry<ItemStack[]> ITEMS = new EntryBuilder<ItemStack[]>(ItemStack[].class, "items")
            .def(new ItemStack[MAX_SIZE]).serializer(new EntrySerializer<ItemStack[]>() {
                @Override
                public ItemStack[] deserialize(Object o) {
                    return DataStrings.parseInventory(o.toString(), MAX_SIZE);
                }

                @Override
                public Object serialize(ItemStack[] itemStacks) {
                    return DataStrings.valueOf(itemStacks);
                }
            }).build();

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

    ConfigEntry<Long> LAST_RESTOCK = new EntryBuilder<Long>(Long.class, "lastRestockTime").def(0L).stringSerializer().build();

    BlockLocation getLocation();

    InventoryHolder getInventoryHolder();
    
    Inventory getInventory(HumanEntity player);

    void update(HumanEntity player);
    
    void restock(Inventory inventory);

    void restockAllInventories();

    void openInventory(HumanEntity player);
    
    CRPlayer getPlayerData(String name);
}
