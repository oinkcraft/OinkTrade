package com.tek.otrade.trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TradeManager {
	
	private List<Trade> trades;
	
	public TradeManager() {
		trades = new ArrayList<Trade>();
	}
	
	public Optional<Trade> getTradeByUser(UUID uuid) {
		return trades.stream()
				.filter(trade -> trade.getSenderUUID().equals(uuid) || trade.getReceiverUUID().equals(uuid))
				.findFirst();
	}
	
	public List<Trade> getTrades() {
		return trades;
	}
	
}
