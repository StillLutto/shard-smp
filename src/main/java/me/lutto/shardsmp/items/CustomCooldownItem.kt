package me.lutto.shardsmp.items

import net.kyori.adventure.text.Component
import org.bukkit.Material

open class CustomCooldownItem(
    id: String,
    material: Material,
    displayName: Component,
    lore: List<Component>,
    customModelData: Int,
    private val rightClick: Boolean,
    private val cooldownTime: Int,
    private val useOnActivation: Boolean
) : CustomItem(
    id, material, displayName, lore, customModelData
) {

    fun isRightClick(): Boolean = rightClick
    fun getCooldownTime(): Int = cooldownTime
    fun isUsedOnActivation(): Boolean = useOnActivation

}
