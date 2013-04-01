package com.dumptruckman.minecraft.chestrestock;

import com.dumptruckman.minecraft.chestrestock.util.Language;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.plugin.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.properties.ListProperty;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyFactory;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyValidator;
import com.dumptruckman.minecraft.pluginbase.properties.SimpleProperty;

import java.util.Arrays;

public interface CRConfig extends BaseConfig {

    /**
     * The interval at which a task performs restock polling on all chests set to {@link CRChest#ACCEPT_POLL}.
     */
    SimpleProperty<Integer> RESTOCK_TASK = PropertyFactory.newProperty(Integer.class, "settings.restock_task_interval", 60)
            .comment("# This is the interval (in seconds) at which a timer task will poll all 'accept_poll' chests to see if it should update.")
            .comment("# This could potentially affect performance if you have LOTS of chests set to accept polling")
            .comment("# To disable this polling feature, set this to 0").build();

    /**
     * A list of worlds for chests to be restocked in when they are reset by Multiverse-Adventure.
     */
    ListProperty<String> RESET_WORLDS = PropertyFactory.newListProperty(String.class, "settings.restock_chests_reset_worlds", Arrays.asList("hungergames"))
            .comment("# These are worlds that all ChestRestock chests will be restocked in when Multiverse-Adventure resets the worlds.")
            .comment("# Ignore this if you're not using Multiverse-Adventure").build();

    /**
     * The maximum size of any inventory.  This is used for inventory mod support.
     */
    SimpleProperty<Integer> MAX_INVENTORY_SIZE = PropertyFactory.newProperty(Integer.class, "settings.max_inventory_size", ChestConstants.getMaxInventorySize())
            .comment("# This is the maximum size of any inventory.  The default (54) is the size of a standard double chest.")
            .comment("# You should only adjust this value if you are using mods that allow larger inventories.")
            .validator(new PropertyValidator<Integer>() {
                @Override
                public boolean isValid(final Integer o) {
                    return o >= ChestConstants.MIN_MAX_INVENTORY_SIZE;
                }

                @Override
                public Message getInvalidMessage() {
                    return Language.MAX_INVENTORY_SIZE_INVALID;
                }
            })
            .build();
}
