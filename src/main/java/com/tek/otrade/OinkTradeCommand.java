package com.tek.otrade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
				if(p.hasPermission(Reference.PERMISSION_TRADE)) {
					if(requests.containsKey(p.getUniqueId())) {
						UUID otherUUID = requests.get(p.getUniqueId());
						OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(otherUUID);
						if(oPlayer.isOnline()) {
							if(p.getWorld().equals(oPlayer.getPlayer().getWorld())) {
								/* TODO START TRADE */
							} else {
								p.sendMessage(Reference.PREFIX + TextFormatter.color("&cYou must be in the same world to trade."));
							}
						} else {
							requests.remove(p.getUniqueId());
							p.sendMessage(Reference.PREFIX + TextFormatter.color("&cThis player is no longer online."));
						}
					} else {
						p.sendMessage(Reference.PREFIX + TextFormatter.color("&cYou have no trading requests on hold."));
					}
				} else {
					p.sendMessage(Reference.PREFIX + Reference.NO_PERMISSIONS);
				}
			}
			
			else if(args[0].equalsIgnoreCase("deny") || args[0].equalsIgnoreCase("decline")) {
				if(p.hasPermission(Reference.PERMISSION_TRADE)) {
					if(requests.containsKey(p.getUniqueId())) {
						OfflinePlayer player = Bukkit.getOfflinePlayer(requests.get(p.getUniqueId()));
						p.sendMessage(Reference.PREFIX + TextFormatter.color("&aDenied &6" + player.getName() + "&a's trading request."));
						if(player.isOnline())
							player.getPlayer().sendMessage(Reference.PREFIX + TextFormatter.color("&6" + p.getName() + "&c has denied your trading request."));
						requests.remove(p.getUniqueId());
					} else {
						p.sendMessage(Reference.PREFIX + TextFormatter.color("&cYou have no trading requests on hold."));
					}
				} else {
					p.sendMessage(Reference.PREFIX + Reference.NO_PERMISSIONS);
				}
			}
			
			else {
				p.sendMessage(Reference.PREFIX + Reference.INVALID_SYNTAX);
			}
		} else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("send")) {
				if(p.hasPermission(Reference.PERMISSION_TRADE)) {
					String playerName = args[1];
					Optional<Player> playerOpt = Bukkit.getOnlinePlayers().stream()
							.filter(player -> player.getName().equalsIgnoreCase(playerName))
							.map(player -> (Player)player)
							.findFirst();
					if(playerOpt.isPresent()) {
						if(!playerOpt.get().equals(p)) {
							if(playerOpt.get().getWorld().equals(p.getWorld())) {
								if(!requests.containsKey(playerOpt.get().getUniqueId()) || !requests.get(playerOpt.get().getUniqueId()).equals(p.getUniqueId())) {
									requests.put(playerOpt.get().getUniqueId(), p.getUniqueId());
									p.sendMessage(Reference.PREFIX + TextFormatter.color("&aTrading request sent to &6" + playerOpt.get().getName() + "&a."));
									playerOpt.get().sendMessage(Reference.PREFIX + TextFormatter.color("&6" + p.getName() + " &ahas sent you a trading request."));
									playerOpt.get().sendMessage(Reference.PREFIX + TextFormatter.color("&9Use &6/oinktrade accept &9to begin trading."));
									playerOpt.get().sendMessage(Reference.PREFIX + TextFormatter.color("&c&oNote that the request will expire in 30 seconds."));
									
									Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
										if(requests.containsKey(playerOpt.get().getUniqueId()) && requests.get(playerOpt.get().getUniqueId()).equals(p.getUniqueId())) {
											requests.remove(playerOpt.get().getUniqueId());
											if(p.isOnline())
												p.sendMessage(Reference.PREFIX + TextFormatter.color("&cYour trading request to &6" + playerOpt.get().getName() + " &chas expired."));
											if(playerOpt.get().isOnline())
												playerOpt.get().sendMessage(Reference.PREFIX + TextFormatter.color("&cThe request from &6" + p.getName() + " &chas expired."));
										}
									}, 20 * 30);
								} else {
									p.sendMessage(Reference.PREFIX + TextFormatter.color("&cYou have already sent a request to this person."));
								}
							} else {
								p.sendMessage(Reference.PREFIX + TextFormatter.color("&cYou must be in the same world to trade."));
							}
						} else {
							p.sendMessage(Reference.PREFIX + TextFormatter.color("&cYou cannot trade with yourself."));
						}
					} else {
						p.sendMessage(Reference.PREFIX + TextFormatter.color("&cNo player was found by that name."));
					}
				} else {
					p.sendMessage(Reference.PREFIX + Reference.NO_PERMISSIONS);
				}
			}
			
			else if(args[0].equalsIgnoreCase("spy")) {
				if(p.hasPermission(Reference.PERMISSION_SPY)) {
					/* TODO ADD LOGIC */
				} else {
					p.sendMessage(Reference.PREFIX + Reference.NO_PERMISSIONS);
				}
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
