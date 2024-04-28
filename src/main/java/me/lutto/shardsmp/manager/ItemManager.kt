package me.lutto.shardsmp.manager

import com.google.common.cache.Cache
import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomCooldownItem
import me.lutto.shardsmp.items.CustomItem
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType
import java.util.*

class ItemManager(private val shardSMP: ShardSMP) {

    private var itemList: MutableList<CustomItem> = mutableListOf()

    private val itemCooldown: MutableMap<String, Cache<UUID, Long>> = mutableMapOf()

    fun registerItem(customItem: CustomItem) {
        val item = customItem.getItemStack()
        val itemMeta = item.itemMeta
        itemMeta.persistentDataContainer[NamespacedKey(shardSMP, "custom_item"), PersistentDataType.STRING] = customItem.getId()
        item.setItemMeta(itemMeta)
        customItem.setItemStack(item)

        itemList.add(customItem)
    }

    fun getItem(id: String): CustomItem? {
        for (customItem in itemList) {
            if (customItem.getId() == id) return customItem
        }
        return null
    }

    fun getCooldownItem(id: String): CustomCooldownItem? {
        for (customItem in itemList) {
            if (customItem !is CustomCooldownItem) return null
            if (customItem.getId() == id) return customItem
        }
        return null
    }

    fun setItemCooldown(id: String, cache: Cache<UUID, Long>) {
        itemCooldown[id] = cache
    }

    fun resetItemCooldown(id: String, uuid: UUID) {
        (itemCooldown[id] ?: return).invalidate(uuid)
        (shardSMP.itemManager.getCooldownItem(id) ?: return).setIsActivated(false)
    }

    fun getItemList(): MutableList<CustomItem> = itemList
    fun getItemCooldown(): Map<String, Cache<UUID, Long>> = itemCooldown

}