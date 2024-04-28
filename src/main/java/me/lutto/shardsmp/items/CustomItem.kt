package me.lutto.shardsmp.items

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class CustomItem(
    private val id: String,
    material: Material,
    displayName: Component,
    lore: List<Component>,
    customModelData: Int
) {

    private var item: ItemStack

    init {
        val itemStack = ItemStack(material)
        val itemMeta = itemStack.itemMeta
        itemMeta.displayName(displayName)
        itemMeta.lore(lore)
        if(customModelData == -1) itemMeta.setCustomModelData(null) else itemMeta.setCustomModelData(customModelData)
        itemStack.setItemMeta(itemMeta)
        this.item = itemStack
    }

    fun setItemStack(itemStack: ItemStack) {
        item = itemStack
    }

    fun getId(): String = id
    fun getItemStack(): ItemStack = item

}
