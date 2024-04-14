package me.lutto.shardsmp.instance

import org.bukkit.inventory.ItemStack

class CustomItem(private val id: String, private val item: ItemStack) {

    fun getId(): String = id
    fun getItemStack(): ItemStack = item

}