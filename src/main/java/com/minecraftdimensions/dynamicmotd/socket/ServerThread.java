package com.minecraftdimensions.dynamicmotd.socket;

import java.net.*;
import java.util.logging.Logger;
import java.io.*;

import com.minecraftdimensions.dynamicmotd.DynamicMOTD;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
/*
 * Very simple socket server example. That responds to a single object with
 * another object. The
 */
public class ServerThread extends Thread {

	private Logger jdkLogger = Logger.getLogger(this.getClass().getName());
    private Socket socket = null;
	private int port;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
    	jdkLogger.fine("Run");
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Object input = null;
            Object output = null;
            port = in.readInt();
            
            input = in.readObject();
            jdkLogger.fine(" Input: " + input);
            ServerInfo s = getServer(new InetSocketAddress(socket.getInetAddress(), port));
            DynamicMOTD.MOTDS.put(s.getName(), (String) input);
            
            output = "Updated MOTD"; 
            jdkLogger.fine("Output: " + input);

            out.writeObject(output);
            
            out.close();
            in.close();
            socket.close();
            jdkLogger.fine("Complete");

        } catch (IOException e) {
        	jdkLogger.severe("run() error: " + e.toString());
        } catch (ClassNotFoundException e) {
        	jdkLogger.severe("run() error: " + e.toString());
		}
    }

	private ServerInfo getServer(InetSocketAddress inetSocketAddress) {
		for(ServerInfo s: ProxyServer.getInstance().getServers().values()){
			if(s.getAddress().equals(inetSocketAddress)){
				return s;
			}
		}
		return null;
	}
}
