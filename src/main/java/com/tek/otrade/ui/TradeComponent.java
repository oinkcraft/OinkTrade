package com.tek.otrade.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.tek.otrade.Main;
import com.tek.otrade.trade.Trade;
import com.tek.rcore.item.InventoryUtils;
import com.tek.rcore.item.SkullFactory;
import com.tek.rcore.misc.TextFormatter;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.components.IPaginatedItem;
import com.tek.rcore.ui.events.InterfaceCloseEvent.InterfaceCloseType;

public class TradeComponent implements IPaginatedItem {

	private Trade trade;
	private ItemStack senderSkull;
	private ItemStack receiverSkull;
	private long timer;
	
	public TradeComponent(Trade trade) {
		this.trade = trade;
		Player sender = Bukkit.getPlayer(trade.getSenderUUID());
		Player receiver = Bukkit.getPlayer(trade.getReceiverUUID());
		String tradeItemName = TextFormatter.color("&a" + sender.getName() + " &6- &a" + receiver.getName());
		this.senderSkull = InventoryUtils.renameItem(SkullFactory.createSkull(trade.getSenderUUID()),
				tradeItemName);
		this.receiverSkull = InventoryUtils.renameItem(SkullFactory.createSkull(trade.getReceiverUUID()),
				tradeItemName);
		this.timer = 0;
	}
	
	@Override
	public ItemStack render(InterfaceState interfaceState) {
		int frame = (int) (timer % 20);
		return frame < 10 ? senderSkull : receiverSkull;
	}
	
	@Override
	public void tick(InterfaceState interfaceState) {
		timer++;
	}
	
	@Override
	public void click(InterfaceState interfaceState, ClickType type, ItemStack item) {
		Player sender = Bukkit.getPlayer(trade.getSenderUUID());
		Player receiver = Bukkit.getPlayer(trade.getReceiverUUID());
		
		trade.getSpies().add(interfaceState.getUUID());
		SpyTradeInterface spyTradeInterface = new SpyTradeInterface(trade, sender, receiver);
		spyTradeInterface.getClosedProperty().addWatcher(e -> {
			if(e.getCloseType().equals(InterfaceCloseType.PLAYER))
				trade.getSpies().remove(interfaceState.getUUID());
		});
		Main.getInstance().getRedstoneCore().getInterfaceManager().openInterface(interfaceState.getOwner(), spyTradeInterface);
	}
	
}