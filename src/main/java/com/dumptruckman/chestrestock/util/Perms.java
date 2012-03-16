package com.dumptruckman.chestrestock.util;

import com.dumptruckman.minecraft.pluginbase.permission.Perm;

public class Perms {
    
    public static final Perm CAN_CREATE = new Perm.Builder("create")
            .desc("Allows creating of restockable chests.").build();

    public static final Perm CAN_EDIT = new Perm.Builder("edit")
            .desc("Allows editing of restockable chests.").build();
}
