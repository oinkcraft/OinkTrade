package com.tek.otrade.trade;

import java.util.Arrays;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.components.ButtonComponent;

public class ValueButtonComponent extends ButtonComponent {

	private String lorePattern;
	private int lastValue;
	private int value;
	
	public ValueButtonComponent(int x, int y, int width, int height, ItemStack item, String lorePattern) {
		super(x, y, width, height, item);
		this.lorePattern = lorePattern;
		lastValue = Integer.MAX_VALUE;
		value = 0;
	}
	
	public ValueButtonComponent(int x, int y, ItemStack item, String lorePattern) {
		super(x, y, item);
		this.lorePattern = lorePattern;
		lastValue = Integer.MAX_VALUE;
		value = 0;
	}
	
	@Override
	public void render(InterfaceState interfaceState, ItemStack[][] drawBuffer) {
		if(lastValue != value) {
			ItemMeta meta = getItem().getItemMeta();
			meta.setLore(Arrays.asList(String.format(lorePattern, value)));
			getItem().setItemMeta(meta);
		}
		
		super.render(interfaceState, drawBuffer);
	}
	
	public void setValue(int value) {
		this.value = value;
	}

}
