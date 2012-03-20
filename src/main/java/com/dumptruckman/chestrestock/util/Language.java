package com.dumptruckman.chestrestock.util;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

public class Language {
    
    public static final Message IN_GAME_ONLY = new Message("cmd.in_game_only",
            "You may only use this command in game!");
    public static final Message TARGETING = new Message("cmd.targeting",
            "You must be targeting a chest to use this command!");
    public static final Message CMD_NOT_RCHEST = new Message("cmd.not_restock_chest",
            "The targeted chest is not managed by ChestRestock.");


    public static final Message CMD_CREATE_NAME = new Message("cmd.create.name",
            "Initiates targeted chest for restocking.");
    public static final Message CMD_CREATE_ALREADY_MADE = new Message("cmd.create.already_made",
            "This chest is already configured! Make use of the other sub-commands to change the options for this chest.",
            "To update the chest contents, use the update sub-command.");
    public static final Message CMD_CREATE_SUCCESS = new Message("cmd.create.success",
            "This chest will restock with the items currently inside of it, every %1 seconds. (default)");
    public static final Message CMD_CREATE_ERROR = new Message("cmd.create.error",
            "There was an internal error while creating a new restock chest.  Please refer to server.log");

    public static final Message CMD_CHECK_NAME = new Message("cmd.check.name",
            "Checks a chest and gives info relating to ChestRestock");
    public static final Message CMD_CHECK_SUCCESS = new Message("cmd.check.success",
            "Chest restocks every %1 second(s)",
            "Chest restock mode: %2",
            "Chest period mode: %3",
            "Chest will preserve slots: %4",
            "Chest is indestructible: %5",
            "Chest player limit: %6",
            "Chest has unique inventory: %7");

    public static final Message CMD_UPDATE_NAME = new Message("cmd.update.name",
            "Updates the restocking contents of a chest.");
    public static final Message CMD_UPDATE_SUCCESS = new Message("cmd.update.success",
            "Chest will now restock with the items currently inside of it.");
}
