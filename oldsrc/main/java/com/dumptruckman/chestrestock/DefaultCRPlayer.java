package com.dumptruckman.chestrestock;

import com.dumptruckman.chestrestock.api.CRPlayer;

class DefaultCRPlayer implements CRPlayer {
    
    private int lootCount;
    private long lastRestockTime;

    DefaultCRPlayer(int lootCount, long lastRestockTime) {
        this.lootCount = lootCount;
        this.lastRestockTime = lastRestockTime;
    }

    @Override
    public int getLootCount() {
        return lootCount;
    }

    @Override
    public long getLastRestockTime() {
        return lastRestockTime;
    }

    @Override
    public void setLootCount(int count) {
        this.lootCount = count;
    }

    @Override
    public void setLastRestockTime(long time) {
        this.lastRestockTime = time;
    }
}
