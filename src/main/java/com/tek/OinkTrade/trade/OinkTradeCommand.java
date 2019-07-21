package com.tek.OinkTrade.trade;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.tek.OinkTrade.Main;
import com.tek.OinkTrade.Reference;

public class OinkTradeCommand implements CommandExecutor{

	Main main;
	
	public OinkTradeCommand(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Reference.prefix + ChatColor.RED + "You must be a player in order to use this");
			return true;
		}
		
		Player player = (Player) sender;
		
		if(args.length == 0) {
			player.sendMessage(ChatColor.BLUE + "- " + ChatColor.GREEN + "" + ChatColor.BOLD + "OinkTrade " + ChatColor.GREEN + "help menu" + ChatColor.BLUE + " -");
			player.sendMessage(ChatColor.GOLD + "/oinktrade send <playername>");
			player.sendMessage(ChatColor.GOLD + "/oinktrade <accept/decline>");
			player.sendMessage(ChatColor.GOLD + "/oinktrade spy <playername>");
			player.sendMessage(ChatColor.GOLD + "/oinktrade info");
			return true;
		}
		
		if(args.length != 2 && args.length != 1) {
			player.sendMessage(Reference.prefix + ChatColor.RED + "Invalid amount of arguments");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("send")) {
			if(Main.hasPermission(player, Reference.Permissions.TRADE.getPermission())) {
				if(args.length == 2) {
					if(main.playerIsOnline(args[1])) {
						Player receiver = Bukkit.getPlayerExact(args[1]);
						if(receiver.getWorld() == player.getWorld()) {
							if(receiver != player) {
								if(!main.getTradeManager().getTradeSenders().contains(player)) {
									main.getTradeManager().startTrade(player, receiver);
								}else {
									player.sendMessage(Reference.prefix + ChatColor.RED + "You already sent a trade request");
								}
							}else {
								player.sendMessage(Reference.prefix + ChatColor.RED + "You can't send a request to yourself");
							}
						}else {
							player.sendMessage(Reference.prefix + ChatColor.RED + "You must be in the same world as the receiver");
						}
					}else {
						player.sendMessage(Reference.prefix + ChatColor.RED + "Player isn't online or doesn't exist");
					}
				}else {
					player.sendMessage(Reference.prefix + ChatColor.RED + "Invalid amount of arguments");
				}
			}else {
				player.sendMessage(Reference.prefix + ChatColor.RED + "You don't have the permissions to do this");
			}
		}
		
		else if(args[0].equalsIgnoreCase("accept")) {
			if(Main.hasPermission(player, Reference.Permissions.TRADE.getPermission())) {
				if(args.length == 1) {
					if(main.getTradeManager().getTradeReceivers().contains(player)) {
						TradeObject newest = main.getTradeManager().getTradesByReceiver(player).get(0);
						if(main.playerIsOnline(newest.sender.getName())){
							if(player.getWorld() == newest.sender.getWorld()) {
								newest.init();
							}else {
								player.sendMessage(Reference.prefix + ChatColor.RED + "You must be in the same world as the receiver");
							}
						}else {
							player.sendMessage(Reference.prefix + ChatColor.RED + "Player isn't online or doesn't exist");
						}
					}else {
						player.sendMessage(Reference.prefix + ChatColor.RED + "You have not received a trade request or it has expired");
					}
				}else {
					player.sendMessage(Reference.prefix + ChatColor.RED + "Invalid amount of arguments");
				}
			}else {
				player.sendMessage(Reference.prefix + ChatColor.RED + "You don't have the permissions to do this");
			}
		}
		
		else if(args[0].equalsIgnoreCase("decline")) {
			if(Main.hasPermission(player, Reference.Permissions.TRADE.getPermission())) {
				if(args.length == 1) {
					if(main.getTradeManager().getTradeReceivers().contains(player)) {
						TradeObject newest = main.getTradeManager().getTradesByReceiver(player).get(0);
						main.getTradeManager().denyTrade(newest);
					}else {
						player.sendMessage(Reference.prefix + ChatColor.RED + "You have not received a trade request or it has expired");
					}
				}else {
					player.sendMessage(Reference.prefix + ChatColor.RED + "Invalid amount of arguments");
				}
			}else {
				player.sendMessage(Reference.prefix + ChatColor.RED + "You don't have the permissions to do this");
			}
		}
		
		else if(args[0].equalsIgnoreCase("info")) {
			player.sendMessage(ChatColor.BLUE + "- " + ChatColor.GREEN + "" + ChatColor.BOLD + "OinkTrade" + ChatColor.BLUE + " -");
			player.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.GRAY + "RedstoneTek aka Link");
			player.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.GRAY + "0.9 SNAPSHOT");
			player.sendMessage(ChatColor.GOLD + "Special Thanks: " + ChatColor.GRAY + "Mobkinz78");
		}
		
		else if(args[0].equalsIgnoreCase("spy")) {
			if(Main.hasPermission(player, Reference.Permissions.SPY.getPermission())) {
				if(args.length == 2) {
					if(main.playerIsOnline(args[1])) {
						if(main.getTradeManager().getActiveTradeUsers().contains(Bukkit.getPlayerExact(args[1]))) {
							TradeObject trade = main.getTradeManager().getActiveTradeByUser(Bukkit.getPlayerExact(args[1]));
							trade.spies.add(player);
							player.openInventory(trade.inv);
						}else {
							player.sendMessage(Reference.prefix + ChatColor.RED + "This player isn't in a trade");
						}
					}else {
						player.sendMessage(Reference.prefix + ChatColor.RED + "Player isn't online or doesn't exist");
					}
				}else {
					player.sendMessage(Reference.prefix + ChatColor.RED + "Invalid amount of arguments");
				}
			}else {
				player.sendMessage(Reference.prefix + ChatColor.RED + "You don't have the permissions to do this");
			}
		}
		
		else {
			player.sendMessage(Reference.prefix + ChatColor.RED + "Unknown subcommand");
		}
		
		return false;
	}
	
	
	
}
