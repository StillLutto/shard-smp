package me.lutto.shardsmp.items

import org.bukkit.event.Event

interface Upgradable {
    fun ability(event: Event)
    fun upgradedAbility(event: Event)
}