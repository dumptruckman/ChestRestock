package com.dumptruckman.chestrestock.util;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

public class Language {

    public static final Message NO_CREATE_PERMISSION = new Message("permission.no_create",
            "You do not have permission to create a restock chest!");
    public static final Message NO_EDIT_PERMISSION = new Message("permission.no_edit",
            "You do not have permission to edit a restock chest!");

    public static final Message SIGN_NOT_ON_CHEST = new Message("sign.not_on_chest",
            "You must place this sign on a chest!");
}
