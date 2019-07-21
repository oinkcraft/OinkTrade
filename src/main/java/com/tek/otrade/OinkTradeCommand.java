package com.tek.otrade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
				String helpMenu = "\n"
								+ "&8/oinktrade help &9- &6Displays the help menu.\n"
								+ "&8/oinktrade accept &9- &6Accepts the current trading request.\n"
								+ "&8/oinktrade deny &9- &6Denies the current trading request.\n"
								+ "&8/oinktrade send <player> &9- &6Sends a trading request to someone.\n";
				
				if(p.hasPermission(Reference.PERMISSION_SPY)) helpMenu += "&8/oinktrade spy <player> &9- &6Spies on a trading request.\n";
				
				p.sendMessage(helpMenu);
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
			}
			
			return Arrays.asList();
		}
		
	}

}
