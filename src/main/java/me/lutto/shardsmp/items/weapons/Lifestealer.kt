package me.lutto.shardsmp.items.weapons

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomItem
import me.lutto.shardsmp.items.Upgradable
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType
import java.util.*

class Lifestealer(private val shardSMP: ShardSMP) : CustomItem(
    "lifestealer",
    Material.DIAMOND_SWORD,
    shardSMP.miniMessage.deserialize("<gradient:#57301f:#417837><gradient:#aa0000:#ff2119>ʟɪꜰᴇѕᴛᴇᴀʟᴇʀ").decoration(TextDecoration.ITALIC, false)
        .decoration(TextDecoration.ITALIC, false),
    listOf(),
    9
), Upgradable, Listener {

    override fun getUpgradedCooldownTime(): Int = 0
    override fun getUpgradedCustomModelData(): Int = 13

    init {
        val turtleMasterPotion = ItemStack(Material.SPLASH_POTION)
        val turtleMasterPotionMeta = turtleMasterPotion.itemMeta as PotionMeta
        turtleMasterPotionMeta.basePotionData = PotionData(PotionType.TURTLE_MASTER, true, false)
        turtleMasterPotion.setItemMeta(turtleMasterPotionMeta)

        val recipe = ShapedRecipe(NamespacedKey.minecraft(getId()), item)
        recipe.shape(
            "SES",
            "EDE",
            "SES"
        )
        recipe.setIngredient('S', ExactChoice(shardSMP.itemManager.getItem("shard")!!.getItemStack()))
        recipe.setIngredient('E', Material.ENCHANTED_GOLDEN_APPLE)
        recipe.setIngredient('D', Material.DIAMOND_SWORD)
        super.setRecipe(recipe)

        shardSMP.itemManager.registerItem(this)
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player) return
        val player: Player = event.damager as Player
        if (player.gameMode == GameMode.CREATIVE) return

        val itemInMainHand = player.inventory.itemInMainHand
        val customItemKey = NamespacedKey(shardSMP, "custom_item")
        val uuidKey = NamespacedKey(shardSMP, "uuid")
        if (itemInMainHand.itemMeta != null && itemInMainHand.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] != "lifestealer") return

        val itemUUID: UUID = UUID.fromString(itemInMainHand.itemMeta.persistentDataContainer[uuidKey, PersistentDataType.STRING])
        var chance = Random().nextInt(4) == 0
        if (shardSMP.itemManager.isUpgraded(itemUUID)) {
            val randomInt = Random().nextInt(5)
            chance = randomInt == 0 || randomInt == 1
        }

        if (!chance) return

        if ((player.health + event.finalDamage) > player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value) {
            player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
            return
        }
        player.health += event.finalDamage
    }

}
