package me.lutto.shardsmp.items.weapons

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomCooldownItem
import me.lutto.shardsmp.items.events.AbilityActivateEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
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
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

class TankShield(private val shardSMP: ShardSMP) : CustomCooldownItem(
    "tank_shield",
    Material.SHIELD,
    MiniMessage.miniMessage().deserialize("<gradient:#3d2216:#633723>Tank Shield")
        .decoration(TextDecoration.ITALIC, false),
    listOf(MiniMessage.miniMessage().deserialize("<gold>[Shift + Left Click]").decoration(TextDecoration.ITALIC, false)),
    8,
    false,
    120,
    true
), Listener {

    init {
        val turtleMasterPotion = ItemStack(Material.SPLASH_POTION)
        val turtleMasterPotionMeta = turtleMasterPotion.itemMeta as PotionMeta
        turtleMasterPotionMeta.basePotionData = PotionData(PotionType.TURTLE_MASTER, true, false)
        turtleMasterPotion.setItemMeta(turtleMasterPotionMeta)

        val recipe = ShapedRecipe(NamespacedKey.minecraft(getId()), item)
        recipe.shape(
            "NEN",
            "SHS",
            "DTD"
        )
        recipe.setIngredient('H', Material.SHIELD)
        recipe.setIngredient('S', ExactChoice(shardSMP.itemManager.getItem("shard")!!.getItemStack()))
        recipe.setIngredient('T', ExactChoice(turtleMasterPotion))
        recipe.setIngredient('E', Material.ENCHANTED_GOLDEN_APPLE)
        recipe.setIngredient('N', Material.NETHERITE_INGOT)
        recipe.setIngredient('D', Material.DIAMOND_BLOCK)
        super.setRecipe(recipe)

        shardSMP.itemManager.registerItem(this)
    }

    @EventHandler
    fun onAbilityActivate(event: AbilityActivateEvent) {
        if (event.getItem() != shardSMP.itemManager.getItem("tank_shield")) return
        val player: Player = event.getPlayer()
        val customItem: CustomCooldownItem = event.getItem()

        player.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 2))
        player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 200, 2))

        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.02
        player.getAttribute(Attribute.GENERIC_FLYING_SPEED)?.baseValue = 0.02

        val actionbarWarning = Bukkit.getScheduler().runTaskTimer(shardSMP,
            Runnable {
                player.sendActionBar(Component.text("You can't attack for 10 seconds!", NamedTextColor.RED))
            }, 1, 40
        )

        customItem.setIsActivated(true)
        Bukkit.getScheduler().runTaskLater(shardSMP, Runnable {
            customItem.setIsActivated(false)
            actionbarWarning.cancel()
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.1
        }, 200)
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player) return
        val player: Player = event.damager as Player

        for (item in player.inventory) {
            val customItemKey = NamespacedKey(shardSMP, "custom_item")
            val uuidKey = NamespacedKey(shardSMP, "uuid")

            if (item == null) continue
            if (item.itemMeta == null) continue
            if (!item.itemMeta.persistentDataContainer.has(customItemKey)) continue
            if (!item.itemMeta.persistentDataContainer.has(uuidKey)) continue
            if (item.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] != "tank_shield") continue
            if (!(shardSMP.itemManager.getCooldownItem(item.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] ?: return)?.isActivated() ?: return)) continue

            event.isCancelled = true
        }
    }

}
