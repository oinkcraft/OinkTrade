package com.tek.otrade.ui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.tek.otrade.Main;
import com.tek.otrade.trade.Trade;
import com.tek.rcore.item.ItemBuilder;
import com.tek.rcore.item.SkullFactory;
import com.tek.rcore.misc.TextFormatter;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.components.ButtonComponent;
import com.tek.rcore.ui.components.PaginatedComponent;
import com.tek.rcore.ui.components.StaticComponent;

public class SpyInterface extends InterfaceState {

	private List<Trade> trades;
	private PaginatedComponent tradeList;
	
	public SpyInterface() {
		super(TextFormatter.color("&9Active Trades"), 4);
		trades = new ArrayList<Trade>();
	}

	@Override
	public void initialize(List<InterfaceComponent> components) {
		StaticComponent browserBackground = new StaticComponent(0, 0, 9, 3, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).withName(" ").build());
		tradeList = new PaginatedComponent(0, 0, 9, 3);
		StaticComponent line = new StaticComponent(0, 3, 9, 1, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).withName(" ").build());
		ButtonComponent previousButton = new ButtonComponent(0, 3, new ItemBuilder(Material.PAPER).withColoredName("&aPrevious Page").build());
		ButtonComponent nextButton = new ButtonComponent(8, 3, new ItemBuilder(Material.PAPER).withColoredName("&aNext Page").build());
		
		trades.addAll(Main.getInstance().getTradeManager().getTrades());
		
		previousButton.getClickedProperty().addWatcher(e -> tradeList.previousPage());
		nextButton.getClickedProperty().addWatcher(e -> tradeList.nextPage());
		
		for(Trade trade : trades) {
			TradeComponent component = new TradeComponent(trade);
			tradeList.addComponent(component);
			
			trade.getSenderInterface().getClosedProperty().addWatcher(e -> {
				tradeList.removeComponent(component);
				trades.remove(trade);
			});
		}
		
		components.add(browserBackground);
		components.add(tradeList);
		components.add(line);
		components.add(previousButton);
		components.add(nextButton);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		for(Trade trade : Main.getInstance().getTradeManager().getTrades()) {
			if(!trades.contains(trade)) {
				trades.add(trade);
				TradeComponent component = new TradeComponent(trade);
				tradeList.addComponent(component);
				
				trade.getSenderInterface().getClosedProperty().addWatcher(e -> {
					tradeList.removeComponent(component);
					trades.remove(trade);
				});
			}
		}
	}
	
	public static class TradeComponent extends ButtonComponent {

		private Trade trade;
		private ItemStack senderSkull;
		private ItemStack receiverSkull;
		private long timer;
		
		public TradeComponent(Trade trade) {
			super(-1, -1, null);
			this.trade = trade;
			this.senderSkull = SkullFactory.createSkull(trade.getSenderUUID());
			this.receiverSkull = SkullFactory.createSkull(trade.getReceiverUUID());
			this.timer = 0;
		}
		
		@Override
		public void tick(InterfaceState interfaceState) {
			super.tick(interfaceState);
			
			timer++;
			int frame = (int) (timer % 20);
			this.setItem(frame < 10 ? senderSkull : receiverSkull);
		}
		
		@Override
		public void onClick(InterfaceState interfaceState, ClickType type, ItemStack item, int x, int y) {
			interfaceState.getOwner().sendMessage("spy on " + trade.getSenderUUID());
		}
		
	}

}
