package com.minecraftdimensions.dynamicmotd.socket;

import java.net.*;
import java.io.*;


import net.md_5.bungee.api.ChatColor;
public class SimpleSocketServer extends Thread {
    
	public static int DEFAULT_PORT = 14444;
	private static int count = 0;
	
    public static SimpleSocketServer startServer() {
    	return startServer(SimpleSocketServer.DEFAULT_PORT);
    }
    public static synchronized SimpleSocketServer startServer(int port) {
        SimpleSocketServer simpleSocketServer = new SimpleSocketServer(port);
        simpleSocketServer.start();
        while (!SimpleSocketServer.isServerRunning()) {
        	if(count==6){
        		System.out.println(ChatColor.RED+"Unable to start socket Bukkit plugin will not function at 100%");
        		return null;
        	}
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	if(count>0){
        	System.out.println(ChatColor.RED+"Re-enabling socket");
        	}
        	count++;
        	System.out.println("Starting socket on port "+ DEFAULT_PORT);
        }
        return simpleSocketServer;
    }

    private int port;
    private static ServerSocket serverSocket = null;
    private static boolean bRunning = false;
    
    public SimpleSocketServer(int port) {
    	super("SimpleSock");
        this.port = port;
    }

    public void run() {

        try {
            serverSocket = new ServerSocket(port);
            bRunning = true;
            while (true) {
                Socket s = serverSocket.accept(); 
                new ServerThread(s).start();
            }
        } 
        catch (IOException e) {
            if (serverSocket != null && serverSocket.isClosed())
            	; //Ignore if closed by stopServer() call
            else
            	e.printStackTrace();
        }
        finally {
        	serverSocket = null;
        	bRunning = false;
        }
    }
    public static void stopServer() {
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
        }
    }
    public static boolean isServerRunning() {
    	return bRunning;
    }
}
