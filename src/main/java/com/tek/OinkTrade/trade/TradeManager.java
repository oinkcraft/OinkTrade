package com.tek.OinkTrade.trade;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.tek.OinkTrade.Main;
import com.tek.OinkTrade.Reference;
import com.tek.OinkTrade.utils.InventoryUtils;

public class TradeManager {
	
	Main main;
	InventoryUtils iutils;
	
	public ArrayList<TradeObject> trades = new ArrayList<TradeObject>();
	
	public TradeManager(Main main) {
		this.main = main;
		this.iutils = new InventoryUtils();
	}
	
	public void startTrade(Player sender, Player receiver) {
		TradeObject trade = new TradeObject(main, sender, receiver);
		trades.add(trade);
		sender.sendMessage(Reference.prefix + ChatColor.GREEN + "Your request has been sent! The receiver has 20 seconds to accept it");
		sender.sendMessage(Reference.prefix + ChatColor.BLUE + "NOTE! Make sure that you are in a safe spot as you can still be hit or killed");
		receiver.sendMessage(Reference.prefix + ChatColor.GREEN + "You have been invited to trade with " + sender.getName());
		receiver.sendMessage(Reference.prefix + ChatColor.GREEN + "/oinktrade <accept/decline> This trade will expire in 20 seconds");
		receiver.sendMessage(Reference.prefix + ChatColor.BLUE + "NOTE! Make sure that you are in a safe spot as you can still be hit or killed");
		
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			public void run() {
				if(!trade.init) {
					trades.remove(trade);
					sender.sendMessage(Reference.prefix + ChatColor.RED + "You trade request has expired...");
					receiver.sendMessage(Reference.prefix + ChatColor.RED + sender.getName() + "'s request has expired");
				}
			}
		}, 20 * 20l);
	}
	
	public boolean completeTrade(TradeObject trade) {
		trades.remove(trade);
		Inventory inv = trade.inv;
		
		for(Player player : trade.spies) {
			player.closeInventory();
			player.sendMessage(Reference.prefix + ChatColor.RED + "The trade you were watching ended");
		}
		
		trade.sender.sendMessage(Reference.prefix + ChatColor.GREEN + "The trade has been completed!");
		trade.receiver.sendMessage(Reference.prefix + ChatColor.GREEN + "The trade has been completed!");
		trade.sender.closeInventory();
		trade.receiver.closeInventory();
		
		for(int x = 0; x <= 3; x++) {
			for(int y = 0; y <= 3; y++) {
				int z = x + 5;
				if(inv.getItem(iutils.slot(x, y)) != null) trade.receiver.getInventory().addItem(inv.getItem(iutils.slot(x, y)));
				if(inv.getItem(iutils.slot(z, y)) != null) trade.sender.getInventory().addItem(inv.getItem(iutils.slot(z, y)));
			}
		}
		
		return true;
	}
	
	public void denyTrade(TradeObject trade) {
		trades.remove(trade);
		trade.sender.sendMessage(Reference.prefix + ChatColor.RED + trade.receiver.getName() + " has declined your trade request");
		trade.receiver.sendMessage(Reference.prefix + ChatColor.RED + "You have declined " + trade.sender.getName() + "'s request");
	}
	
	public void cancelTrade(TradeObject trade) {
		trades.remove(trade);
		Inventory inv = trade.inv;
		
		for(Player player : trade.spies) {
			player.closeInventory();
			player.sendMessage(Reference.prefix + ChatColor.RED + "The trade you were watching ended");
		}
		
		trade.sender.sendMessage(Reference.prefix + ChatColor.RED + "The trade has been cancelled");
		trade.receiver.sendMessage(Reference.prefix + ChatColor.RED + "The trade has been cancelled");
		trade.sender.closeInventory();
		trade.receiver.closeInventory();
		
		for(int x = 0; x <= 3; x++) {
			for(int y = 0; y <= 2; y++) {
				int z = x + 5;
				if(inv.getItem(iutils.slot(x, y)) != null) trade.sender.getInventory().addItem(inv.getItem(iutils.slot(x, y)));
				if(inv.getItem(iutils.slot(z, y)) != null) trade.receiver.getInventory().addItem(inv.getItem(iutils.slot(z, y)));
			}
		}
	}
	
	public ArrayList<Player> getTradeSpies(){
		ArrayList<Player> spies = new ArrayList<Player>();
		for(TradeObject trade : trades) {
			for(Player player : trade.spies) {
				spies.add(player);
			}
		}
		return spies;
	}
	
	public TradeObject getActiveTradeBySpy(Player spy) {
		for(TradeObject trade : getActiveTrades()) {
			if(trade.spies.contains(spy)) {
				return trade;
			}
		}
		return null;
	}
	
	public ArrayList<Player> getTradeSenders(){
		ArrayList<Player> senders = new ArrayList<Player>();
		for(TradeObject trade : trades) {
			senders.add(trade.sender);
		}
		return senders;
	}
	
	public ArrayList<Player> getTradeReceivers(){
		ArrayList<Player> receivers = new ArrayList<Player>();
		for(TradeObject trade : trades) {
			receivers.add(trade.receiver);
		}
		return receivers;
	}
	
	public ArrayList<Player> getTradeUsers(){
		ArrayList<Player> users = new ArrayList<Player>();
		for(TradeObject trade : getActiveTrades()) {
			users.add(trade.receiver);
			users.add(trade.sender);
		}
		return users;
	}
	
	public TradeObject getTradeBySender(Player sender) {
		for(TradeObject trade : this.trades) {
			if(trade.sender == sender) {
				return trade;
			}
		}
		return null;
	}
	
	public ArrayList<TradeObject> getTradesByReceiver(Player receiver) {
		ArrayList<TradeObject> trades = new ArrayList<TradeObject>();
		for(TradeObject trade : this.trades) {
			if(trade.receiver == receiver) {
				trades.add(trade);
			}
		}
		return trades;
	}
	
	public ArrayList<TradeObject> getActiveTrades(){
		ArrayList<TradeObject> trades = new ArrayList<TradeObject>();
		for(TradeObject trade : this.trades) {
			if(trade.init) {
				trades.add(trade);
			}
		}
		return trades;
	}
	
	public TradeObject getActiveTradeByUser(Player user) {
		for(TradeObject trade : getActiveTrades()) {
			if(trade.sender == user || trade.receiver == user) {
				return trade;
			}
		}
		return null;
	}
	
	public TradeObject getActiveTradeBySender(Player sender) {
		for(TradeObject trade : getActiveTrades()) {
			if(trade.sender == sender) {
				return trade;
			}
		}
		return null;
	}
	
	public TradeObject getActiveTradeByReceiver(Player receiver) {
		for(TradeObject trade : getActiveTrades()) {
			if(trade.receiver == receiver) {
				return trade;
			}
		}
		return null;
	}
	
	public ArrayList<Player> getActiveTradeSenders(){
		ArrayList<Player> senders = new ArrayList<Player>();
		for(TradeObject trade : getActiveTrades()) {
			senders.add(trade.sender);
		}
		return senders;
	}
	
	public ArrayList<Player> getActiveTradeReceivers(){
		ArrayList<Player> receivers = new ArrayList<Player>();
		for(TradeObject trade : getActiveTrades()) {
			receivers.add(trade.receiver);
		}
		return receivers;
	}
	
	public ArrayList<Player> getActiveTradeUsers(){
		ArrayList<Player> users = new ArrayList<Player>();
		for(TradeObject trade : getActiveTrades()) {
			users.add(trade.receiver);
			users.add(trade.sender);
		}
		return users;
	}
	
	public Player getPlayerTradingWith(Player player, TradeObject trade) {
		if(trade.sender == player) return trade.receiver;
		if(trade.receiver == player) return trade.sender;
		return null;
	}
	
	public String getTradingSide(TradeObject trade, Player player) {
		if(trade.sender == player) {
			return "l";
		}else {
			return "r";
		}
	}
}
