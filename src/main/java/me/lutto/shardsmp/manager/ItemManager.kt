package me.lutto.shardsmp.manager

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.instance.CustomItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

class ItemManager(private val shardSMP: ShardSMP) {

    private var itemList: MutableList<CustomItem> = mutableListOf()

    private var cooldownMap: MutableMap<String, Pair<Boolean, Int>> = mutableMapOf()

    private val miniMessage: MiniMessage = MiniMessage.miniMessage()

    init {
        val leftClickLore = MiniMessage.miniMessage().deserialize("<gold>[Shift + Left Click]").decoration(TextDecoration.ITALIC, false)
        val rightClickLore = MiniMessage.miniMessage().deserialize("<gold>[Shift + Right Click]").decoration(TextDecoration.ITALIC, false)
        createItems(
            CustomItem.Builder()
                .id("shard")
                .item(ItemStack(Material.ECHO_SHARD))
                .displayName(miniMessage.deserialize("<dark_purple>Shard").decoration(TextDecoration.ITALIC, false))
                .customModelData(431)
                .build(),
            CustomItem.Builder()
                .id("life")
                .item(ItemStack(Material.RABBIT_FOOT))
                .displayName(miniMessage.deserialize("<color:#ff1c27>Life").decoration(TextDecoration.ITALIC, false))
                .customModelData(2)
                .build(),
            CustomItem.Builder()
                .id("earth_shatterer")
                .item(ItemStack(Material.NETHERITE_PICKAXE))
                .displayName(miniMessage.deserialize("<gradient:#57301f:#417837>Earth Shatterer").decoration(TextDecoration.ITALIC, false))
                .lore(listOf(leftClickLore))
                .customModelData(7)
                .rightClick(true)
                .cooldownTime(120)
                .useOnActivation(true)
                .build(),
            CustomItem.Builder()
                .id("ender_bow")
                .item(ItemStack(Material.BOW))
                .displayName(miniMessage.deserialize("<gradient:#8e1daa:#4b14aa>Ender Bow").decoration(TextDecoration.ITALIC, false))
                .lore(listOf(leftClickLore))
                .customModelData(3)
                .rightClick(false)
                .cooldownTime(30)
                .useOnActivation(false)
                .build(),
            CustomItem.Builder()
                .id("lifestealer")
                .item(ItemStack(Material.DIAMOND_SWORD))
                .displayName(miniMessage.deserialize("<gradient:#57301f:#417837><gradient:#aa0000:#ff2119>Lifestealer").decoration(TextDecoration.ITALIC, false))
                .customModelData(9)
                .build(),
            CustomItem.Builder()
                .id("mjolnir")
                .item(ItemStack(Material.NETHERITE_AXE))
                .displayName(miniMessage.deserialize("<gradient:#3a4261:#7277a6>Mjölnir").decoration(TextDecoration.ITALIC, false))
                .lore(listOf(rightClickLore))
                .customModelData(6)
                .rightClick(true)
                .cooldownTime(120)
                .useOnActivation(true)
                .build(),
            CustomItem.Builder()
                .id("poseidon_trident")
                .item(ItemStack(Material.TRIDENT))
                .displayName(miniMessage.deserialize("<gradient:#1616aa:#3646ff>Poseidon's Trident").decoration(TextDecoration.ITALIC, false))
                .lore(listOf(leftClickLore))
                .customModelData(11)
                .rightClick(false)
                .cooldownTime(180)
                .useOnActivation(true)
                .build(),
            CustomItem.Builder()
                .id("pyro_sword")
                .item(ItemStack(Material.DIAMOND_SWORD))
                .displayName(miniMessage.deserialize("<gradient:#ff8b0f:#ffd60a>Pyro Sword").decoration(TextDecoration.ITALIC, false))
                .lore(listOf(rightClickLore))
                .customModelData(4)
                .rightClick(true)
                .cooldownTime(240)
                .useOnActivation(false)
                .build(),
            CustomItem.Builder()
                .id("tank_shield")
                .item(ItemStack(Material.SHIELD))
                .displayName(miniMessage.deserialize("<gradient:#3d2216:#633723>Tank Shield").decoration(TextDecoration.ITALIC, false))
                .lore(listOf(leftClickLore))
                .customModelData(8)
                .rightClick(false)
                .cooldownTime(120)
                .useOnActivation(true)
                .build(),
            CustomItem.Builder()
                .id("titans_edge")
                .item(ItemStack(Material.DIAMOND_SWORD))
                .displayName(miniMessage.deserialize("<gradient:#aa0000:#ff5555>Titans Edge").decoration(TextDecoration.ITALIC, false))
                .lore(listOf(rightClickLore))
                .customModelData(5)
                .rightClick(true)
                .cooldownTime(120)
                .useOnActivation(true)
                .build(),
            CustomItem.Builder()
                .id("vanish_blade")
                .item(ItemStack(Material.DIAMOND_SWORD))
                .displayName(miniMessage.deserialize("<gradient:#919191:#c2c2c2>Vanish Blade").decoration(TextDecoration.ITALIC, false))
                .lore(listOf(rightClickLore))
                .customModelData(10)
                .rightClick(true)
                .cooldownTime(150)
                .useOnActivation(true)
                .build()
        )
    }

    private fun createItems(vararg customItems: CustomItem) {
        for (customItem in customItems) {
            itemList.add(customItem)

            val item = customItem.getItemStack()
            val customItemMeta = item.itemMeta
            val key = NamespacedKey(shardSMP, "custom_item")
            customItemMeta.persistentDataContainer[key, PersistentDataType.STRING] = customItem.getId()
            item.setItemMeta(customItemMeta)
            customItem.setItemStack(item)
        }
    }

    fun getItem(id: String): CustomItem? {
        for (customItem in itemList) {
            if (customItem.getId() == id) return customItem
        }
        return null
    }

    fun getItemList(): MutableList<CustomItem> = itemList

}