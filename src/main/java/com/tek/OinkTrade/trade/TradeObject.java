package com.tek.OinkTrade.trade;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.tek.OinkTrade.Main;
import com.tek.OinkTrade.Reference;
import com.tek.OinkTrade.utils.InventoryUtils;
import com.tek.OinkTrade.utils.ItemUtil;

public class TradeObject {
	
	public Player sender, receiver;
	public Inventory inv;
	public Main main;
	public InventoryUtils iutils;
	public boolean init = false;
	public Inventory sinv,rinv;
	public boolean sacc = false, racc = false;
	public boolean completed = false;
	
	public ArrayList<Player> spies = new ArrayList<Player>();
	
	public TradeObject(Main main, Player sender, Player receiver) {
		this.sender = sender;
		this.receiver = receiver;
		this.main = main;
		
		this.iutils = new InventoryUtils();
	}
	
	public void init() {
		sender.sendMessage(Reference.prefix + ChatColor.GREEN + receiver.getName() + " has accepted your trade request");
		receiver.sendMessage(Reference.prefix + ChatColor.GREEN + "You have accepted " + sender.getName() + "'s request");
		createInventory();
		displayTrade();
		init = true;
	}
	
	public void createInventory() {
		inv = Bukkit.createInventory(null, 54, ChatColor.BLUE + sender.getName() + ChatColor.GOLD + "" + ChatColor.BOLD + " : " + ChatColor.RED + receiver.getName());
		iutils.fillHorizontal(inv, ItemUtil.createItemStack(Material.RED_STAINED_GLASS_PANE, 1, ChatColor.RED + "Not Accepted"), 4);
		iutils.fillVertical(inv, ItemUtil.createItemStack(Material.IRON_BARS, 1, " "), 4);
		restoreInv();
	}
	
	public void restoreInv() {
		inv.setItem(iutils.slot(0, 5), ItemUtil.createItemStack(Material.GREEN_STAINED_GLASS_PANE, 1, ChatColor.GREEN + "Accept Trade"));
		inv.setItem(iutils.slot(5, 5), ItemUtil.createItemStack(Material.GREEN_STAINED_GLASS_PANE, 1, ChatColor.GREEN + "Accept Trade"));
		inv.setItem(iutils.slot(1, 5), ItemUtil.createItemStack(Material.RED_STAINED_GLASS_PANE, 1, ChatColor.RED + "Deny Trade"));
		inv.setItem(iutils.slot(6, 5), ItemUtil.createItemStack(Material.RED_STAINED_GLASS_PANE, 1, ChatColor.RED + "Deny Trade"));
		inv.setItem(iutils.slot(2, 5), ItemUtil.createItemStack(Material.RED_CONCRETE, 1, ChatColor.DARK_RED + "Exit Trade"));
		inv.setItem(iutils.slot(7, 5), ItemUtil.createItemStack(Material.RED_CONCRETE, 1, ChatColor.DARK_RED + "Exit Trade"));
		inv.setItem(iutils.slot(3, 5), ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
		inv.setItem(iutils.slot(8, 5), ItemUtil.createItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
		
		if(sacc) {
			iutils.fillArea(inv, ItemUtil.createItemStack(Material.LIME_STAINED_GLASS_PANE, 1, ChatColor.GREEN + "Accepted"), 0, 4, 4, 1);
		} else {
			iutils.fillArea(inv, ItemUtil.createItemStack(Material.RED_STAINED_GLASS_PANE, 1, ChatColor.RED + "Not Accepted"), 0, 4, 4, 1);
		}
		
		if(racc) {
			iutils.fillArea(inv, ItemUtil.createItemStack(Material.LIME_STAINED_GLASS_PANE, 1, ChatColor.GREEN + "Accepted"), 5, 4, 4, 1);
		} else {
			iutils.fillArea(inv, ItemUtil.createItemStack(Material.RED_STAINED_GLASS_PANE, 1, ChatColor.RED + "Not Accepted"), 5, 4, 4, 1);
		}
	}
	
	public void updateDisplay(Inventory updated) {
		if(completed) return;
		
		//If he somehow teleported
		if(receiver.getWorld() != sender.getWorld()) {
			completed = true;
			main.getTradeManager().cancelTrade(this);
			return;
		}
		
		inv.setContents(updated.getContents());
		restoreInv();
		
		if(racc == true && sacc == true) {
			if(main.getTradeManager().completeTrade(this)) {
				completed = true;
			}else {
				change();
				completed = false;
			}
		}
		
		iutils.fillVertical(inv, ItemUtil.createItemStack(Material.IRON_BARS, 1, " "), 4);
		
		rinv.setContents(inv.getContents());
		iutils.fillArea(rinv, ItemUtil.createItemStack(Material.BARRIER, 1, " "), 0, 5, 4, 1);
		
		sinv.setContents(inv.getContents());
		iutils.fillArea(sinv, ItemUtil.createItemStack(Material.BARRIER, 1, " "), 5, 5, 4, 1);
	}
	
	public void displayTrade() {
		sinv = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Trading with " + ChatColor.GREEN + ChatColor.BOLD + receiver.getName());
		sinv.setContents(inv.getContents());
		iutils.fillArea(sinv, ItemUtil.createItemStack(Material.BARRIER, 1, " "), 5, 5, 4, 1);
		sinv.setContents(sinv.getContents());
		
		rinv = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Trading with " + ChatColor.GREEN + ChatColor.BOLD + sender.getName());
		rinv.setContents(inv.getContents());
		iutils.fillArea(rinv, ItemUtil.createItemStack(Material.BARRIER, 1, " "), 0, 5, 4, 1);
		rinv.setContents(rinv.getContents());
		
		sender.openInventory(sinv);
		receiver.openInventory(rinv);
	}
	
	public void agree(Player user) {
		if(user == receiver) {
			racc = true;
		}else {
			sacc = true;
		}
	}
	
	public void disagree(Player user) {
		if(user == receiver) {
			racc = false;
		}else {
			sacc = false;
		}
	}
	
	public void change() {
		racc = false;
		sacc = false;
	}
	
}
