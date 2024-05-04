package me.lutto.shardsmp.items.weapons

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomCooldownItem
import me.lutto.shardsmp.items.Upgradable
import me.lutto.shardsmp.items.events.AbilityActivateEvent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.NamespacedKey
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

class EarthShatterer(private val shardSMP: ShardSMP) : CustomCooldownItem(
    "earth_shatterer",
    Material.NETHERITE_PICKAXE,
    MiniMessage.miniMessage().deserialize("<gradient:#57301f:#417837>Earth Shatterer")
        .decoration(TextDecoration.ITALIC, false),
    listOf(MiniMessage.miniMessage().deserialize("<gold>[Shift + Right Click]").decoration(TextDecoration.ITALIC, false)),
    7,
    true,
    120,
    true
), Upgradable, Listener {

    override fun getUpgradedCooldownTime(): Int = 120
    override fun getUpgradedCustomModelData(): Int {
        item.itemMeta.setCustomModelData(10)
        return 7
    }

    init {
        val speedPotion = ItemStack(Material.SPLASH_POTION)
        val speedPotionItemMeta = speedPotion.itemMeta as PotionMeta
        speedPotionItemMeta.basePotionData = PotionData(PotionType.SPEED, false, true)
        speedPotion.setItemMeta(speedPotionItemMeta)

        val recipe = ShapedRecipe(NamespacedKey.minecraft(getId()), item)
        recipe.shape(
            "SSS",
            "NAN",
            "PPP"
        )
        recipe.setIngredient('N', Material.NETHER_STAR)
        recipe.setIngredient('S', ExactChoice((shardSMP.itemManager.getItem("shard")!!.getItemStack())))
        recipe.setIngredient('P', ExactChoice(speedPotion))
        recipe.setIngredient('A', Material.NETHERITE_PICKAXE)
        super.setRecipe(recipe)

        shardSMP.itemManager.registerItem(this)
    }

    private fun ability(event: AbilityActivateEvent) {
        event.getPlayer().addPotionEffect(PotionEffect(PotionEffectType.SPEED, 300, 1))
        event.getPlayer().addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 300, 2))
    }

    private fun upgradedAbility(event: AbilityActivateEvent) {
        event.getPlayer().addPotionEffect(PotionEffect(PotionEffectType.SPEED, 300, 1))
        event.getPlayer().addPotionEffect(PotionEffect(PotionEffectType.FAST_DIGGING, 300, 3))
    }

    @EventHandler
    private fun onAbilityActivated(event: AbilityActivateEvent) {
        if (event.getItem() != shardSMP.itemManager.getCooldownItem("earth_shatterer")) return
        if (shardSMP.itemManager.isUpgraded(event.getItemUUID())) upgradedAbility(event) else ability(event)
    }

}
