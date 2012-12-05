package com.dumptruckman.chestrestock.util;

import com.dumptruckman.minecraft.pluginbase.permission.BukkitPerm;
import com.dumptruckman.minecraft.pluginbase.permission.BukkitPermFactory;

public class Perms {

    public static final BukkitPerm CMD_CREATE = BukkitPermFactory.newBukkitPerm("cmd.create").usePluginName().commandPermission().build();
    public static final BukkitPerm CMD_CHECK = BukkitPermFactory.newBukkitPerm("cmd.check").usePluginName().commandPermission().build();
    public static final BukkitPerm CMD_UPDATE = BukkitPermFactory.newBukkitPerm("cmd.update").usePluginName().commandPermission().build();
    public static final BukkitPerm CMD_SET = BukkitPermFactory.newBukkitPerm("cmd.set").usePluginName().commandPermission().build();
    public static final BukkitPerm CMD_DEFAULT = BukkitPermFactory.newBukkitPerm("cmd.default").usePluginName().commandPermission().build();
    public static final BukkitPerm CMD_DEFAULTS =BukkitPermFactory.newBukkitPerm("cmd.defaults").usePluginName().commandPermission().build();
    public static final BukkitPerm CMD_RESTOCKALL = BukkitPermFactory.newBukkitPerm("cmd.restockall").usePluginName().commandPermission().build();
    public static final BukkitPerm CMD_CREATEALL = BukkitPermFactory.newBukkitPerm("cmd.createall").usePluginName().commandPermission().build();
    public static final BukkitPerm CMD_RESTOCK = BukkitPermFactory.newBukkitPerm("cmd.restock").usePluginName().commandPermission().build();
    public static final BukkitPerm CMD_DISABLE = BukkitPermFactory.newBukkitPerm("cmd.disable").usePluginName().commandPermission().build();

    public static final BukkitPerm CAN_BREAK_ANY = BukkitPermFactory.newBukkitPerm("break.*").usePluginName().addToAll().build();
    public static final BukkitPerm CAN_BREAK = BukkitPermFactory.newBukkitPerm("break").usePluginName().parent(CAN_BREAK_ANY).build();

    public static final BukkitPerm BYPASS_LOOT_LIMIT_ANY = BukkitPermFactory.newBukkitPerm("bypass.lootlimit.*").usePluginName().addToAll().build();
    public static final BukkitPerm BYPASS_LOOT_LIMIT = BukkitPermFactory.newBukkitPerm("bypass.lootlimit").usePluginName().parent(BYPASS_LOOT_LIMIT_ANY).build();
}
