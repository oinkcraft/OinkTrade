package com.tek.otrade;

import java.util.logging.Level;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.tek.otrade.OinkTradeCommand.OinkTradeCompleter;
import com.tek.rcore.RedstoneCore;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	private static Main instance;
	
	private Economy economy;
	private RedstoneCore redstoneCore;
	
	@Override
	public void onEnable() {
		instance = this;
		
		if(getServer().getPluginManager().getPlugin("Vault") != null) {
			RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
			if(rsp != null) {
				economy = rsp.getProvider();
			} else {
				getLogger().log(Level.SEVERE, "Couldn't find a valid economy, disabled plugin.");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		} else {
			getLogger().log(Level.SEVERE, "Vault is missing, disabled plugin.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		if(getServer().getPluginManager().getPlugin("RedstoneCore") != null) {
			redstoneCore = (RedstoneCore) getServer().getPluginManager().getPlugin("RedstoneCore");
		} else {
			getLogger().log(Level.SEVERE, "RedstoneCore is missing, disabled plugin.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		getCommand("oinktrade").setExecutor(new OinkTradeCommand());
		getCommand("oinktrade").setTabCompleter(new OinkTradeCompleter());
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public Economy getEconomy() {
		return economy;
	}
	
	public RedstoneCore getRedstoneCore() {
		return redstoneCore;
	}
	
}