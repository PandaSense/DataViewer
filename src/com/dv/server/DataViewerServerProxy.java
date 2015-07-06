package com.dv.server;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Nick
 * Date: 8/13/13
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataViewerServerProxy extends SessionSocket {

    private static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataViewerServerProxy(Socket socket){
        super(socket);
    }

    public static String getCurrentDateTimeString() {
        Date current = new Date();
        String dateString = dateformat.format(current);
        return "[ "+dateString+" ] ";
    }


    @Override
    public void onDataArrived(byte[] data, Socket socket, Thread thread) {
        Debug.print(getCurrentDateTimeString()+"Notice : Message get.socketID: "+socket.hashCode());
        Debug.print(getCurrentDateTimeString()+"Message Contents :"+new String(data));
        try {
            sendMessageToAll(data);
        } catch (Exception e) {
            //e.printStackTrace();
//			Debug.info(e.getMessage());
        }

    }
    public void sendMessageToAll(byte[] message) {
        ArrayList<SessionSocket> sessions=getSessions();
        for (int i=0;i<sessions.size();i++) {
            DataViewerServerProxy session=(DataViewerServerProxy)sessions.get(i);
            try {
                sendMessage(message,session.getSocket());
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    @Override
    public void beforeConnected(Socket socket) {
//        if(ServerListener.max_thread>0)setMAX_THREAD(ServerListener.max_thread);
        Debug.print("================================\n"+getCurrentDateTimeString()+"New Connected before Like Below : ");
    }

    @Override
    public void beforeThreadStarted(Thread thread, Socket socket) {
        Debug.print(getCurrentDateTimeString()+"Before Thread Started Thread ID+thread.getId()");
    }

    @Override
    public void onClose(Socket socket, Thread thread) {
        Debug.print(getCurrentDateTimeString()+"Notice Connected close.socketID:"+socket.hashCode());

    }


    @Override
    public void onConnected(Socket socket, Thread thread) {
        Debug.print(getCurrentDateTimeString()+"Notice Connected successfully :socketID:"+socket.hashCode());

    }
    @Override
    public void onError(Exception e, Socket socket, Thread thread) {
        Debug.print(getCurrentDateTimeString()+"Notice Connected Exception .socketID:"+socket.hashCode());

    }

    @Override
    public void onMaxThread(Socket socket) {
        try {
            sendMessage("Max connections reached!".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Debug.info(getCurrentDateTimeString()+"Notice : Max Threads Reached ,Current break socketID"+socket.hashCode());
    }

    @Override
    public void onThreadExit(Thread thread, Socket socket) {
        Debug.print(getCurrentDateTimeString()+"Notice : Thread quit .Thread ID +thread.getId()");

    }

    @Override
    public void onThreadStarted(Thread thread, Socket socket) {
        Debug.print(getCurrentDateTimeString()+"Notice : Thread start .Thread ID+thread.getId()");
    }

}