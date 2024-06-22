package me.lutto.shardsmp.items.weapons

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomCooldownItem
import me.lutto.shardsmp.items.Upgradable
import me.lutto.shardsmp.items.events.AbilityActivateEvent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

class TitansEdge(private val shardSMP: ShardSMP) : CustomCooldownItem(
    "titans_edge",
    Material.DIAMOND_SWORD,
    shardSMP.miniMessage.deserialize("<gradient:#aa0000:#ff5555>Titans Edge")
        .decoration(TextDecoration.ITALIC, false),
    listOf(shardSMP.miniMessage.deserialize("<gold>[Shift + Right Click]").decoration(TextDecoration.ITALIC, false)),
    5,
    true,
    120,
    true
), Upgradable, Listener {

    override fun getUpgradedCooldownTime(): Int = 90
    override fun getUpgradedCustomModelData(): Int = 12

    init {
        val strengthPotion = ItemStack(Material.LINGERING_POTION)
        val strengthPotionItemMeta = strengthPotion.itemMeta as PotionMeta
        strengthPotionItemMeta.basePotionData = PotionData(PotionType.STRENGTH)
        strengthPotion.setItemMeta(strengthPotionItemMeta)

        val recipe = ShapedRecipe(NamespacedKey.minecraft(getId()), item)
        recipe.shape(
            "SSS",
            "SDS",
            "NPN"
        )
        recipe.setIngredient('P', ExactChoice(strengthPotion))
        recipe.setIngredient('S', ExactChoice(shardSMP.itemManager.getItem("shard")!!.getItemStack()))
        recipe.setIngredient('D', Material.DIAMOND_SWORD)
        recipe.setIngredient('N', Material.NETHERITE_INGOT)
        super.setRecipe(recipe)

        shardSMP.itemManager.registerItem(this)
    }

    @EventHandler
    fun onAbilityActivated(event: AbilityActivateEvent) {
        if (event.getItem() != shardSMP.itemManager.getItem("titans_edge")) return

        var abilityDuration = 200
        if (shardSMP.itemManager.isUpgraded(event.getItemUUID())) {
            abilityDuration = 300
        }
        event.getPlayer().addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, abilityDuration, 2))
    }

}
