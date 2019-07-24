package com.tek.otrade.ui;

import java.util.List;

import org.bukkit.Material;

import com.tek.otrade.trade.Trade;
import com.tek.otrade.trade.TradeRole;
import com.tek.rcore.misc.TextFormatter;
import com.tek.rcore.ui.InterfaceComponent;
import com.tek.rcore.ui.InterfaceState;
import com.tek.rcore.ui.components.SliderComponent;
import com.tek.rcore.ui.components.SliderComponent.Direction;

public class ExperienceInterface extends InterfaceState {

	private TradeRole role;
	private Trade trade;
	
	public ExperienceInterface(TradeRole role, Trade trade) {
		super(TextFormatter.color("&b&lTrade XP Levels"), 1);
		this.role = role;
		this.trade = trade;
	}

	@Override
	public void initialize(List<InterfaceComponent> components) {
		SliderComponent experienceSlider = new SliderComponent(0, 0, 9, Direction.HORIZONTAL, 0, getOwner().getLevel(), Material.EXPERIENCE_BOTTLE, Material.GLASS_BOTTLE);
		
		experienceSlider.getValue().addWatcher(level -> {
			if(role.equals(TradeRole.SENDER)) {
				trade.getSenderXp().setValue(level);
			} else if(role.equals(TradeRole.RECEIVER)) {
				trade.getReceiverXp().setValue(level);
			}
		});
		
		components.add(experienceSlider);
	}

}
