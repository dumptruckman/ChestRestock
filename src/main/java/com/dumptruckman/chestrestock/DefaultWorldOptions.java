package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.util.Language;
import pluginbase.config.SerializationRegistrar;
import pluginbase.config.annotation.Comment;
import pluginbase.config.annotation.Description;

public class DefaultWorldOptions extends DefaultChestOptions {

    static {
        SerializationRegistrar.registerClass(OtherDefaults.class);
    }

    public static class OtherDefaults {
        @Description(Language.LKEY_AUTO_CREATE_DESC)
        @Comment({"This will automatically initialize any chest not already managed by ChestRestock with these defaults.",
                "Essentially the same thing as using \"/cr create\" on every chest encountered"})
        private boolean autoCreate = false;

        @Description(Language.LKEY_AUTO_CREATE_NEW_DESC)
        @Comment({"This will automatically initialize any chest not already managed by ChestRestock with these defaults.",
                "Essentially the same thing as using \"/cr create\" on every chest encountered",
                "Requires auto_create to be enabled!",
                "This basically determines if NEWLY PLACED chests will be enabled or disabled.  When set to false, all NEWLY PLACED chests will act like NORMAL chests."})
        private boolean autoCreateNew = false;

        @Description(Language.LKEY_EMPTY_LOOT_TABLE_DESC)
        @Comment({"This is the loot table that will be used when intializing a restock chest that is empty when created."})
        private String emptyLootTable = "";
    }

    @Comment({"These are options that pertain to chests but are not properties of individual chests."})
    private OtherDefaults other = new OtherDefaults();

    public
}
