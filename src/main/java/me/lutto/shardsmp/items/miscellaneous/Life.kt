package me.lutto.shardsmp.items.miscellaneous

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Life(private val shardSMP: ShardSMP) : CustomItem(
    "life",
    Material.RABBIT_FOOT,
    shardSMP.miniMessage.deserialize("<color:#ff1c27>Life").decoration(TextDecoration.ITALIC, false)
        .decoration(TextDecoration.ITALIC, false),
    listOf(),
    2
), Listener {

    init {
        val recipe = ShapedRecipe(NamespacedKey.minecraft(getId()), item)
        recipe.shape(
            "DND",
            "STS",
            "DND"
        )
        recipe.setIngredient('T', Material.TOTEM_OF_UNDYING)
        recipe.setIngredient('D', Material.DIAMOND_BLOCK)
        recipe.setIngredient('S', Material.NETHERITE_SCRAP)
        recipe.setIngredient('N', Material.NETHERITE_INGOT)
        super.setRecipe(recipe)

        shardSMP.itemManager.registerItem(this)
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player

        if (event.hand != EquipmentSlot.HAND) return
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return
        if (player.inventory.itemInMainHand.isEmpty) return
        val itemInMainHand: ItemStack = player.inventory.itemInMainHand

        val customItemKey = NamespacedKey(shardSMP, "custom_item")
        if (itemInMainHand.itemMeta != null && !itemInMainHand.itemMeta.persistentDataContainer.has(customItemKey)) return
        if (itemInMainHand.itemMeta.persistentDataContainer[customItemKey, PersistentDataType.STRING] != "life") return

        if (!shardSMP.livesManager.addLives(player.uniqueId, 1)) {
            player.sendRichMessage("<red>Cannot apply life!")
            return
        }
        player.inventory.remove(itemInMainHand)
        player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 120, 2))
        player.sendActionBar(Component.text("+1 Life!", NamedTextColor.GREEN))
        shardSMP.livesManager.updateListName(player)
    }

}
