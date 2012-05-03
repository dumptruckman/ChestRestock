package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.EntryBuilder;
import com.dumptruckman.minecraft.pluginbase.config.EntryValidator;
import com.dumptruckman.minecraft.pluginbase.locale.Message;

public interface CRChestOptions {

    ConfigEntry<String> NAME = new EntryBuilder<String>(String.class, "name").def("")
            .description(Language.NAME_DESC).build();

    ConfigEntry<Boolean> PRESERVE_SLOTS = new EntryBuilder<Boolean>(Boolean.class, "preserve_slots").def(true)
            .description(Language.PRESERVE_SLOTS_DESC).stringSerializer().build();

    ConfigEntry<Boolean> INDESTRUCTIBLE = new EntryBuilder<Boolean>(Boolean.class, "indestructible").def(true)
            .description(Language.INDESTRUCTIBLE_DESC).stringSerializer().build();

    ConfigEntry<Integer> PLAYER_LIMIT = new EntryBuilder<Integer>(Integer.class, "player_limit").def(-1)
            .description(Language.PLAYER_LIMIT_DESC).stringSerializer().build();

    ConfigEntry<Boolean> UNIQUE = new EntryBuilder<Boolean>(Boolean.class, "unique").def(true)
            .description(Language.UNIQUE_DESC).stringSerializer().build();

    ConfigEntry<Boolean> REDSTONE = new EntryBuilder<Boolean>(Boolean.class, "redstone").def(false)
            .description(Language.REDSTONE_DESC).stringSerializer().build();

    ConfigEntry<Boolean> ACCEPT_POLL = new EntryBuilder<Boolean>(Boolean.class, "accept_poll").def(false)
            .description(Language.ACCEPT_POLL_DESC).stringSerializer().build();

    ConfigEntry<Integer> PERIOD = new EntryBuilder<Integer>(Integer.class, "period").def(900)
            .description(Language.PERIOD_DESC).stringSerializer().build();

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
            }).description(Language.PERIOD_MODE_DESC).build();

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
            }).description(Language.RESTOCK_MODE_DESC).build();

    ConfigEntry<String> LOOT_TABLE = new EntryBuilder<String>(String.class, "loot_table").def("")
            .description(Language.LOOT_TABLE_DESC).build();

    ConfigEntry<String> GLOBAL_MESSAGE = new EntryBuilder<String>(String.class, "global_message").def("")
            .description(Language.GLOBAL_MESSAGE_DESC).build();
}
