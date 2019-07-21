package com.tek.otrade.trade;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.tek.rcore.item.ItemBuilder;
import com.tek.rcore.misc.TextFormatter;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.components.ButtonComponent;
import com.tek.rcore.ui.components.InventoryComponent;
import com.tek.rcore.ui.components.StaticComponent;
import com.tek.rcore.ui.components.SwitchComponent;

public class TradeInterface extends InterfaceState {

	private TradeRole role;
	private Trade trade;
	private Player with;
	
	public TradeInterface(TradeRole role, Trade trade, Player with) {
		super(TextFormatter.color("&6Trading with &a&l" + with.getName()), 6);
		this.role = role;
		this.trade = trade;
		this.with = with;
	}
	
	@Override
	public void initialize(List<InterfaceComponent> components) {
		StaticComponent separator = new StaticComponent(4, 0, 1, 6, new ItemBuilder(Material.IRON_BARS).withName(" ").build());
		SwitchComponent playerReadyIndicator = new SwitchComponent(0, 4, 4, 1, 
				new ItemBuilder(Material.RED_STAINED_GLASS_PANE).withColoredName("&cNot Ready.").build(),
				new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).withColoredName("&aReady.").build());
		SwitchComponent otherReadyIndicator = new SwitchComponent(5, 4, 4, 1, 
				new ItemBuilder(Material.RED_STAINED_GLASS_PANE).withColoredName("&cNot Ready.").build(),
				new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).withColoredName("&aReady.").build());
		InventoryComponent playerInventory = new InventoryComponent(0, 0, 4, 4);
		InventoryComponent otherInventory = new InventoryComponent(5, 0, 4, 4, false, false);
		ValueButtonComponent xpButton = new ValueButtonComponent(0, 5, 
				new ItemBuilder(Material.EXPERIENCE_BOTTLE).withColoredName("&bXP Levels").build(), 
				TextFormatter.color("&aCurrent: &b%d levels"));
		ValueButtonComponent moneyButton = new ValueButtonComponent(1, 5, 
				new ItemBuilder(Material.GOLD_INGOT).withColoredName("&6Money").build(), 
				TextFormatter.color("&aCurrent: &6$%d"));
		ButtonComponent readyButton = new ButtonComponent(2, 5, new ItemBuilder(Material.LIME_CONCRETE).withColoredName("&aReady").build());
		ButtonComponent unreadyButton = new ButtonComponent(3, 5, new ItemBuilder(Material.RED_CONCRETE).withColoredName("&cUnready").build());
		ValueButtonComponent xpOther = new ValueButtonComponent(5, 5, 2, 1, 
				new ItemBuilder(Material.EXPERIENCE_BOTTLE).withColoredName("&a" + with.getName() + "&b's XP Levels").build(), 
				TextFormatter.color("&aCurrent: &b%d levels"));
		ValueButtonComponent moneyOther = new ValueButtonComponent(7, 5, 2, 1, 
				new ItemBuilder(Material.GOLD_INGOT).withColoredName("&a" + with.getName() + "&6's Money").build(), 
				TextFormatter.color("&aCurrent: &6$%d"));
		
		playerReadyIndicator.setEditable(false);
		otherReadyIndicator.setEditable(false);
		xpOther.setEditable(false);
		moneyOther.setEditable(false);
		
		if(role.equals(TradeRole.SENDER)) {
			playerInventory.getInventoryContents().addWatcher(trade.getSenderItems()::setValue);
			trade.getReceiverItems().addWatcher(otherInventory.getInventoryContents()::setValue);
			trade.getSenderXp().addWatcher(xpButton::setValue);
			trade.getSenderMoney().addWatcher(moneyButton::setValue);
			trade.getSenderReady().addWatcher(playerReadyIndicator.getState()::setValue);
			trade.getReceiverReady().addWatcher(otherReadyIndicator.getState()::setValue);
			readyButton.getClickedProperty().addWatcher(e -> trade.getSenderReady().setValue(true));
			unreadyButton.getClickedProperty().addWatcher(e -> trade.getSenderReady().setValue(false));
		} 
		
		else if(role.equals(TradeRole.RECEIVER)) {
			playerInventory.getInventoryContents().addWatcher(trade.getReceiverItems()::setValue);
			trade.getSenderItems().addWatcher(otherInventory.getInventoryContents()::setValue);
			trade.getReceiverXp().addWatcher(xpButton::setValue);
			trade.getReceiverMoney().addWatcher(moneyButton::setValue);
			trade.getReceiverReady().addWatcher(playerReadyIndicator.getState()::setValue);
			trade.getSenderReady().addWatcher(otherReadyIndicator.getState()::setValue);
			readyButton.getClickedProperty().addWatcher(e -> trade.getReceiverReady().setValue(true));
			unreadyButton.getClickedProperty().addWatcher(e -> trade.getReceiverReady().setValue(false));
		}
		
		components.add(separator);
		components.add(playerReadyIndicator);
		components.add(otherReadyIndicator);
		components.add(playerInventory);
		components.add(otherInventory);
		components.add(xpButton);
		components.add(moneyButton);
		components.add(readyButton);
		components.add(unreadyButton);
		components.add(xpOther);
		components.add(moneyOther);
	}

}
