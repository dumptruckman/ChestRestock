package com.dumptruckman.chestrestock.i18n

import org.bukkit.ChatColor

class LocalizedMessage(private val messageSource: MessageSource, val key: CharSequence) {

    operator fun invoke(vararg args: Any?): String {
        var message = ChatColor.translateAlternateColorCodes('&', messageSource.getString(key))
        args.forEachIndexed({
            i, arg -> message = message.replace("{$i}", arg.toString())
        })
        return message
    }
}