package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.CRPlayer;

public final class Players {
    
    private Players() {
        throw new AssertionError();
    }
    
    public static CRPlayer newCRPlayer() {
        return new DefaultCRPlayer(0, 0L);
    }

    public static CRPlayer newCRPlayer(int lootCount, long lastRestock) {
        return new DefaultCRPlayer(lootCount, lastRestock);
    }
}
