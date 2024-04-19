package me.lutto.shardsmp.instance

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class CustomItem(
    private val id: String,
    private var item: ItemStack,
    private val rightClick: Boolean?,
    private val cooldownTime: Long?,
    private val useOnActivation: Boolean?
) {

    init {
        val itemMeta = item.itemMeta
        itemMeta.displayName()
    }

    data class Builder(
        var id: String = "",
        var item: ItemStack = ItemStack(Material.DIAMOND_SWORD),
        var lore: List<Component> = listOf(Component.text("")),
        var customModelData: Int = -1,
        var rightClick: Boolean? = null,
        var cooldownTime: Long? = null,
        var useOnActivation: Boolean? = null) {

        fun id(id: String) = apply { this.id = id }
        fun item(item: ItemStack) = apply { this.item = item }
        fun displayName(displayName: Component) = apply {
            val itemMeta = item.itemMeta
            itemMeta.displayName(displayName)
            item.itemMeta = itemMeta
        }
        fun lore(lore: List<Component>) = apply {
            val itemMeta = item.itemMeta
            itemMeta.lore(lore)
            item.itemMeta = itemMeta
        }
        fun customModelData(customModelData: Int) = apply {
            val itemMeta = item.itemMeta
            itemMeta.setCustomModelData(customModelData)
            item.itemMeta = itemMeta
        }
        fun rightClick(rightClick: Boolean) = apply { this.rightClick = rightClick }
        fun cooldownTime(cooldownTime: Long) = apply { this.cooldownTime = cooldownTime }
        fun useOnActivation(useOnActivation: Boolean) = apply { this.useOnActivation = useOnActivation }

        fun build() = CustomItem(
                id = id,
                item = item,
                rightClick = rightClick,
                cooldownTime = cooldownTime,
                useOnActivation = useOnActivation
            )
    }

    fun setItemStack(itemStack: ItemStack) {
        item = itemStack
    }

    fun getId(): String = id
    fun getItemStack(): ItemStack = item
    fun isRightClick(): Boolean? = rightClick
    fun getCooldownTime(): Long? = cooldownTime
    fun isUsedOnActivation(): Boolean? = useOnActivation
}
