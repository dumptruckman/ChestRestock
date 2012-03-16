package com.dumptruckman.chestrestock;

import com.dumptruckman.actionmenu2.api.Menu;
import com.dumptruckman.actionmenu2.api.MenuView;
import com.dumptruckman.actionmenu2.api.event.MenuEvent;
import com.dumptruckman.actionmenu2.api.event.ModelChangeEvent;
import com.dumptruckman.actionmenu2.impl.Views;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

class ChestSignView implements MenuView {
    
    private MenuView actualView;
    private Sign sign;
    private Plugin plugin;
    private CleanupTask cleanupTask;

    ChestSignView(Plugin plugin, Sign sign) {
        this.actualView = Views.newView(plugin, sign);
        this.sign = sign;
        this.plugin = plugin;
        this.cleanupTask = new CleanupTask();
        cleanupTask.start();
    }

    @Override
    public void showMenu(Menu menu, Player player) {
        actualView.showMenu(menu, player);
    }

    @Override
    public void update(Plugin plugin, Menu menu, Player player) {
        cleanupTask.update(menu);
        actualView.update(plugin, menu, player);
    }

    @Override
    public void onContentsAdd(MenuEvent menuEvent) {
        actualView.onContentsAdd(menuEvent);
    }

    @Override
    public void onContentsRemove(MenuEvent menuEvent) {
        actualView.onContentsRemove(menuEvent);
    }

    @Override
    public void onContentsChange(MenuEvent menuEvent) {
        actualView.onContentsChange(menuEvent);
    }

    @Override
    public void onSelectionChange(MenuEvent menuEvent) {
        actualView.onSelectionChange(menuEvent);
    }

    @Override
    public void onModelChange(ModelChangeEvent modelChangeEvent) {
        actualView.onModelChange(modelChangeEvent);
    }

    private class CleanupTask implements Runnable {
        
        private static final int TICKS = 20;
        private static final int STALE_DURATION = 10;

        private int staleSeconds = 0;
        private int taskId = -1;
        private Menu menu = null;

        public void start() {
            if (this.taskId == -1) {
                schedule();
                this.staleSeconds = 0;
            }
        }

        public void stop() {
            Bukkit.getScheduler().cancelTask(this.taskId);
            this.taskId = -1;
            staleSeconds = 0;
            if (menu != null) {
                menu.getViews().remove(ChestSignView.this);
                ChestSignView.this.sign.getBlock().setType(Material.AIR);
            }
        }

        public void update(Menu menu) {
            this.menu = menu;
            this.staleSeconds = 0;
        }

        private void schedule() {
            taskId = Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(ChestSignView.this.plugin, this, TICKS);
        }

        @Override
        public void run() {
            if (staleSeconds < STALE_DURATION) {
                staleSeconds++;
                schedule();
            } else {
                stop();
            }
        }
    }
}
