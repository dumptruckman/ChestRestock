package com.dumptruckman.chestrestock.util;

import com.dumptruckman.minecraft.pluginbase.permission.Perm;

public class Perms {
    
    private static final String BASE_PERM = "chestrestock.";
    
    public static final Perm CMD_CREATE = new Perm.Builder(BASE_PERM + "cmd.create").commandPermission().build();
    public static final Perm CMD_CHECK = new Perm.Builder(BASE_PERM + "cmd.check").commandPermission().build();
    public static final Perm CMD_UPDATE = new Perm.Builder(BASE_PERM + "cmd.update").commandPermission().build();
    public static final Perm CMD_SET = new Perm.Builder(BASE_PERM + "cmd.set").commandPermission().build();
    public static final Perm CMD_DEFAULT = new Perm.Builder(BASE_PERM + "cmd.default").commandPermission().build();
    public static final Perm CMD_DEFAULTS = new Perm.Builder(BASE_PERM + "cmd.defaults").commandPermission().build();
    public static final Perm CMD_RESTOCKALL = new Perm.Builder(BASE_PERM + "cmd.restockall").commandPermission().build();
    public static final Perm CMD_CREATEALL = new Perm.Builder(BASE_PERM + "cmd.createall").commandPermission().build();
    public static final Perm CMD_RESTOCK = new Perm.Builder(BASE_PERM + "cmd.restock").commandPermission().build();
    public static final Perm CMD_DISABLE = new Perm.Builder(BASE_PERM + "cmd.disable").commandPermission().build();

    public static final Perm CAN_BREAK_ANY = new Perm.Builder(BASE_PERM + "break.*").addToAll().build();
    public static final Perm CAN_BREAK = new Perm.Builder(BASE_PERM + "break").parent(CAN_BREAK_ANY.getName(), true).build();

    public static final Perm BYPASS_LOOT_LIMIT_ANY = new Perm.Builder(BASE_PERM + "bypass.lootlimit.*").addToAll().build();
    public static final Perm BYPASS_LOOT_LIMIT = new Perm.Builder(BASE_PERM + "bypass.lootlimit").parent(BYPASS_LOOT_LIMIT_ANY.getName(), true).build();
}
