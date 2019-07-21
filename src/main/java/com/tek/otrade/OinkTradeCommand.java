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

	private Map<UUID, List<UUID>> requests;
	
	public OinkTradeCommand() {
		requests = new HashMap<UUID, List<UUID>>();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Reference.PREFIX + TextFormatter.color("&cYou must be a player to use this command."));
			return false;
		}
		
		Player p = (Player) sender;
		
		/*
		 * oinktrade help
		 * oinktrade accept
		 * oinktrade deny
		 * oinktrade spy <player>
		 * oinktrade send <player>
		 */
		
		return false;
	}
	
	public Map<UUID, List<UUID>> getRequests() {
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
