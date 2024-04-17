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

    init {
        val leftClickLore = MiniMessage.miniMessage().deserialize("<gold>[Shift + Left Click]").decoration(TextDecoration.ITALIC, false)
        val rightClickLore = MiniMessage.miniMessage().deserialize("<gold>[Shift + Right Click]").decoration(TextDecoration.ITALIC, false)
        createItem(
            "shard",
            Material.ECHO_SHARD,
            MiniMessage.miniMessage().deserialize("<dark_purple>Shard").decoration(TextDecoration.ITALIC, false),
            listOf(MiniMessage.miniMessage().deserialize("").decoration(TextDecoration.ITALIC, false)),
            1
        )
        createItem(
            "life",
            Material.RABBIT_FOOT,
            MiniMessage.miniMessage().deserialize("<color:#ff1c27>Life").decoration(TextDecoration.ITALIC, false),
            listOf(MiniMessage.miniMessage().deserialize("").decoration(TextDecoration.ITALIC, false)),
            2
        )
        createItem(
            "earth_shatterer",
            Material.NETHERITE_PICKAXE,
            MiniMessage.miniMessage().deserialize("<gradient:#57301f:#417837>Earth Shatterer").decoration(TextDecoration.ITALIC, false),
            listOf(rightClickLore),
            7,
            Pair(false, 120)
        )
        createItem(
            "ender_bow",
            Material.BOW,
            MiniMessage.miniMessage().deserialize("<gradient:#8e1daa:#4b14aa>Ender Bow").decoration(TextDecoration.ITALIC, false),
            listOf(leftClickLore),
            3,
            Pair(true, 30)
        )
        createItem(
            "lifestealer",
            Material.DIAMOND_SWORD,
            MiniMessage.miniMessage().deserialize("<gradient:#aa0000:#ff2119>Lifestealer").decoration(TextDecoration.ITALIC, false),
            listOf(MiniMessage.miniMessage().deserialize("").decoration(TextDecoration.ITALIC, false)),
            9
        )
        createItem(
            "mjolnir",
            Material.NETHERITE_AXE,
            MiniMessage.miniMessage().deserialize("<gradient:#3a4261:#7277a6>Mjölnir").decoration(TextDecoration.ITALIC, false),
            listOf(rightClickLore),
            6,
            Pair(false, 120)
        )
        createItem(
            "poseidon_trident",
            Material.TRIDENT,
            MiniMessage.miniMessage().deserialize("<gradient:#1616aa:#3646ff>Poseidon's Trident").decoration(TextDecoration.ITALIC, false),
            listOf(leftClickLore),
            11,
            Pair(true, 180)
        )
        createItem(
            "pyro_sword",
            Material.DIAMOND_SWORD,
            MiniMessage.miniMessage().deserialize("<gradient:#ff8b0f:#ffd60a>Pyro Sword").decoration(TextDecoration.ITALIC, false),
            listOf(rightClickLore),
            4,
            Pair(false, 240)
        )
        createItem(
            "tank_shield",
            Material.SHIELD,
            MiniMessage.miniMessage().deserialize("<gradient:#3d2216:#633723>Tank Shield").decoration(TextDecoration.ITALIC, false),
            listOf(leftClickLore),
            8,
            Pair(true, 120)
        )
        createItem(
            "titans_edge",
            Material.DIAMOND_SWORD,
            MiniMessage.miniMessage().deserialize("<gradient:#aa0000:#ff5555>Titans Edge").decoration(TextDecoration.ITALIC, false),
            listOf(rightClickLore),
            5,
            Pair(false, 120)
        )
        createItem(
            "vanish_blade",
            Material.DIAMOND_SWORD,
            MiniMessage.miniMessage().deserialize("<gradient:#919191:#c2c2c2>Vanish Blade").decoration(TextDecoration.ITALIC, false),
            listOf(rightClickLore),
            10,
            Pair(false, 150)
        )
    }

    private fun createItem(
        itemId: String,
        material: Material,
        itemDisplayName: Component,
        lore: List<Component>,
        customModelData: Int = -1,
        cooldown: Pair<Boolean, Int>? = null // true = Left Click, false = Right Click
    ) {
        val item = ItemStack(material, 1)
        val itemMeta = item.itemMeta

        val key = NamespacedKey(shardSMP, "custom_item")
        itemMeta.persistentDataContainer[key, PersistentDataType.STRING] = itemId

        itemMeta.displayName(itemDisplayName)
        itemMeta.lore(lore)
        itemMeta.setCustomModelData(customModelData)
        item.setItemMeta(itemMeta)

        val customItem = CustomItem(itemId, item)
        itemList.add(customItem)

        if (cooldown != null) {
            cooldownMap[itemId] = cooldown
        }
    }

    fun getItem(id: String): CustomItem? {
        for (customItem in itemList) {
            if (customItem.getId() == id) return customItem
        }
        return null
    }

    fun getCooldown(id: String): Pair<Boolean, Int>? {
        if (cooldownMap.containsKey(id)) {
            return cooldownMap[id]
        }
        return null
    }

    fun getItemList(): MutableList<CustomItem> = itemList

}