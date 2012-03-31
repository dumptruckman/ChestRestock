package com.dumptruckman.chestrestock.api;

public interface CRPlayer {
    
    int getLootCount();

    long getLastRestockTime();

    void setLootCount(int count);

    void setLastRestockTime(long time);
}
