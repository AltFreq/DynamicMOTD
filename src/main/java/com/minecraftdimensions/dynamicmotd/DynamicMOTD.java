package com.minecraftdimensions.dynamicmotd;

import java.io.File;
import java.util.HashMap;

import com.minecraftdimensions.dynamicmotd.configlibrary.Config;
import com.minecraftdimensions.dynamicmotd.socket.SimpleSocketServer;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

public class DynamicMOTD extends Plugin {
	
	public static ProxyServer proxy;
	public static Config config;
	public static HashMap<String,String> MOTDS = new HashMap<String,String>();
	public static DynamicMOTD instance;
	
	public void onEnable() {
		instance = this;
		proxy =ProxyServer.getInstance();
		setConfig();
		proxy.getPluginManager().registerListener(this, new pingListener());
		proxy.registerChannel("MOTD");
		SimpleSocketServer.startServer();
		
	}


	public void onDisable(){
		SimpleSocketServer.stopServer();
	}
	
	private void setConfig() {
		String configpath = File.separator+"plugins"+File.separator+"DynamicMOTD"+File.separator+"config.yml";
		config = new Config(configpath);
		for(ServerInfo s :proxy.getServers().values()){
			config.getBoolean("Servers."+s.getName()+".TruePlayerCount",true);
		}
	}
}
