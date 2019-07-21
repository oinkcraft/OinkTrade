package com.tek.OinkTrade.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtil {
	
	public static ItemStack createItemStack(Material mat, int amount, String displayName, String... lore) {
		ItemStack itemStack = new ItemStack(mat, amount);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(displayName);
		List<String> lores = new ArrayList<String>();
		for(String str : lore) {
			lores.add(str);
		}
		itemMeta.setLore(lores);
		itemStack.setItemMeta(itemMeta);
		
		return itemStack;
	}
	
}
