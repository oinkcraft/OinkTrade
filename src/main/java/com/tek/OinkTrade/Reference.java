package com.tek.OinkTrade;

import org.bukkit.ChatColor;

public class Reference {
	
	public static String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "OinkTrade" + ChatColor.DARK_GRAY + "]: ";
	
	public static enum Permissions{
		TRADE("oinktrade.trade"),
		SPY("oinktrade.spy");
		
		final String permission;
		
		private Permissions(String permission) {
			this.permission = permission;
		}
		
		public String getPermission() {
			return this.permission;
		}
	}
}
