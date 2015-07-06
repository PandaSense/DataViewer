/*
 * DataViewerServer.java  8/13/13 5:40 PM
 *
 * Copyright (C) 2012-2013 Nick Ma
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.dv.server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created with IntelliJ IDEA.
 * User: Nick
 * Date: 8/13/13
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataViewerServer {

    private static boolean IS_STOP = false;
    private ServerSocket listener;
    public static int max_thread = 0;

    public static boolean isIS_STOP() {
        return IS_STOP;
    }
    public static void main(String args[]){
//    	   if(args.length>1){max_thread=Integer.valueOf(args[1]);}
        new DataViewerServer().startService(5000);
    }

    public void startService(int port){
        try {

            IS_STOP=false;
            listener=new ServerSocket(port);
            System.err.println("Service started.");
            while (!IS_STOP&&!listener.isClosed()) {
//                new DataViewerServerProxy(listener.accept()).start();
            }
            System.err.println("Service stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
