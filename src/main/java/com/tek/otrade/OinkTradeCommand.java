package com.tek.otrade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.tek.rcore.misc.TextFormatter;

public class OinkTradeCommand implements CommandExecutor {

	private Map<UUID, UUID> requests;
	
	public OinkTradeCommand() {
		requests = new HashMap<UUID, UUID>();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Reference.PREFIX + TextFormatter.color("&cYou must be a player to use this command."));
			return false;
		}
		
		Player p = (Player) sender;
		
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("help")) {
				String helpMenu = "&7/oinktrade help &9- &6Displays the help menu.\n"
								+ "&7/oinktrade accept &9- &6Accepts the current trading request.\n"
								+ "&7/oinktrade deny &9- &6Denies the current trading request.\n"
								+ "&7/oinktrade send <player> &9- &6Sends a trading request to someone.\n";
				
				if(p.hasPermission(Reference.PERMISSION_SPY)) helpMenu += "&7/oinktrade spy <player> &9- &6Spies on a trading request.";
				
				p.sendMessage(TextFormatter.color(helpMenu));
			} 
			
			else if(args[0].equalsIgnoreCase("accept")) {
				
			}
			
			else if(args[0].equalsIgnoreCase("deny") || args[0].equalsIgnoreCase("decline")) {
				
			}
			
			else {
				p.sendMessage(Reference.PREFIX + Reference.INVALID_SYNTAX);
			}
		} else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("send")) {
				
			}
			
			else if(args[0].equalsIgnoreCase("spy")) {
				
			}
			
			else {
				p.sendMessage(Reference.PREFIX + Reference.INVALID_SYNTAX);
			}
		} else {
			p.sendMessage(Reference.PREFIX + Reference.INVALID_SYNTAX);
		}
		
		return false;
	}
	
	public Map<UUID, UUID> getRequests() {
		return requests;
	}
	
	public static class OinkTradeCompleter implements TabCompleter {

		@Override
		public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				
				if(args.length == 1) {
					return Arrays.asList("help", "accept", "deny", "decline", "send", "spy").stream()
							.filter(str -> str.toLowerCase().startsWith(args[0].toLowerCase()))
							.collect(Collectors.toList());
				}
				
				if(args.length == 2) {
					if(args[0].equalsIgnoreCase("send") || args[0].equalsIgnoreCase("spy")) {
						return Bukkit.getOnlinePlayers().stream()
								.filter(player -> !player.equals(p))
								.map(Player::getName)
								.filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
								.collect(Collectors.toList());
					}
				}
			}
			
			return Arrays.asList();
		}
		
	}

}
