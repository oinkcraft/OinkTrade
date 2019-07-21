package com.tek.otrade.trade;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import com.tek.rcore.ui.WrappedProperty;

public class Trade {
	
	private UUID senderUUID;
	private UUID receiverUUID;
	private TradeInterface senderInterface;
	private TradeInterface receiverInterface;
	private WrappedProperty<ItemStack[][]> senderItems;
	private WrappedProperty<ItemStack[][]> receiverItems;
	private WrappedProperty<Integer> senderXp;
	private WrappedProperty<Integer> receiverXp;
	private WrappedProperty<Integer> senderMoney;
	private WrappedProperty<Integer> receiverMoney;
	private WrappedProperty<Boolean> senderConfirm;
	private WrappedProperty<Boolean> receiverConfirm;
	
	public Trade(UUID senderUUID, UUID receiverUUID) {
		this.senderUUID = senderUUID;
		this.receiverUUID = receiverUUID;
		this.senderInterface = null;
		this.receiverInterface = null;
		this.senderItems = new WrappedProperty<ItemStack[][]>(new ItemStack[4][4]);
		this.receiverItems = new WrappedProperty<ItemStack[][]>(new ItemStack[4][4]);
		this.senderXp = new WrappedProperty<Integer>(0);
		this.receiverXp = new WrappedProperty<Integer>(0);
		this.senderMoney = new WrappedProperty<Integer>(0);
		this.receiverMoney = new WrappedProperty<Integer>(0);
		this.senderConfirm = new WrappedProperty<Boolean>(false);
		this.receiverConfirm = new WrappedProperty<Boolean>(false);
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
	
	public TradeInterface getSenderInterface() {
		return senderInterface;
	}
	
	public void setSenderInterface(TradeInterface senderInterface) {
		this.senderInterface = senderInterface;
	}
	
	public TradeInterface getReceiverInterface() {
		return receiverInterface;
	}
	
	public void setReceiverInterface(TradeInterface receiverInterface) {
		this.receiverInterface = receiverInterface;
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

	public WrappedProperty<Integer> getSenderMoney() {
		return senderMoney;
	}

	public void setSenderMoney(WrappedProperty<Integer> senderMoney) {
		this.senderMoney = senderMoney;
	}

	public WrappedProperty<Integer> getReceiverMoney() {
		return receiverMoney;
	}

	public void setReceiverMoney(WrappedProperty<Integer> receiverMoney) {
		this.receiverMoney = receiverMoney;
	}

	public WrappedProperty<Boolean> getSenderConfirm() {
		return senderConfirm;
	}

	public void setSenderConfirm(WrappedProperty<Boolean> senderConfirm) {
		this.senderConfirm = senderConfirm;
	}

	public WrappedProperty<Boolean> getReceiverConfirm() {
		return receiverConfirm;
	}

	public void setReceiverConfirm(WrappedProperty<Boolean> receiverConfirm) {
		this.receiverConfirm = receiverConfirm;
	}
	
}
