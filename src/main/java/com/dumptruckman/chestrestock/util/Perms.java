package com.dumptruckman.chestrestock.util;

import com.dumptruckman.minecraft.pluginbase.permission.Perm;

public class Perms {
    
    private static final String BASE_PERM = "chestrestock.";
    
    public static final Perm CMD_CREATE = new Perm.Builder(BASE_PERM + "cmd.create").build();
    public static final Perm CMD_CHECK = new Perm.Builder(BASE_PERM + "cmd.check").build();
    public static final Perm CMD_UPDATE = new Perm.Builder(BASE_PERM + "cmd.update").build();
    public static final Perm CMD_SET = new Perm.Builder(BASE_PERM + "cmd.set").build();
    public static final Perm CMD_RESTOCK = new Perm.Builder(BASE_PERM + "cmd.restock").build();
    public static final Perm CMD_DISABLE = new Perm.Builder(BASE_PERM + "cmd.disable").build();

    public static final Perm CAN_BREAK_ANY = new Perm.Builder(BASE_PERM + "break.*").build();
    public static final Perm CAN_BREAK = new Perm.Builder(BASE_PERM + "break").parent(CAN_BREAK_ANY.getName(), true).build();

    public static final Perm BYPASS_LOOT_LIMIT_ANY = new Perm.Builder(BASE_PERM + "bypass.lootlimit.*").build();
    public static final Perm BYPASS_LOOT_LIMIT = new Perm.Builder(BASE_PERM + "bypass.lootlimit").parent(BYPASS_LOOT_LIMIT_ANY.getName(), true).build();
}
