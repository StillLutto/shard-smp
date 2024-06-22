package me.lutto.shardsmp.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class MaxEnchantCommand : CommandExecutor {

    private fun handleItem(item: ItemStack): ItemMeta {
        val meta: ItemMeta = item.itemMeta
        for (enchantment in Enchantment.values()) {
            if (enchantment.isCursed) continue
            val type = item.type
            if (EnchantmentTarget.BOW.includes(type) && badBowEnchantment(enchantment)) continue
            if (EnchantmentTarget.WEAPON.includes(type) && badSwordEnchantment(enchantment)) continue
            if (EnchantmentTarget.ARMOR.includes(type) && badArmorEnchantment(enchantment)) continue

            if (!enchantment.canEnchantItem(item)) continue

            val itemEnchantments = meta.enchants.keys
            if (itemEnchantments.any { enchantment.conflictsWith(it) }) continue

            meta.addEnchant(enchantment, enchantment.maxLevel, true)
        }
        return meta
    }

    private fun badBowEnchantment(enchantment: Enchantment): Boolean {
        if (enchantment == Enchantment.MENDING) return true
        if (enchantment == Enchantment.ARROW_DAMAGE) return true
        return when (enchantment) {
            Enchantment.MENDING,
            Enchantment.ARROW_DAMAGE -> true
            else -> false
        }
    }

    private fun badSwordEnchantment(enchantment: Enchantment): Boolean {
        return enchantment == Enchantment.KNOCKBACK
    }

    private fun badArmorEnchantment(enchantment: Enchantment): Boolean {
        return when (enchantment) {
            Enchantment.THORNS,
            Enchantment.PROTECTION_FIRE,
            Enchantment.PROTECTION_EXPLOSIONS,
            Enchantment.PROTECTION_PROJECTILE,
            Enchantment.FROST_WALKER -> true
            else -> false
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (sender !is Player) {
            Bukkit.getLogger().info("A player must run this command!")
            return false
        }

        if (!sender.isOp()) {
            sender.sendRichMessage("<red>You need to be an operator to use this command!")
            return false
        }

        val itemInMainHand: ItemStack = sender.inventory.itemInMainHand
        if (itemInMainHand.itemMeta == null) return false

        itemInMainHand.setItemMeta(handleItem(itemInMainHand))

        sender.sendRichMessage("<green>Successfully enchanted item!")
        return true

    }

}