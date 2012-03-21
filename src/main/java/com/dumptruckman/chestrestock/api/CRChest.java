package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.BlockLocation;
import com.dumptruckman.minecraft.pluginbase.config.Config;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.EntryBuilder;
import com.dumptruckman.minecraft.pluginbase.config.EntrySerializer;
import com.dumptruckman.minecraft.pluginbase.config.EntryValidator;
import com.dumptruckman.minecraft.pluginbase.locale.Message;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface CRChest extends Config {
    
    final int MAX_SIZE = 54;

    ConfigEntry<Boolean> PRESERVE_SLOTS = new EntryBuilder<Boolean>(Boolean.class, "preserve_slots").def(true).stringSerializer().build();

    ConfigEntry<Boolean> INDESTRUCTIBLE = new EntryBuilder<Boolean>(Boolean.class, "indestructible").def(true).stringSerializer().build();

    ConfigEntry<Integer> PLAYER_LIMIT = new EntryBuilder<Integer>(Integer.class, "player_limit").def(-1).stringSerializer().build();

    ConfigEntry<Boolean> UNIQUE = new EntryBuilder<Boolean>(Boolean.class, "unique").def(true).stringSerializer().build();

    ConfigEntry<Integer> PERIOD = new EntryBuilder<Integer>(Integer.class, "period").def(900).stringSerializer().build();

    ConfigEntry<String> NAME = new EntryBuilder<String>(String.class, "name").def("").build();

    ConfigEntry<String> PERIOD_MODE = new EntryBuilder<String>(String.class, "period_mode").def("player")
            .validator(new EntryValidator() {
                @Override
                public boolean isValid(Object o) {
                    String value = o.toString();
                    return value.equalsIgnoreCase("player") || value.equalsIgnoreCase("fixed");
                }

                @Override
                public Message getInvalidMessage() {
                    //TODO
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }
            }).build();

    ConfigEntry<String> RESTOCK_MODE = new EntryBuilder<String>(String.class, "restock_mode").def("replace")
            .validator(new EntryValidator() {
                @Override
                public boolean isValid(Object o) {
                    String value = o.toString();
                    return value.equalsIgnoreCase("add") || value.equalsIgnoreCase("replace");
                }

                @Override
                public Message getInvalidMessage() {
                    //TODO
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
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

    //TODO add serializer
    ConfigEntry<CRPlayer> PLAYERS = new EntryBuilder<CRPlayer>(CRPlayer.class, "players").buildMap();

    

    BlockLocation getLocation();

    InventoryHolder getInventoryHolder();

    void update();

    void openInventory(HumanEntity player);
    
    Integer getLootCount(HumanEntity player);

    Long getLastAccess(HumanEntity player);
    
    Long getLastAccess();
}
