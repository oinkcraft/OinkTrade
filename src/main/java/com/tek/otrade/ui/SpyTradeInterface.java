package com.tek.otrade.ui;

import java.util.List;

import org.bukkit.entity.Player;

import com.tek.otrade.trade.Trade;
import com.tek.rcore.misc.TextFormatter;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;

public class SpyTradeInterface extends InterfaceState {

	public SpyTradeInterface(Trade trade, Player sender, Player receiver) {
		super(TextFormatter.color("&a" + sender.getName() + " &6- &a" + receiver.getName()), 6);
	}

	@Override
	public void initialize(List<InterfaceComponent> components) {
		
	}

}
