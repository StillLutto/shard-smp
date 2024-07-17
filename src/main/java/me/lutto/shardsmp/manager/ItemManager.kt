package me.lutto.shardsmp.manager

import com.google.common.cache.Cache
import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomCooldownItem
import me.lutto.shardsmp.items.CustomItem
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPlugin.getPlugin
import java.io.File
import java.io.IOException
import java.util.*

class ItemManager(private val shardSMP: ShardSMP) {

    private var itemList: MutableList<CustomItem> = mutableListOf()
    private val itemCooldown: MutableMap<String, Cache<UUID, Long>> = mutableMapOf()
    private val isActivated: MutableSet<UUID> = mutableSetOf()
    private val upgradedItems: MutableMap<UUID, Boolean> = mutableMapOf()

    private var upgradedItemsFile: File
    private var upgradedItemsConfig: YamlConfiguration

    init {
        if (!shardSMP.dataFolder.exists()) {
            shardSMP.dataFolder.mkdir()
        }

        upgradedItemsFile = File(shardSMP.dataFolder, "upgradedItems.yml")
        if (!upgradedItemsFile.exists()) {
            try {
                upgradedItemsFile.createNewFile()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }

        upgradedItemsConfig = YamlConfiguration.loadConfiguration(upgradedItemsFile)
    }

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
            if (customItem !is CustomCooldownItem) continue
            if (customItem.getId() == id) return customItem
        }
        return null
    }

    fun setItemCooldown(id: String, cache: Cache<UUID, Long>) {
        itemCooldown[id] = cache
    }

    fun resetItemCooldown(id: String, uuid: UUID) {
        (itemCooldown[id] ?: return).invalidate(uuid)
        setIsActivated(uuid, false)
    }

    fun setIsActivated(uuid: UUID, activated: Boolean) {
        if (activated) {
            isActivated.add(uuid)
        } else {
            isActivated.remove(uuid)
        }
    }

    fun isActivated(uuid: UUID): Boolean = isActivated.contains(uuid)

    fun setUpgraded(uuid: UUID, upgraded: Boolean) {
        upgradedItemsConfig[uuid.toString()] = upgraded
        try {
            upgradedItemsConfig.save(upgradedItemsFile)
        } catch (e: IOException) {
            throw java.lang.RuntimeException(e)
        }
        upgradedItems.containsKey(uuid)
    }

    fun isUpgraded(uuid: UUID): Boolean = upgradedItemsConfig.getBoolean(uuid.toString())

    fun getItemList(): MutableList<CustomItem> = itemList
    fun getItemCooldown(): Map<String, Cache<UUID, Long>> = itemCooldown

}

private val shardSMP: ShardSMP = getPlugin(ShardSMP::class.java)
private val uuidKey = NamespacedKey(shardSMP, "uuid")
private val customItemKey = NamespacedKey(shardSMP, "custom_item")

fun ItemStack.getUUID(): UUID? {
    if (!hasItemMeta() || !itemMeta.persistentDataContainer.has(uuidKey)) return null
    return UUID.fromString(itemMeta.persistentDataContainer[uuidKey, PersistentDataType.STRING])
}

fun ItemStack.isCustomItem(itemName: String): Boolean = itemMeta?.persistentDataContainer?.get(customItemKey, PersistentDataType.STRING) == itemName