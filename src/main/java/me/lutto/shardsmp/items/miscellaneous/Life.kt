package me.lutto.shardsmp.items.miscellaneous

import me.lutto.shardsmp.ShardSMP
import me.lutto.shardsmp.items.CustomItem
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.Listener
import org.bukkit.inventory.ShapedRecipe

class Life(private val shardSMP: ShardSMP) : CustomItem(
    "life",
    Material.RABBIT_FOOT,
    MiniMessage.miniMessage().deserialize("<color:#ff1c27>Life").decoration(TextDecoration.ITALIC, false)
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

}
