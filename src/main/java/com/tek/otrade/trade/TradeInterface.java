package com.tek.otrade.trade;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.tek.rcore.item.ItemBuilder;
import com.tek.rcore.misc.TextFormatter;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.components.InventoryComponent;
import com.tek.rcore.ui.components.StaticComponent;
import com.tek.rcore.ui.components.SwitchComponent;

public class TradeInterface extends InterfaceState {

	private TradeRole role;
	private Trade trade;
	
	public TradeInterface(TradeRole role, Trade trade, Player with) {
		super(TextFormatter.color("&6Trading with &a&l" + with.getName()), 6);
		this.role = role;
		this.trade = trade;
	}
	
	@Override
	public void initialize(List<InterfaceComponent> components) {
		StaticComponent separator = new StaticComponent(4, 0, 1, 6, new ItemBuilder(Material.IRON_BARS).withName(" ").build());
		SwitchComponent playerConfirmIndicator = new SwitchComponent(0, 4, 4, 1, 
				new ItemBuilder(Material.RED_STAINED_GLASS_PANE).withColoredName("&cNot Ready.").build(),
				new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).withColoredName("&aReady.").build());
		SwitchComponent otherConfirmIndicator = new SwitchComponent(5, 4, 4, 1, 
				new ItemBuilder(Material.RED_STAINED_GLASS_PANE).withColoredName("&cNot Ready.").build(),
				new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).withColoredName("&aReady.").build());
		InventoryComponent playerInventory = new InventoryComponent(0, 0, 4, 4);
		InventoryComponent otherInventory = new InventoryComponent(5, 0, 4, 4, false, false);
		
		playerConfirmIndicator.setEditable(false);
		otherConfirmIndicator.setEditable(false);
		
		if(role.equals(TradeRole.SENDER)) {
			playerInventory.getInventoryContents().addWatcher(trade.getSenderItems()::setValue);
			trade.getReceiverItems().addWatcher(otherInventory.getInventoryContents()::setValue);
			trade.getSenderConfirm().addWatcher(playerConfirmIndicator.getState()::setValue);
			trade.getReceiverConfirm().addWatcher(otherConfirmIndicator.getState()::setValue);
		} else if(role.equals(TradeRole.RECEIVER)) {
			playerInventory.getInventoryContents().addWatcher(trade.getReceiverItems()::setValue);
			trade.getSenderItems().addWatcher(otherInventory.getInventoryContents()::setValue);
			trade.getReceiverConfirm().addWatcher(playerConfirmIndicator.getState()::setValue);
			trade.getSenderConfirm().addWatcher(otherConfirmIndicator.getState()::setValue);
		}
		
		components.add(separator);
		components.add(playerConfirmIndicator);
		components.add(otherConfirmIndicator);
		components.add(playerInventory);
		components.add(otherInventory);
	}

}
