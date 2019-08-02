package com.tek.otrade.ui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.tek.otrade.trade.Trade;
import com.tek.rcore.item.InventoryUtils;
import com.tek.rcore.item.ItemBuilder;
import com.tek.rcore.item.SkullFactory;
import com.tek.rcore.misc.TextFormatter;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.components.InventoryComponent;
import com.tek.rcore.ui.components.StaticComponent;
import com.tek.rcore.ui.components.SwitchComponent;

public class SpyTradeInterface extends InterfaceState {

	private Trade trade;
	
	public SpyTradeInterface(Trade trade, Player sender, Player receiver) {
		super(TextFormatter.color("&a" + sender.getName() + " &6- &a" + receiver.getName()), 6);
		this.trade = trade;
	}

	@Override
	public void initialize(List<InterfaceComponent> components) {
		Player sender = Bukkit.getPlayer(trade.getSenderUUID());
		Player receiver = Bukkit.getPlayer(trade.getReceiverUUID());
		
		InventoryComponent senderInventory = new InventoryComponent(0, 0, 4, 4, false, false);
		InventoryComponent receiverInventory = new InventoryComponent(5, 0, 4, 4, false, false);
		SwitchComponent senderReadyIndicator = new SwitchComponent(0, 4, 4, 1, 
				new ItemBuilder(Material.RED_STAINED_GLASS_PANE).withColoredName("&cNot Ready.").build(),
				new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).withColoredName("&aReady.").build());
		SwitchComponent receiverReadyIndicator = new SwitchComponent(5, 4, 4, 1, 
				new ItemBuilder(Material.RED_STAINED_GLASS_PANE).withColoredName("&cNot Ready.").build(),
				new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).withColoredName("&aReady.").build());
		ValueButtonComponent senderXpButton = new ValueButtonComponent(0, 5, 
				new ItemBuilder(Material.EXPERIENCE_BOTTLE).withColoredName("&bXP Levels").build(), 
				TextFormatter.color("&aCurrent: &b%d levels"));
		ValueButtonComponent senderMoneyButton = new ValueButtonComponent(1, 5, 
				new ItemBuilder(Material.GOLD_INGOT).withColoredName("&6Money").build(), 
				TextFormatter.color("&aCurrent: &6$%d"));
		ValueButtonComponent receiverXpButton = new ValueButtonComponent(7, 5, 
				new ItemBuilder(Material.EXPERIENCE_BOTTLE).withColoredName("&bXP Levels").build(), 
				TextFormatter.color("&aCurrent: &b%d levels"));
		ValueButtonComponent receiverMoneyButton = new ValueButtonComponent(8, 5, 
				new ItemBuilder(Material.GOLD_INGOT).withColoredName("&6Money").build(), 
				TextFormatter.color("&aCurrent: &6$%d"));
		StaticComponent senderSkull = new StaticComponent(2, 5, 2, 1, InventoryUtils.renameItem(SkullFactory.createSkull(trade.getSenderUUID()),
				TextFormatter.color("&6" + sender.getName() + "&a's Side")));
		StaticComponent receiverSkull = new StaticComponent(5, 5, 2, 1, InventoryUtils.renameItem(SkullFactory.createSkull(trade.getReceiverUUID()),
				TextFormatter.color("&6" + receiver.getName() + "&a's Side")));
		StaticComponent separator = new StaticComponent(4, 0, 1, 6, new ItemBuilder(Material.IRON_BARS).withName(" ").build());
		
		senderInventory.getInventoryContents().setValue(trade.getSenderItems().getValue());
		receiverInventory.getInventoryContents().setValue(trade.getReceiverItems().getValue());
		senderReadyIndicator.getState().setValue(trade.getSenderReady().getValue());
		receiverReadyIndicator.getState().setValue(trade.getReceiverReady().getValue());
		senderXpButton.setValue(trade.getSenderXp().getValue());
		receiverXpButton.setValue(trade.getReceiverXp().getValue());
		senderMoneyButton.setValue(trade.getSenderMoney().getValue());
		receiverMoneyButton.setValue(trade.getReceiverMoney().getValue());
		
		senderInventory.setEditable(false);
		receiverInventory.setEditable(false);
		senderReadyIndicator.setEditable(false);
		receiverReadyIndicator.setEditable(false);
		senderXpButton.setEditable(false);
		receiverXpButton.setEditable(false);
		senderMoneyButton.setEditable(false);
		receiverMoneyButton.setEditable(false);
		
		trade.getSenderItems().addWatcher(senderInventory.getInventoryContents()::setValue);
		trade.getReceiverItems().addWatcher(receiverInventory.getInventoryContents()::setValue);
		trade.getSenderXp().addWatcher(senderXpButton::setValue);
		trade.getReceiverXp().addWatcher(receiverXpButton::setValue);
		trade.getSenderMoney().addWatcher(senderMoneyButton::setValue);
		trade.getReceiverMoney().addWatcher(receiverMoneyButton::setValue);
		trade.getSenderReady().addWatcher(senderReadyIndicator.getState()::setValue);
		trade.getReceiverReady().addWatcher(receiverReadyIndicator.getState()::setValue);
		
		components.add(senderInventory);
		components.add(receiverInventory);
		components.add(senderReadyIndicator);
		components.add(receiverReadyIndicator);
		components.add(senderXpButton);
		components.add(senderMoneyButton);
		components.add(receiverXpButton);
		components.add(receiverMoneyButton);
		components.add(senderSkull);
		components.add(receiverSkull);
		components.add(separator);
	}

}
