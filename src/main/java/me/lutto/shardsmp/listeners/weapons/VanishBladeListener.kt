//package me.lutto.shardsmp.listeners.weapons
//
//import me.lutto.shardsmp.items.events.AbilityActivateEvent
//import me.lutto.shardsmp.ShardSMP
//import org.bukkit.Bukkit
//import org.bukkit.Material
//import org.bukkit.entity.Player
//import org.bukkit.event.EventHandler
//import org.bukkit.event.Listener
//import org.bukkit.inventory.EquipmentSlot
//import org.bukkit.inventory.ItemStack
//
//
//class VanishBladeListener(private val shardSMP: ShardSMP) : Listener {
//
//    fun changeEquipment(player: Player, toOwnItems: Boolean) {
//        if (toOwnItems) {
//            for (onlinePlayer in Bukkit.getOnlinePlayers()) {
//                if (onlinePlayer.uniqueId === player.uniqueId) continue
//                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.OFF_HAND, player.inventory.itemInOffHand)
//                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.HAND, player.inventory.itemInMainHand)
//                if (player.inventory.helmet != null) onlinePlayer.sendEquipmentChange(player, EquipmentSlot.HEAD, player.inventory.helmet)
//                if (player.inventory.chestplate != null) onlinePlayer.sendEquipmentChange(player, EquipmentSlot.CHEST, player.inventory.chestplate)
//                if (player.inventory.leggings != null) onlinePlayer.sendEquipmentChange(player, EquipmentSlot.LEGS, player.inventory.leggings)
//                if (player.inventory.boots != null) onlinePlayer.sendEquipmentChange(player, EquipmentSlot.FEET, player.inventory.boots)
//            }
//        } else {
//            for (onlinePlayer in Bukkit.getOnlinePlayers()) {
//                if (onlinePlayer.uniqueId === player.uniqueId) continue
//                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.HAND, ItemStack(Material.AIR))
//                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.OFF_HAND, ItemStack(Material.AIR))
//                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.HEAD, ItemStack(Material.AIR))
//                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.CHEST, ItemStack(Material.AIR))
//                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.LEGS, ItemStack(Material.AIR))
//                onlinePlayer.sendEquipmentChange(player, EquipmentSlot.FEET, ItemStack(Material.AIR))
//            }
//        }
//    }
//
//    @EventHandler
//    fun onAbilityActivate(event: AbilityActivateEvent) {
//        if (event.getItem() != shardSMP.itemManager.getItem("vanish_blade")) return
//        val player: Player = event.getPlayer()
//        val customItem = event.getItem()
//
//        customItem.setIsActivated(true)
//        player.isInvisible = true
//        changeEquipment(player, false)
//
//        Bukkit.getScheduler().runTaskLaterAsynchronously(shardSMP, Runnable {
//            player.sendRichMessage("<green>You are now visible to all players")
//            customItem.setIsActivated(false)
//            player.isInvisible = false
//            changeEquipment(player, true)
//        }, 300)
//    }
//
//}