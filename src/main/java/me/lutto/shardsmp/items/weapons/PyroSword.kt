package me.lutto.shardsmp.items.weapons

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomCooldownItem
import me.lutto.shardsmp.items.Upgradable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import java.util.*


class PyroSword(private val shardSMP: ShardSMP) : CustomCooldownItem(
    "pyro_sword",
    Material.DIAMOND_SWORD,
    MiniMessage.miniMessage().deserialize("<gradient:#ff8b0f:#ffd60a>Pyro Sword")
        .decoration(TextDecoration.ITALIC, false),
    listOf(MiniMessage.miniMessage().deserialize("<gold>[Shift + Right Click]").decoration(TextDecoration.ITALIC, false)),
    4,
    true,
    240,
    false
), Upgradable, Listener {

    override fun getUpgradedCooldownTime(): Int = 120
    override fun getUpgradedCustomModelData(): Int = 15

    init {
        val itemMeta: ItemMeta = item.itemMeta
        itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 2, true)
        item.itemMeta = itemMeta

        val fireResPotion = ItemStack(Material.SPLASH_POTION)
        val fireResPotionItemMeta = fireResPotion.itemMeta as PotionMeta
        fireResPotionItemMeta.basePotionData = PotionData(PotionType.FIRE_RESISTANCE, true, false)
        fireResPotion.setItemMeta(fireResPotionItemMeta)

        val recipe = ShapedRecipe(NamespacedKey.minecraft(getId()), item)
        recipe.shape(
            "SSS",
            "PDP",
            "NBN"
        )
        recipe.setIngredient('P', ExactChoice(fireResPotion))
        recipe.setIngredient('S', ExactChoice(shardSMP.itemManager.getItem("shard")!!.getItemStack()))
        recipe.setIngredient('B', Material.DIAMOND_BLOCK)
        recipe.setIngredient('N', Material.NETHERITE_INGOT)
        recipe.setIngredient('D', Material.DIAMOND_SWORD)
        super.setRecipe(recipe)

        shardSMP.itemManager.registerItem(this)
    }

    @EventHandler
    fun onHit(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player) return
        if (event.entity !is Player) return

        val itemInMainHand = (event.damager as Player).inventory.itemInMainHand
        val customItemKey = NamespacedKey(shardSMP, "custom_item")
        val uuidKey = NamespacedKey(shardSMP, "uuid")

        if (itemInMainHand.isEmpty) return
        if (!itemInMainHand.itemMeta.persistentDataContainer.has(customItemKey)) return
        if (!itemInMainHand.itemMeta.persistentDataContainer.has(uuidKey)) return
        if (itemInMainHand.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] != "pyro_sword") return
        val itemUUID: UUID = UUID.fromString(item.itemMeta.persistentDataContainer[uuidKey, PersistentDataType.STRING])
        if (!shardSMP.itemManager.isActivated(itemUUID)) return

        val damagee = event.entity as Player
        if (damagee.isDead) return

        damagee.removePotionEffect(PotionEffectType.FIRE_RESISTANCE)
        damagee.sendActionBar(Component.text("Fire Removed!", NamedTextColor.RED))

        val customItem: CustomCooldownItem = shardSMP.itemManager.getCooldownItem("pyro_sword") ?: return
        (shardSMP.itemManager.getItemCooldown()[customItem.getId()] ?: return).asMap()[itemUUID] = System.currentTimeMillis() + (customItem.getCooldownTime()) * 1000
        shardSMP.itemManager.setIsActivated(itemUUID, true)
    }

}
