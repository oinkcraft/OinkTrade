package com.tek.otrade.ui;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;

import com.tek.otrade.Main;
import com.tek.otrade.trade.Trade;
import com.tek.otrade.trade.TradeRole;
import com.tek.rcore.item.ItemBuilder;
import com.tek.rcore.item.SkullFactory.NumberSet;
import com.tek.rcore.misc.TextFormatter;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.components.ButtonComponent;
import com.tek.rcore.ui.components.NumberDisplayComponent;
import com.tek.rcore.ui.components.NumberDisplayComponent.DrawOrder;
import com.tek.rcore.ui.components.SliderComponent.Direction;

public class MoneyInterface extends InterfaceState {

	private TradeRole role;
	private Trade trade;
	
	public MoneyInterface(TradeRole role, Trade trade) {
		super(TextFormatter.color("&6&lTrade Money"), 2);
		this.role = role;
		this.trade = trade;
	}
	
	@Override
	public void initialize(List<InterfaceComponent> components) {
		NumberDisplayComponent moneyDisplay = new NumberDisplayComponent(0, 1, NumberSet.WHITE, 9, Direction.HORIZONTAL, DrawOrder.START_LAST);
		ButtonComponent clear = new ButtonComponent(0, 0, new ItemBuilder(Material.BARRIER).withColoredName("&cClear Money").build());
		ButtonComponent max = new ButtonComponent(1, 0, new ItemBuilder(Material.BEACON).withColoredName("&aFull Balance").build());
		ButtonComponent one = new ButtonComponent(2, 0, 
				new ItemBuilder(Material.COAL).withColoredName("&6$1").withColoredLoreLine("&aLeft click &6+$1").withColoredLoreLine("&cRight click &6-$1").build());
		ButtonComponent ten = new ButtonComponent(3, 0, 
				new ItemBuilder(Material.BRICK).withColoredName("&6$10").withColoredLoreLine("&aLeft click &6+$10").withColoredLoreLine("&cRight click &6-$10").build());
		ButtonComponent hundred = new ButtonComponent(4, 0, 
				new ItemBuilder(Material.IRON_INGOT).withColoredName("&6$100").withColoredLoreLine("&aLeft click &6+$100").withColoredLoreLine("&cRight click &6-$100").build());
		ButtonComponent thousand = new ButtonComponent(5, 0, 
				new ItemBuilder(Material.GOLD_INGOT).withColoredName("&6$1,000").withColoredLoreLine("&aLeft click &6+$1,000").withColoredLoreLine("&cRight click &6-$1,000").build());
		ButtonComponent tenThousand = new ButtonComponent(6, 0, 
				new ItemBuilder(Material.DIAMOND).withColoredName("&6$10,000").withColoredLoreLine("&aLeft click &6+$10,000").withColoredLoreLine("&cRight click &6-$10,000").build());
		ButtonComponent hundredThousand = new ButtonComponent(7, 0, 
				new ItemBuilder(Material.EMERALD).withColoredName("&6$100,000").withColoredLoreLine("&aLeft click &6+$100,000").withColoredLoreLine("&cRight click &6-$100,000").build());
		ButtonComponent million = new ButtonComponent(8, 0, 
				new ItemBuilder(Material.NETHER_STAR).withColoredName("&6$1,000,000").withColoredLoreLine("&aLeft click &6+$1,000,000").withColoredLoreLine("&cRight click &6-$1,000,000").build());
		
		clear.getClickedProperty().addWatcher(e -> moneyDisplay.getValue().setValue(0l));
		max.getClickedProperty().addWatcher(e -> {
			long balance = (long) Math.floor(Main.getInstance().getEconomy().getBalance(getOwner()));
			moneyDisplay.getValue().setValue(balance);
		});
		
		one.getClickedProperty().addWatcher(event -> {
			int value = 1;
			if(event.getClickType().equals(ClickType.RIGHT)) {
				moneyDisplay.getValue().setValue(moneyDisplay.getValue().getValue() - value);
			} else {
				moneyDisplay.getValue().setValue(moneyDisplay.getValue().getValue() + value);
			}
		});
		
		ten.getClickedProperty().addWatcher(event -> {
			int value = 10;
			if(event.getClickType().equals(ClickType.RIGHT)) {
				moneyDisplay.getValue().setValue(moneyDisplay.getValue().getValue() - value);
			} else {
				moneyDisplay.getValue().setValue(moneyDisplay.getValue().getValue() + value);
			}
		});
		
		hundred.getClickedProperty().addWatcher(event -> {
			int value = 100;
			if(event.getClickType().equals(ClickType.RIGHT)) {
				moneyDisplay.getValue().setValue(moneyDisplay.getValue().getValue() - value);
			} else {
				moneyDisplay.getValue().setValue(moneyDisplay.getValue().getValue() + value);
			}
		});
		
		thousand.getClickedProperty().addWatcher(event -> {
			int value = 1000;
			if(event.getClickType().equals(ClickType.RIGHT)) {
				moneyDisplay.getValue().setValue(moneyDisplay.getValue().getValue() - value);
			} else {
				moneyDisplay.getValue().setValue(moneyDisplay.getValue().getValue() + value);
			}
		});
		
		tenThousand.getClickedProperty().addWatcher(event -> {
			int value = 10000;
			if(event.getClickType().equals(ClickType.RIGHT)) {
				moneyDisplay.getValue().setValue(moneyDisplay.getValue().getValue() - value);
			} else {
				moneyDisplay.getValue().setValue(moneyDisplay.getValue().getValue() + value);
			}
		});
		
		hundredThousand.getClickedProperty().addWatcher(event -> {
			int value = 100000;
			if(event.getClickType().equals(ClickType.RIGHT)) {
				moneyDisplay.getValue().setValue(moneyDisplay.getValue().getValue() - value);
			} else {
				moneyDisplay.getValue().setValue(moneyDisplay.getValue().getValue() + value);
			}
		});
		
		million.getClickedProperty().addWatcher(event -> {
			int value = 1000000;
			if(event.getClickType().equals(ClickType.RIGHT)) {
				moneyDisplay.getValue().setValue(moneyDisplay.getValue().getValue() - value);
			} else {
				moneyDisplay.getValue().setValue(moneyDisplay.getValue().getValue() + value);
			}
		});
		
		moneyDisplay.getValue().addWatcher(amount -> {
			long balance = (long) Math.floor(Main.getInstance().getEconomy().getBalance(getOwner()));
			if(amount > balance) {
				moneyDisplay.getValue().setValueSilent(balance);
			}
		});
		
		moneyDisplay.getValue().addWatcher(level -> {
			if(role.equals(TradeRole.SENDER)) {
				trade.getSenderMoney().setValue(level);
			} else if(role.equals(TradeRole.RECEIVER)) {
				trade.getReceiverMoney().setValue(level);
			}
		});
		
		components.add(clear);
		components.add(max);
		components.add(one);
		components.add(ten);
		components.add(hundred);
		components.add(thousand);
		components.add(tenThousand);
		components.add(hundredThousand);
		components.add(million);
		components.add(clear);
		components.add(moneyDisplay);
	}

}