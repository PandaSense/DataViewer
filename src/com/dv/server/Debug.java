/*
 * Debug.java  8/15/13 1:44 PM
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

/**
 * @Title Debug.java
 * @Package com.test
 * @Description 用一句话描述该文件做什么
 * @author 狂奔的蜗牛 672308444@163.com  
 * @date 2011-9-13 下午11:54:01
 * @version V1.0  
*/
package com.dv.server;

/**
 * @ClassName Debug
 * @Description 这里用一句话描述这个类的作用
 * @author 狂奔的蜗牛 672308444@163.com  
 * @date 2011-9-13 下午11:54:01
 *
 */
public class Debug {
    public static void print(Object message) {
		System.err.println(message);
	}
    public static void info(Object message){
    	System.out.println(message);
    }
}
