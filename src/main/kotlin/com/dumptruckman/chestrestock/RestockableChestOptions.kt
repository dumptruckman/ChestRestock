package com.dumptruckman.chestrestock

import com.dumptruckman.chestrestock.config.Konfig
import org.bukkit.configuration.Configuration

open class RestockableChestOptions(config: Configuration, defaults: RestockableChestDefaults?) :
        Konfig(config, defaults) {

    var enabled: Boolean by KonfigValue(true).withComments(
            "When set to true, the chest functions as a chest for this plugin. "
                    + "Very rarely would you manually set this to false.",
            "When set to false, the chest will behave exactly as a normal chest!")

    var name: String by KonfigValue("").withComments(
            "The default name for new chests. This is used for permissions if not left blank (per chest).")

    var preserve_slots: Boolean by KonfigValue(true).withComments(
            "When set to true, chests will restock items in the same slots they were set up with.")

    var indestructible: Boolean by KonfigValue(true).withComments(
            "When set to true, chests will only be able to broken by players with the correct permission.")

    var player_limit: Int by KonfigValue(-1).withComments(
            "This is the max number of times a chest will restock for each player. "
                    + "Negative values indicate unlimited restocks.")

    var unique: Boolean by KonfigValue(true).withComments(
            "When set to true, chests will give each player a unique version of the chest to prevent loot theft.",
            "These inventories will NOT persist through server restarts/reloads.",
            "It's possible these inventories will be wiped when the chest is not in use for a while. "
                    + "This is dependent on your JVM Garbage Collection settings.")

    var redstone: Boolean by KonfigValue(false).withComments(
            "When set to true, chests will restock when they receive redstone power.")

    var accept_poll: Boolean by KonfigValue(false).withComments(
            "When set to true, chests will use the restock task poll to determine if the chest should update.",
            "This may not work as expected with chests that are also 'unique'")

    var period: Int by KonfigValue(900).withComments(
            "This is the amount of time a chest requires before it will restock it's inventory. (In seconds)",
            "You may set this to 0 to disable timed restocking.")

    var period_mode: String by KonfigValue(PERIOD_MODE_PLAYER).withComments(
            "Possible options are 'player' and 'fixed'",
            "'player' mode means restock timing will be based on when a player last looted the chest.",
            "'fixed' mode means restock timing is based on when a chest is set up.")
            .withValidator {
                val value = it.toString()
                value.equals(PERIOD_MODE_PLAYER, ignoreCase = true)
                        || value.equals(PERIOD_MODE_FIXED, ignoreCase = true)
            }

    var restock_mode: String by KonfigValue(RESTOCK_MODE_REPLACE).withComments(
            "Possible options are 'add' and 'replace'",
            "'add' mode means restocks will ADD items to the chest.",
            "'replace' mode means restocks will REPLACE items in the chest.")
            .withValidator {
                val value = it.toString()
                value.equals(RESTOCK_MODE_ADD, ignoreCase = true)
                        || value.equals(RESTOCK_MODE_REPLACE, ignoreCase = true)
            }

    var global_message: String by KonfigValue("").withComments(
            "The default global message for new chests. "
                    + "This will be broadcast when the chest restocks if not left blank. (per chest)")

    var only_restock_empty: Boolean by KonfigValue(false).withComments(
            "When set to true, only empty chests will be restocked.")

    companion object {
        const val PERIOD_MODE_PLAYER = "player"
        const val PERIOD_MODE_FIXED = "fixed"

        const val RESTOCK_MODE_REPLACE = "replace"
        const val RESTOCK_MODE_ADD = "add"
    }
}