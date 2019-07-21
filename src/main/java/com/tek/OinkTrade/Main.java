package com.tek.OinkTrade;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.tek.OinkTrade.trade.OinkTradeCommand;
import com.tek.OinkTrade.trade.TradeListener;
import com.tek.OinkTrade.trade.TradeManager;
import com.tek.OinkTrade.trade.TradeObject;

public class Main extends JavaPlugin{
	
	private Logger log;
	
	TradeManager tradeManager;
	
	@Override
	public void onEnable() {
		log = getLogger();
		
		log.log(Level.INFO, "OinkTrade is initializing!");
		
		this.tradeManager = new TradeManager(this);
		
		try {
			getCommand("oinktrade").setExecutor(new OinkTradeCommand(this));
			
			getServer().getPluginManager().registerEvents(new TradeListener(this), this);
			
			this.tradeManager = new TradeManager(this);
			
			log.log(Level.INFO, "OinkTrade has successfully initialized!");
		}catch(Exception e) {
			log.log(Level.INFO, "OinkTrade has unsuccessfully initialized... Check the error below and report to developer");
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDisable() {
		try {
			if(this.tradeManager != null) {
				for(TradeObject trade : getTradeManager().trades) {
					getTradeManager().cancelTrade(trade);
				}
			}
			
			log.log(Level.INFO, "OinkTrade has successfully uninitialized!");
		}catch(Exception e) {
			log.log(Level.INFO, "OinkTrade has unsuccessfully uninitialized... Check the error below and report to developer");
			e.printStackTrace();
		}
	}
	
	public static void broadcastToServer(Main main, String debug) {
		for(Player player : main.getServer().getOnlinePlayers()) {
			player.sendMessage(debug);
		}
	}
	
	public static boolean hasPermission(Player player, String permission) {
		if(!player.hasPermission(permission)) {
			return false;
		}else {
			return true;
		}
	}
	
	public boolean playerIsOnline(String playername) {
		if(Bukkit.getPlayerExact(playername) == null) return false;
		return true;
	}
	
	public TradeManager getTradeManager() {
		return this.tradeManager;
	}
	
}