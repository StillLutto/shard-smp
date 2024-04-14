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

class ItemManager(val shardSMP: ShardSMP) {

    private var itemList: MutableList<CustomItem> = mutableListOf()

    init {
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
            listOf(MiniMessage.miniMessage().deserialize("<gold>[Shift + Right Click]").decoration(TextDecoration.ITALIC, false)),
            7
        )
        createItem(
            "ender_bow",
            Material.BOW,
            MiniMessage.miniMessage().deserialize("<gradient:#8e1daa:#4b14aa>Ender Bow").decoration(TextDecoration.ITALIC, false),
            listOf(MiniMessage.miniMessage().deserialize("<gold>[Shift + Left Click]").decoration(TextDecoration.ITALIC, false)),
            3
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
            listOf(MiniMessage.miniMessage().deserialize("<gold>[Shift + Right Click]").decoration(TextDecoration.ITALIC, false)),
            6
        )
        createItem(
            "poseidon_trident",
            Material.TRIDENT,
            MiniMessage.miniMessage().deserialize("<gradient:#1616aa:#3646ff>Poseidon's Trident").decoration(TextDecoration.ITALIC, false),
            listOf(MiniMessage.miniMessage().deserialize("<gold>[Shift + Left Click]").decoration(TextDecoration.ITALIC, false)),
            11
        )
        createItem(
            "pyro_sword",
            Material.DIAMOND_SWORD,
            MiniMessage.miniMessage().deserialize("<gradient:#ff8b0f:#ffd60a>Pyro Sword").decoration(TextDecoration.ITALIC, false),
            listOf(MiniMessage.miniMessage().deserialize("<gold>[Shift + Right Click]").decoration(TextDecoration.ITALIC, false)),
            4
        )
        createItem(
            "tank_shield",
            Material.SHIELD,
            MiniMessage.miniMessage().deserialize("<gradient:#3d2216:#633723>Tank Shield").decoration(TextDecoration.ITALIC, false),
            listOf(MiniMessage.miniMessage().deserialize("<gold>[Shift + Left Click]").decoration(TextDecoration.ITALIC, false)),
            8
        )
        createItem(
            "titans_edge",
            Material.DIAMOND_SWORD,
            MiniMessage.miniMessage().deserialize("<gradient:#aa0000:#ff5555>Titans Edge").decoration(TextDecoration.ITALIC, false),
            listOf(MiniMessage.miniMessage().deserialize("<gold>[Shift + Right Click]").decoration(TextDecoration.ITALIC, false)),
            5
        )
        createItem(
            "vanish_blade",
            Material.DIAMOND_SWORD,
            MiniMessage.miniMessage().deserialize("<gradient:#919191:#c2c2c2>Vanish Blade").decoration(TextDecoration.ITALIC, false),
            listOf(MiniMessage.miniMessage().deserialize("<gold>[Shift + Right Click]").decoration(TextDecoration.ITALIC, false)),
            10
        )
    }

    private fun createItem(
        itemId: String,
        material: Material,
        itemDisplayName: Component,
        lore: List<Component>,
        customModelData: Int = -1
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
    }

    fun getItem(id: String): CustomItem? {
        for (customItem in itemList) {
            if (customItem.getId() == id) return customItem
        }
        return null
    }

    fun getItemList(): MutableList<CustomItem> = itemList

}