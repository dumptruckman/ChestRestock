package com.dumptruckman.chestrestock

import com.dumptruckman.chestrestock.i18n.MutableMessageSource
import org.bukkit.plugin.java.JavaPlugin
import java.util.Locale
import java.util.ResourceBundle

class ChestRestock : JavaPlugin() {

    companion object {
        internal val messageSource = object : MutableMessageSource(
                ResourceBundle.getBundle("Messages", Locale.ENGLISH)) {
            override fun getString(key: CharSequence): String = resourceBundle.getString(key.toString())
        }
    }
}