package com.tek.otrade.trade;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.tek.otrade.Main;
import com.tek.otrade.Reference;
import com.tek.otrade.ui.TradeInterface;
import com.tek.rcore.item.InventoryUtils;
import com.tek.rcore.misc.TextFormatter;
import com.tek.rcore.ui.WrappedProperty;
import com.tek.rcore.ui.events.InterfaceCloseEvent;
import com.tek.rcore.ui.events.InterfaceCloseEvent.InterfaceCloseType;

import net.milkbowl.vault.economy.Economy;

public class Trade {
	
	private UUID senderUUID;
	private UUID receiverUUID;
	private List<UUID> spies;
	private TradeInterface senderInterface;
	private TradeInterface receiverInterface;
	private WrappedProperty<ItemStack[][]> senderItems;
	private WrappedProperty<ItemStack[][]> receiverItems;
	private WrappedProperty<Integer> senderXp;
	private WrappedProperty<Integer> receiverXp;
	private WrappedProperty<Long> senderMoney;
	private WrappedProperty<Long> receiverMoney;
	private WrappedProperty<Boolean> senderReady;
	private WrappedProperty<Boolean> receiverReady;
	private boolean completed;
	
	public Trade(UUID senderUUID, UUID receiverUUID) {
		this.senderUUID = senderUUID;
		this.receiverUUID = receiverUUID;
		this.spies = new ArrayList<UUID>(8);
		this.senderInterface = null;
		this.receiverInterface = null;
		this.senderItems = new WrappedProperty<ItemStack[][]>(new ItemStack[4][4]);
		this.receiverItems = new WrappedProperty<ItemStack[][]>(new ItemStack[4][4]);
		this.senderXp = new WrappedProperty<Integer>(0);
		this.receiverXp = new WrappedProperty<Integer>(0);
		this.senderMoney = new WrappedProperty<Long>(0l);
		this.receiverMoney = new WrappedProperty<Long>(0l);
		this.senderReady = new WrappedProperty<Boolean>(false);
		this.receiverReady = new WrappedProperty<Boolean>(false);
		this.completed = false;
		
		this.senderReady.addWatcher(s -> verifyCompletion());
		this.receiverReady.addWatcher(s -> verifyCompletion());
		
		this.senderItems.addWatcher(v -> change());
		this.receiverItems.addWatcher(v -> change());
		this.senderXp.addWatcher(v -> change());
		this.receiverXp.addWatcher(v -> change());
		this.senderMoney.addWatcher(v -> change());
		this.receiverMoney.addWatcher(v -> change());
	}
	
	public void verifyCompletion() {
		if(senderReady.getValue() && receiverReady.getValue()) {
			complete();
		}
	}
	
	public void change() {
		senderReady.setValue(false);
		receiverReady.setValue(false);
	}
	
	public void complete() {
		Player sender = Bukkit.getPlayer(senderUUID);
		Player receiver = Bukkit.getPlayer(receiverUUID);
		
		Economy eco = Main.getInstance().getEconomy();
		double senderBalance = eco.getBalance(sender);
		double receiverBalance = eco.getBalance(receiver);
		
		if(senderBalance >= senderMoney.getValue()) {
			if(receiverBalance >= receiverMoney.getValue()) {
				if(sender.getLevel() >= senderXp.getValue()) {
					if(receiver.getLevel() >= receiverXp.getValue()) {
						double newSenderMoney = -senderMoney.getValue() + receiverMoney.getValue();
						double newReceiverMoney = -receiverMoney.getValue() + senderMoney.getValue();
						if(newSenderMoney > 0) eco.depositPlayer(sender, newSenderMoney);
						else eco.withdrawPlayer(sender, Math.abs(newSenderMoney));
						if(newReceiverMoney > 0) eco.depositPlayer(receiver, newReceiverMoney);
						else eco.withdrawPlayer(receiver, Math.abs(newReceiverMoney));
						sender.setLevel(sender.getLevel() - senderXp.getValue() + receiverXp.getValue());
						receiver.setLevel(sender.getLevel() - receiverXp.getValue() + senderXp.getValue());
						
						completed = true;
						
						sender.sendMessage(Reference.PREFIX + TextFormatter.color("&aTrade completed. Enjoy your new items!"));
						receiver.sendMessage(Reference.PREFIX + TextFormatter.color("&aTrade completed. Enjoy your new items!"));
					} else {
						sender.sendMessage(Reference.PREFIX + TextFormatter.color("&cThe trade was cancelled because &6" + receiver.getName() + " &cdid not have enough XP."));
						receiver.sendMessage(Reference.PREFIX + TextFormatter.color("&cThe trade was cancelled because you did not have enough XP."));
					}
				} else {
					sender.sendMessage(Reference.PREFIX + TextFormatter.color("&cThe trade was cancelled because you did not have enough XP."));
					receiver.sendMessage(Reference.PREFIX + TextFormatter.color("&cThe trade was cancelled because &6" + sender.getName() + " &cdid not have enough XP."));
				}
			} else {
				sender.sendMessage(Reference.PREFIX + TextFormatter.color("&cThe trade was cancelled because &6" + receiver.getName() + " &cdid not have enough money."));
				receiver.sendMessage(Reference.PREFIX + TextFormatter.color("&cThe trade was cancelled because you did not have enough money."));
			}
		} else {
			sender.sendMessage(Reference.PREFIX + TextFormatter.color("&cThe trade was cancelled because you did not have enough money."));
			receiver.sendMessage(Reference.PREFIX + TextFormatter.color("&cThe trade was cancelled because &6" + sender.getName() + " &cdid not have enough money."));
		}
		
		close(new InterfaceCloseEvent(null, InterfaceCloseType.PROGRAMMATICAL, senderInterface));
	}
	
	public void close(InterfaceCloseEvent event) {
		Player sender = Bukkit.getPlayer(senderUUID);
		Player receiver = Bukkit.getPlayer(receiverUUID);
		
		if(!Main.getInstance().getTradeManager().isRegistered(this)) return;
			
		for(UUID spyUUID : spies) {
			Player spy = Bukkit.getPlayer(spyUUID);
			if(spy != null) {
				Main.getInstance().getRedstoneCore().getInterfaceManager().close(spy);
			}
		}
		
		if(event.getCloseType().equals(InterfaceCloseType.PLAYER) || (event.getCloseType().equals(InterfaceCloseType.PROGRAMMATICAL) && !completed)) {
			if(event.getCloseType().equals(InterfaceCloseType.PLAYER)) {
				if(sender.equals(event.getPlayer())) {
					sender.sendMessage(Reference.PREFIX + TextFormatter.color("&cYou have cancelled the trade with &6" + receiver.getName() + "&c."));
					receiver.sendMessage(Reference.PREFIX + TextFormatter.color("&cThe trade was cancelled by &6" + sender.getName() + "&c."));
				} else {
					sender.sendMessage(Reference.PREFIX + TextFormatter.color("&cThe trade was cancelled by &6" + receiver.getName() + "&c."));
					receiver.sendMessage(Reference.PREFIX + TextFormatter.color("&cYou have cancelled the trade with &6" + sender.getName() + "&c."));
				}
			}
			
			for(int x = 0; x < 4; x++) {
				for(int y = 0; y < 4; y++) {
					ItemStack senderItem = senderItems.getValue()[x][y];
					ItemStack receiverItem = receiverItems.getValue()[x][y];
					
					if(!InventoryUtils.isItemEmpty(senderItem)) {
						int amount = senderItem.getAmount();
						int fit = InventoryUtils.getItemFitCount(sender, senderItem);
						if(fit < amount) {
							senderItem.setAmount(fit);
							sender.getInventory().addItem(senderItem.clone());
							senderItem.setAmount(amount - fit);
							sender.getWorld().dropItem(sender.getLocation().add(0, 0.25, 0), senderItem);
						} else {
							sender.getInventory().addItem(senderItem);
						}
					}
					
					if(!InventoryUtils.isItemEmpty(receiverItem)) {
						int amount = receiverItem.getAmount();
						int fit = InventoryUtils.getItemFitCount(receiver, receiverItem);
						if(fit < amount) {
							receiverItem.setAmount(fit);
							receiver.getInventory().addItem(receiverItem.clone());
							receiverItem.setAmount(amount - fit);
							receiver.getWorld().dropItem(receiver.getLocation().add(0, 0.25, 0), receiverItem);
						} else {
							receiver.getInventory().addItem(receiverItem);
						}
					}
				}
			}
		} else if(event.getCloseType().equals(InterfaceCloseType.PROGRAMMATICAL) && completed) {
			for(int x = 0; x < 4; x++) {
				for(int y = 0; y < 4; y++) {
					ItemStack senderItem = receiverItems.getValue()[x][y];
					ItemStack receiverItem = senderItems.getValue()[x][y];
						
					if(!InventoryUtils.isItemEmpty(senderItem)) {
						int amount = senderItem.getAmount();
						int fit = InventoryUtils.getItemFitCount(sender, senderItem);
						if(fit < amount) {
							senderItem.setAmount(fit);
							sender.getInventory().addItem(senderItem.clone());
							senderItem.setAmount(amount - fit);
							sender.getWorld().dropItem(sender.getLocation().add(0, 0.25, 0), senderItem);
						} else {
							sender.getInventory().addItem(senderItem);
						}
					}
						
					if(!InventoryUtils.isItemEmpty(receiverItem)) {
						int amount = receiverItem.getAmount();
						int fit = InventoryUtils.getItemFitCount(receiver, receiverItem);
						if(fit < amount) {
							receiverItem.setAmount(fit);
							receiver.getInventory().addItem(receiverItem.clone());
							receiverItem.setAmount(amount - fit);
							receiver.getWorld().dropItem(receiver.getLocation().add(0, 0.25, 0), receiverItem);
						} else {
							receiver.getInventory().addItem(receiverItem);
						}
					}
				}
			}
		}
		
		Main.getInstance().getTradeManager().unregisterTrade(this);
		Main.getInstance().getRedstoneCore().getInterfaceManager().close(sender);
		Main.getInstance().getRedstoneCore().getInterfaceManager().close(receiver);
	}
	
	public UUID getSenderUUID() {
		return senderUUID;
	}
	
	public void setSenderUUID(UUID senderUUID) {
		this.senderUUID = senderUUID;
	}

	public UUID getReceiverUUID() {
		return receiverUUID;
	}

	public void setReceiverUUID(UUID receiverUUID) {
		this.receiverUUID = receiverUUID;
	}
	
	public List<UUID> getSpies() {
		return spies;
	}
	
	public void setSpies(List<UUID> spies) {
		this.spies = spies;
	}
	
	public TradeInterface getSenderInterface() {
		return senderInterface;
	}
	
	public void setSenderInterface(TradeInterface senderInterface) {
		this.senderInterface = senderInterface;
		this.senderInterface.getClosedProperty().addWatcher(this::close);
	}
	
	public TradeInterface getReceiverInterface() {
		return receiverInterface;
	}
	
	public void setReceiverInterface(TradeInterface receiverInterface) {
		this.receiverInterface = receiverInterface;
		this.receiverInterface.getClosedProperty().addWatcher(this::close);
	}

	public WrappedProperty<ItemStack[][]> getSenderItems() {
		return senderItems;
	}

	public void setSenderItems(WrappedProperty<ItemStack[][]> senderItems) {
		this.senderItems = senderItems;
	}

	public WrappedProperty<ItemStack[][]> getReceiverItems() {
		return receiverItems;
	}

	public void setReceiverItems(WrappedProperty<ItemStack[][]> receiverItems) {
		this.receiverItems = receiverItems;
	}

	public WrappedProperty<Integer> getSenderXp() {
		return senderXp;
	}

	public void setSenderXp(WrappedProperty<Integer> senderXp) {
		this.senderXp = senderXp;
	}

	public WrappedProperty<Integer> getReceiverXp() {
		return receiverXp;
	}

	public void setReceiverXp(WrappedProperty<Integer> receiverXp) {
		this.receiverXp = receiverXp;
	}

	public WrappedProperty<Long> getSenderMoney() {
		return senderMoney;
	}

	public void setSenderMoney(WrappedProperty<Long> senderMoney) {
		this.senderMoney = senderMoney;
	}

	public WrappedProperty<Long> getReceiverMoney() {
		return receiverMoney;
	}

	public void setReceiverMoney(WrappedProperty<Long> receiverMoney) {
		this.receiverMoney = receiverMoney;
	}

	public WrappedProperty<Boolean> getSenderReady() {
		return senderReady;
	}

	public void setSenderReady(WrappedProperty<Boolean> senderConfirm) {
		this.senderReady = senderConfirm;
	}

	public WrappedProperty<Boolean> getReceiverReady() {
		return receiverReady;
	}

	public void setReceiverReady(WrappedProperty<Boolean> receiverConfirm) {
		this.receiverReady = receiverConfirm;
	}
	
}
