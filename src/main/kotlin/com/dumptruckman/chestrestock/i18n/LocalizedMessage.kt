package com.dumptruckman.chestrestock.i18n

import org.bukkit.ChatColor
import java.text.MessageFormat

class LocalizedMessage(private val messageSource: MessageSource, val key: CharSequence) {

    operator fun invoke(vararg args: Any?): String {
        val message = ChatColor.translateAlternateColorCodes('&', messageSource.getString(key))
        return MessageFormat.format(message, *args)
    }
}