package com.tek.otrade.trade;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

public class Trade {
	
	private UUID senderUUID;
	private UUID receiverUUID;
	private ItemStack[][] senderItems;
	private ItemStack[][] receiverItems;
	private int senderXp;
	private int receiverXp;
	private int senderMoney;
	private int receiverMoney;
	private boolean senderConfirm;
	private boolean receiverConfirm;
	
	public Trade(UUID senderUUID, UUID receiverUUID) {
		this.senderUUID = senderUUID;
		this.receiverUUID = receiverUUID;
		this.senderItems = new ItemStack[4][4];
		this.receiverItems = new ItemStack[4][4];
		this.senderXp = 0;
		this.receiverXp = 0;
		this.senderMoney = 0;
		this.receiverMoney = 0;
		this.senderConfirm = false;
		this.receiverConfirm = false;
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
	
	public ItemStack[][] getSenderItems() {
		return senderItems;
	}
	
	public void setSenderItems(ItemStack[][] senderItems) {
		this.senderItems = senderItems;
	}
	
	public ItemStack[][] getReceiverItems() {
		return receiverItems;
	}
	
	public void setReceiverItems(ItemStack[][] receiverItems) {
		this.receiverItems = receiverItems;
	}
	
	public int getSenderXp() {
		return senderXp;
	}
	
	public void setSenderXp(int senderXp) {
		this.senderXp = senderXp;
	}
	
	public int getReceiverXp() {
		return receiverXp;
	}
	
	public void setReceiverXp(int receiverXp) {
		this.receiverXp = receiverXp;
	}
	
	public int getSenderMoney() {
		return senderMoney;
	}
	
	public void setSenderMoney(int senderMoney) {
		this.senderMoney = senderMoney;
	}
	
	public int getReceiverMoney() {
		return receiverMoney;
	}
	
	public void setReceiverMoney(int receiverMoney) {
		this.receiverMoney = receiverMoney;
	}
	
	public boolean isSenderConfirm() {
		return senderConfirm;
	}
	
	public void setSenderConfirm(boolean senderConfirm) {
		this.senderConfirm = senderConfirm;
	}
	
	public boolean isReceiverConfirm() {
		return receiverConfirm;
	}
	
	public void setReceiverConfirm(boolean receiverConfirm) {
		this.receiverConfirm = receiverConfirm;
	}
	
}
