package com.dumptruckman.chestrestock.config

import com.dumptruckman.bukkit.configuration.hocon.HoconConfiguration
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class Konfig(private val config: HoconConfiguration, private val parent: Konfig? = null, private val node: String = "") {

    constructor (parent: Konfig, node: String) :
            this(parent.config,
                    parent,
                    if (parent.node.isEmpty())
                        node
                    else
                        "${parent.node}${parent.config.options().pathSeparator()}$node")

    private val konfigValueProperties: Collection<KProperty1<*, *>>
        get() = Konfig::class.memberProperties.filter { it.getDelegate(this) is KonfigValue<*> }

    var someValue: String? by KonfigValue("my default")

    private fun getPath(property: KProperty<*>): String =
        if (node.isEmpty()) {
            property.name
        } else {
            "$node${config.options().pathSeparator()}${property.name}"
        }

    class KonfigValue<T>(val defaultValue: T? = null) {

        operator fun provideDelegate(konfig: Konfig, property: KProperty<*>): KonfigValueDelegate<T> {
            if (defaultValue != null) {
                konfig.config.addDefault(konfig.getPath(property), defaultValue)
            }
            return KonfigValueDelegate()
        }
    }

    class KonfigValueDelegate<T>() {


        operator fun getValue(konfig: Konfig, property: KProperty<*>): T? {
            return konfig.config.get(konfig.getPath(property)) as T
        }

        operator fun setValue(konfig: Konfig, property: KProperty<*>, value: T) {
            konfig.config.set(konfig.getPath(property), value)
        }
    }
}