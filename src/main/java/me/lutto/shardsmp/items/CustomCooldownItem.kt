package me.lutto.shardsmp.items

import me.lutto.shardsmp.ShardSMP
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

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

    init {
        val itemStack = item
        val itemMeta = itemStack.itemMeta
        var loreList: MutableList<Component> = mutableListOf(ShardSMP.plugin.miniMessage.deserialize("<gold>[ѕʜɪꜰᴛ + ʟᴇꜰᴛ ᴄʟɪᴄᴋ]").decoration(TextDecoration.ITALIC, false))
        if (rightClick) {
            loreList = mutableListOf(ShardSMP.plugin.miniMessage.deserialize("<gold>[ѕʜɪꜰᴛ + ʀɪɢʜᴛ ᴄʟɪᴄᴋ]").decoration(TextDecoration.ITALIC, false))
        }
        loreList.addAll(lore)
        itemMeta.lore(loreList)
        itemStack.setItemMeta(itemMeta)
        this.item = itemStack
    }

    fun isRightClick(): Boolean = rightClick
    fun getCooldownTime(): Int = cooldownTime
    fun isUsedOnActivation(): Boolean = useOnActivation

}
