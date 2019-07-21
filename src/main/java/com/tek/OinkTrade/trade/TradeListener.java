package com.tek.OinkTrade.trade;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.tek.OinkTrade.Main;
import com.tek.OinkTrade.utils.InventoryUtils;

import org.bukkit.ChatColor;

public class TradeListener implements Listener{
	
	Main main;
	InventoryUtils iutils;
	
	public TradeListener(Main main) {
		this.main = main;
		this.iutils = new InventoryUtils();
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		int slot = event.getSlot();
		
		if(main.getTradeManager().getTradeSpies().contains(player)) {
			event.setCancelled(true);
		}
		
		if(main.getTradeManager().getActiveTradeUsers().contains(player)){
			if(event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
				event.setCancelled(true);
				return;
			}
		}
		
		if(main.getTradeManager().getActiveTradeUsers().contains(player)) {
			if(event.getRawSlot() >= 54) {
				return;
			}
			if(event.getInventory().equals(player.getInventory())) {
				return;
			}
			
			if(event.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)) {
				event.setCancelled(true);
			}
			
			TradeObject trade = main.getTradeManager().getActiveTradeByUser(player);
			String tradeside = main.getTradeManager().getTradingSide(trade, player);
			
			int x = iutils.x(slot);
			int y = iutils.y(slot);
			
			if(tradeside.equalsIgnoreCase("l")) {
				//User is the sender, on the left side
				if(x >= 4) { 
					event.setCancelled(true);
				}
				if(x <= 3) {
					if(y >= 4) {
						if(item.getType().equals(Material.RED_CONCRETE) && item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_RED + "Exit Trade")) {
							main.getTradeManager().cancelTrade(trade);
						}
						if(item.getType().equals(Material.RED_STAINED_GLASS_PANE) && item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Deny Trade")) {
							trade.disagree(player);
							trade.updateDisplay(event.getInventory());
						}
						if(item.getType().equals(Material.GREEN_STAINED_GLASS_PANE) && item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Accept Trade")) {
							trade.agree(player);
							trade.updateDisplay(event.getInventory());
						}
						event.setCancelled(true);
					}
				}
				main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
					public void run() {
						if(!event.isCancelled()) trade.change();
						trade.updateDisplay(event.getInventory());
					}
				}, 1l);
			}else {
				//User is the receiver, on the right side
				if(x <= 4) {
					event.setCancelled(true);
				}
				if(x >= 5) {
					if(y >= 4) {
						if(item.getType().equals(Material.RED_CONCRETE) && item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_RED + "Exit Trade")) {
							main.getTradeManager().cancelTrade(trade);
						}
						if(item.getType().equals(Material.RED_STAINED_GLASS_PANE) && item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Deny Trade")) {
							trade.disagree(player);
							trade.updateDisplay(event.getInventory());
						}
						if(item.getType().equals(Material.GREEN_STAINED_GLASS_PANE) && item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Accept Trade")) {
							trade.agree(player);
							trade.updateDisplay(event.getInventory());
						}
						event.setCancelled(true);
					}
				}
				main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
					public void run() {
						if(!event.isCancelled()) trade.change();
						trade.updateDisplay(event.getInventory());
					}
				}, 1l);
			}
		}
	}
	
	@EventHandler
	public void onEntityDamage(final EntityDamageEvent event) {
		Entity e = event.getEntity();
		if(e instanceof Player) {
			Player player = (Player) e;
			if(main.getTradeManager().getActiveTradeUsers().contains(player)) {
				main.getTradeManager().cancelTrade(main.getTradeManager().getActiveTradeByUser(player));
			}
		}
	}
	
	@EventHandler
	public void onInventoryLeave(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		
		if(main.getTradeManager().getTradeSpies().contains(player)) {
			TradeObject trade = main.getTradeManager().getActiveTradeBySpy(player);
			trade.spies.remove(player);
		}
		
		if(main.getTradeManager().getActiveTradeUsers().contains(player)) {
			if(main.getTradeManager().getTradingSide(main.getTradeManager().getActiveTradeByUser(player), player).equalsIgnoreCase("l")) {
				main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
					public void run() {
						player.openInventory(main.getTradeManager().getActiveTradeByUser(player).sinv);
					}
				}, 1l);
			}else {
				main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
					public void run() {
						player.openInventory(main.getTradeManager().getActiveTradeByUser(player).rinv);
					}
				}, 1l);
			}
		}
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		Player player = (Player) event.getWhoClicked();
		
		if(main.getTradeManager().getActiveTradeUsers().contains(player)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if(main.getTradeManager().getActiveTradeUsers().contains(event.getPlayer())) {
			main.getTradeManager().cancelTrade(main.getTradeManager().getActiveTradeByUser(event.getPlayer()));
		}
	}
	
}
