package com.tek.otrade.ui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.tek.otrade.trade.Trade;
import com.tek.rcore.item.SkullFactory;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.components.IPaginatedItem;

public class TradeComponent implements IPaginatedItem {

	private Trade trade;
	private ItemStack senderSkull;
	private ItemStack receiverSkull;
	private long timer;
	
	public TradeComponent(Trade trade) {
		this.trade = trade;
		this.senderSkull = SkullFactory.createSkull(trade.getSenderUUID());
		this.receiverSkull = SkullFactory.createSkull(trade.getReceiverUUID());
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
		interfaceState.getOwner().sendMessage("spy on " + trade.getSenderUUID());
	}
	
}