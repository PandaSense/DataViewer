
/*
 * SessionSocket.java  8/15/13 1:44 PM
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 
 * @ClassName SessionSocket
 * @Description socket通信会话类,功能是简化scoket通信过程，
 * 用户只需要继承本类实现几个事件处理方法即可完成scoket通信，
 * 使用灵活，可控性强，每个阶段都可以控制，扩展性强，详细使用
 * 方法可以参考demo
 * @author 狂奔的蜗牛 672308444@163.com  
 * @date 2011-9-16 上午10:30:31
*
 */
public abstract class SessionSocket implements Runnable{
	/**
	 * 所有会话列表
	 */
	private static ArrayList<SessionSocket> sessiontList=new ArrayList<SessionSocket>();
	/**
	 * 读取数据使用的缓存区大小,单位KB
	 */
	private int BUFFER_SIZE=5;
	/**
	 * 允许的最大连接数,0不限制
	 */
	private int MAX_THREAD=0;
	/**
	 * 线程退出控制标记
	 */
	private boolean needExit=false;
	/**
	 * 会话注册功能启用禁用标志
	 */
	private boolean REGIST_DISABLED=false;
	/**
	 * 伴随着线程启动的socket
	 */
	private Socket socket;
	/**
	 * 接受socket连接的线程
	 */
	private Thread thread;
	/**
     * 
     * <p>构造方法 </p> 
     * <p>Description 构造方法中接受连接，触发beforeConnected事件</p> 
     * @param socket ：Socket 接受的Socket连接对象
     */
	public SessionSocket(Socket socket){
		this.socket=socket;
		registSession(this);
		beforeConnected(socket);
	}
    /**
     * @Description 接受连接之前触发该方法
     * @param socket ：Socket 接受的Socket连接对象
     * @return 返回类型 void
//     * @throws 抛出的异常类型
     */
    public void beforeConnected(Socket socket){}
	/**
	 * @Description 线程启动之前触发该方法
	 * @param thread : Thread 对应的线程对象
	 * @param socket ：Socket 接受的Socket连接对象
	 * @return 返回类型 void
	 */
    public void beforeThreadStarted(Thread thread,Socket socket){}
	/**
	 * @Description 关闭当前连接并退出当前线程
	 * @return 返回类型 void
	 */
    public void close(){
    	unregistSession(this);
    	onClose(socket, thread);
    	needExit=true;
    	try {
			socket.close();
		} catch (IOException e) {
		}
    }
	/**
	 * @Description 发生通信异常时触发
	 * @param e : Exception 错误异常对象
	 * @param socket ：Socket 接受的Socket连接对象
	 * @param thread : Thread 对应的线程对象
	 * @return 返回类型 void
	 */
    private void errorHandle(Exception e,Socket socket,Thread thread){
    	needExit=true;
    	onError(e, socket, thread);
    }

	/**
	 * @Description 获得读取数据使用的缓存区大小，单位KB
	 * @return 返回类型 int 缓存区大小
	 */
    public int getBUFFER_SIZE() {
		return BUFFER_SIZE;
	}
	
	/**
	 * @Description 获得最大线程数
	 * @return 返回类型 int
	 */
	public int getMAX_THREAD() {
		return MAX_THREAD;
	}
	/**
     * @Description 滤掉不可用的BaseSocket对象,返回有效的已经注册的SessionSocket对象列表，
     * 由于是操作共享数据，该方法用synchronized同步，确保线程安全。
     * @return 返回类型 ArrayList(SessionSocket)
     */
    public synchronized ArrayList<SessionSocket> getSessions(){
  	  for(int i=0;i<sessiontList.size();i++){
  		  if(sessiontList.get(i).socket.isClosed()){
  			  unregistSession(sessiontList.get(i));
  		  }
  	  }
  	  return sessiontList;
    }
	/**
	 * @Description 取得当前连接的socket对象
	 * @return 返回类型 Socket
	 */
    public Socket getSocket() {
		return socket;
	}

	/**
     * @Description 取得当前线程对象
     * @return 返回类型 Thread
     */
	public Thread getThread() {
		return thread;
	}
	
	/**
	 * @Description 注册功能状态
	 * @return 返回类型 boolean true注册功能禁用 false注册功能启用
	 */
	public boolean isREGIST_DISABLED() {
		return REGIST_DISABLED;
	}
	
	/**
	 * @Description 关闭连接时触发
	 * @param socket ：Socket 关闭连接对应的Socket对象
	 * @param thread : Thread 对应的线程对象
	 * @return 返回类型 void
	 */
	public abstract void onClose(Socket socket,Thread thread);
	/**
	 * @Description 接受连接的线程启动后触发
	 * @param socket ：Socket 接受的Socket连接对象
	 * @param thread : Thread 对应的线程对象
	 * @return 返回类型 void
	 */
	public abstract void onConnected(Socket socket,Thread thread);
    /**
	 * @Description 当有数据到达时触发
	 * @param data ：byte[] 字节数组数据
	 * @param socket ：Socket 接受的Socket连接对象
	 * @param thread ：Thread 对应的线程对象
	 * @return 返回类型 void
	 */
	public abstract void onDataArrived(byte[] data,Socket socket,Thread thread);
	/**
	 * @Description 发生通信错误时触发
	 * @param e :Exception 错误异常对象
	 * @param socket : Socket 发生错误的socket
	 * @param thread : Thread 对应的线程对象
	 * @return 返回类型 void
//	 * @throws 抛出的异常类型
	 */
	public abstract void onError(Exception e,Socket socket,Thread thread);
	/**
	 * @Description 到达最大连接数时触发
	 * @param socket ：被拒绝的连接socket对象
	 * @return 返回类型 void
	 */
    public void onMaxThread(Socket socket){}
	/**
	 * @Description 线程退出时触发
	 * @param thread : Thread 对应的线程对象
	 * @param socket ：Socket 接受的Socket连接对象
	 * @return 返回类型 void
	 */
	public void onThreadExit(Thread thread,Socket socket){}
	/**
	 * @Description 线程启动时触发
	 * @param thread : Thread 对应的线程对象
	 * @param socket ：Socket 接受的Socket连接对象
	 * @return 返回类型 void
//	 * @throws 抛出的异常类型
	 */
	public void onThreadStarted(Thread thread,Socket socket){}
	/**
	 * @Description 最大线程数检查，达到最大值则拒绝连接，没有达到最大值就注册连接
	 * @return 返回类型 void
	 */
    private boolean reachMaxThread(){
    	//System.out.println("size:"+sessiontList.size());
    	if(MAX_THREAD==0){return false;}
    	if(sessiontList.size()<=MAX_THREAD){
    		return false;
    		}else {
			return true;
		}
	}
    /**
	 * @Description 从指定的socket中读取一次数据
	 * @param socket : Socket 
	 * @throws java.io.IOException  抛出IO异常,说明网络异常
	 * @return 返回类型 String,即接收到的数据
	 */
	private byte[] reciveMessage(Socket socket) throws IOException {
		BufferedInputStream reciver=new BufferedInputStream(socket.getInputStream());
		byte[] buffer=new byte[getBUFFER_SIZE()*1024*2];//缓存大小，1*1024*1024*2是1M
		int len=reciver.read(buffer);
		if(len>0){
			return new String(buffer,0,len).getBytes();
			}{
				throw new IOException();
			}
	}
    /**
	 * @Description 注册一个SessionSocket，由于是操作共享数据，
	 * 该方法用synchronized同步，确保线程安全。
	 * @param session ：SessionSocket
	 * @return 返回类型 void
	 */
	public synchronized void registSession(SessionSocket session){
		if(!REGIST_DISABLED){
			sessiontList.add(session);
			}
	}
	/**
	 * 线程启动时执行的方法
	 */
    public final void run() {
		onThreadStarted(thread, socket);
		onConnected(socket, thread);
		//线程开始
		while(!needExit){
			try {
				if(socket.isClosed()){onClose(socket, thread);break;}
				byte[] data=reciveMessage(socket);
				onDataArrived(data, socket, thread);
			} catch (IOException e) {
				errorHandle(e, socket, thread);
			}
		}
		threadExitHandle();
		//线程结束
	};
    /**
     * @Description 使用成员socket发送信息
     * @param data : byte[] 要发送的信息
     * @throws java.io.IOException    抛出IO异常,说明网络异常
     * @return 返回类型 void
     */
	public void  sendMessage(byte[] data) throws IOException{
	    	OutputStream sender=socket.getOutputStream();
		    sender.write(data);
			sender.flush();
    };
    /**
     * @Description 使用指定的socket发送信息
     * @param data : byte[] 要发送的信息
     * @param socket ：Socket 发送信息使用的socket
     * @throws java.io.IOException    抛出IO异常,说明网络异常
     * @return 返回类型 void
     */
	public void  sendMessage(byte[] data,Socket socket) throws IOException{
	    	OutputStream sender=socket.getOutputStream();
		    sender.write(data);
			sender.flush();
    };
	/**
     * @Description 设置读取数据使用的缓存区大小，单位KB，默认1024KB即1M(默认)
     * @param bUFFERSIZE ：int 缓存区大小
     * @return 返回类型 void
     */
	public void setBUFFER_SIZE(int bUFFERSIZE) {
		BUFFER_SIZE = bUFFERSIZE;
	};
	/**
	 * @Description 设置最大线程数
	 * @param mAXTHREAD :int
	 * @return 返回类型 void
	 */
	public void setMAX_THREAD(int mAXTHREAD) {
		MAX_THREAD = mAXTHREAD;
	};
    /**
	 * @Description 设置注册功能
	 * @param rEGISTDISABLED : boolean   true禁用注册功能   false启用注册功能(默认)
	 * @return 返回类型 void
	 */
	public void setREGIST_DISABLED(boolean rEGISTDISABLED) {
		REGIST_DISABLED = rEGISTDISABLED;
	}
	/**
	 * @Description 启动线程
	 * @return 返回类型 void
	 */
    public final void start() {
    	//线程数检查
    	if(reachMaxThread()){
    		onMaxThread(socket);
    		close();
    		return;//阻止线程启动
		}
    	thread=new Thread(this);
    	beforeThreadStarted(thread, socket);
    	thread.start();
	}
	/**
     * @Description 线程退出清理
     * @return 返回类型 void
     */
    private void threadExitHandle(){
    	onThreadExit(thread, socket);
    	close();
    }
	/**
	 * @Description 注销一个SessionSocket对象,由于是操作共享数据，
	 * 该方法用synchronized同步，确保线程安全。
	 * @param session ：SessionSocket
	 * @return 返回类型 void
	 */
	public synchronized void unregistSession(SessionSocket session){
		if(!REGIST_DISABLED){
			for (int i=0;i<sessiontList.size();i++) {
				SessionSocket session2=sessiontList.get(i);
				if(session.hashCode()==session2.hashCode()||session.equals(session2)){
					sessiontList.remove(i);
				}
			}
		}
	}
	
}
