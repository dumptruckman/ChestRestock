/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dumptruckman.dchest;

import java.util.TimerTask;

/**
 *
 * @author Billing Manager
 */
public class SaveTimer extends TimerTask {

    DChest plugin;

    public SaveTimer(DChest plugin) {
        super();
        this.plugin = plugin;
    }

    public void run() {
        plugin.saveConfigs();
    }
}
