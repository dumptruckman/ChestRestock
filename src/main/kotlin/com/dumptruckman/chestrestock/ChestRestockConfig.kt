package com.dumptruckman.chestrestock

import com.dumptruckman.bukkit.configuration.hocon.HoconConfiguration
import com.dumptruckman.chestrestock.config.Konfig

class ChestRestockConfig(config: HoconConfiguration) : Konfig(config) {

    val settings: Settings = Settings()

    // TODO cleanup magic values

    inner class Settings : Konfig(this@ChestRestockConfig, "settings") {
        var restock_task_interval: Int by KonfigValue(60)
                .withComments("This is the interval (in seconds) at which a timer task will poll all 'accept_poll' chests to see if it should update.",
                        "This could potentially affect performance if you have LOTS of chests set to accept polling",
                        "To disable this polling feature, set this to 0")

        var max_inventory_size: Int by KonfigValue(54)
                .withComments("# This is the maximum size of any inventory.  The default (54) is the size of a standard double chest.",
                        "# You should only adjust this value if you are using mods that allow larger inventories.")
                .withValidator {
                    var o = it
                    if (o is String) {
                        try {
                            o = o.toInt()
                        } catch (e: NumberFormatException) {
                        }
                    }
                    o is Int && o >= 54
                }
    }
}
