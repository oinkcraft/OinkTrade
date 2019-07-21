package com.tek.OinkTrade.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
	
	public void fillArea(Inventory inv, ItemStack item, int x, int y, int width, int height) {
		for(int x1 = 0; x1 < width; x1++) {
			for(int y1 = 0; y1 < height; y1++) {
				inv.setItem(slot(x + x1, y + y1), item);
			}
		}
	}
	
	public void fillVertical(Inventory inv, ItemStack item, int column) {
		int invheight = inv.getSize() / 9 - 1;
		for(int i = 0; i <= invheight; i++) {
			inv.setItem(column + i * 9, item);
		}
	}
	
	public void fillHorizontal(Inventory inv, ItemStack item, int row) {
		for(int i = 0; i <= 8; i++) {
			int base = row * 9;
			inv.setItem(base + i, item);
		}
	}
	
	public int slot(int x, int y) {
		return(y * 9 + x);
	}
	
	public int x(int slot) {
		return(slot % 9);
	}
	
	public int y(int slot) {
		return((slot - (slot % 9)) / 9);
	}
}
