package com.minecraftdimensions.dynamicmotd;


import java.util.Map;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;


public class pingListener implements Listener {
	
	@EventHandler
	public void pingEvent(ProxyPingEvent e){
		int players;
		int maxPlayers;
		String motd;
		if(e.getConnection()==null || e.getConnection().getVirtualHost()==null|| e.getConnection().getVirtualHost().getHostName()==null){
			return;
		}
		String host = e.getConnection().getVirtualHost().getHostName();
		ListenerInfo l = e.getConnection().getListener();
		Map<String, String> forcedHosts = l.getForcedHosts();
		if(forcedHosts.values().contains(host)){
			String server = forcedHosts.get(host);
			if(DynamicMOTD.config.getBoolean("Servers."+server+".show_true_players", false)){
				players = DynamicMOTD.proxy.getServerInfo(server).getPlayers().size();
			}else{
				players = DynamicMOTD.proxy.getOnlineCount();
			}
			maxPlayers = l.getMaxPlayers();
//
			if(DynamicMOTD.MOTDS.containsKey(server)){
				motd = DynamicMOTD.MOTDS.get(server);
			}else{
				motd = l.getMotd();
			}
			e.setResponse(new ServerPing(e.getResponse().getProtocolVersion(), e.getResponse().getGameVersion(), motd, players, maxPlayers));
		}
	}
	

}
