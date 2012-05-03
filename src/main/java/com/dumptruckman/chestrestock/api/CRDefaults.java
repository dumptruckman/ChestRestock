package com.dumptruckman.chestrestock.api;

import com.dumptruckman.chestrestock.util.Language;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.EntryBuilder;

public interface CRDefaults extends CRChestOptions {

    ConfigEntry<Boolean> AUTO_CREATE = new EntryBuilder<Boolean>(Boolean.class, "other.auto_create").def(false)
            .description(Language.AUTO_CREATE_DESC)
            .comment("# This will automatically initialize any chest not already managed by ChestRestock with these defaults.")
            .comment("# Essentially the same thing as using \"/cr create\" on every chest encountered")
            .build();
}
