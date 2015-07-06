/*
 * DataViewer.java  2/6/13 1:04 PM
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

package com.dv.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import javax.swing.*;

public class DataViewer {
	
	public static final String USER_HOME_PATH = System.getProperty("user.home")
			+ "/.dataviewer/";
	public static String USER_HOME_UPGRADE_FOLDER = USER_HOME_PATH + "upgrade/";

	public static void main(String args[]) {
		DataViewer dv = new DataViewer();
		dv.doUpgradeFile();
		DataViewer.launchApplication();
	}

	private boolean doUpgradeFile() {
		File upgrade = new File(USER_HOME_UPGRADE_FOLDER);

		if (!upgrade.exists()) {
			return false;
		}
		File[] upgrades = upgrade.listFiles();

		if (upgrades.length > 2) {
			for (int i = 0; i < upgrades.length; i++) {
				if (!upgrades[i].getName().equals("path")) {
					copy(USER_HOME_UPGRADE_FOLDER + upgrades[i].getName(),
							read(USER_HOME_UPGRADE_FOLDER + "path")
									+ upgrades[i].getName());
				}
			}
			deleteUpgradeFiles(upgrades);
		}
		return true;
	}

	private void deleteUpgradeFiles(File[] upgrades) {
		try {
			for (int i = 0; i < upgrades.length; i++) {
				if (!upgrades[i].getName().equals("path")) {
					upgrades[i].delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * reads the specified file and returns the content as a StringBuffer
	 *
	 * @param filename
	 * @return content of the file as a StringBuffer
	 */

	private String read(String filename) {
		String content = null;
		try {
			// Get a file channel for the file
			File file = new File(filename);
			FileInputStream input = new FileInputStream(file);

			byte[] result = new byte[(int) file.length()];

			final int length = result.length;
			int offset = 0;
			long byte_read = 0;
			while (byte_read != -1 && offset < file.length()) {
				try {
					byte_read = input.read(result, offset, length - offset);
					if (byte_read >= 0) // bytesRead == -1 when end-of-file is
										// reached
					{
						offset += byte_read;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
					System.out.println(e1.getMessage());
				}
			}

			// copy the result into content to return
			// this is because we must have result declared inside the try/catch
			// clause
			content = new String(result);
			input.close(); // close file
		} catch (Exception e) {
			System.out.println(e.getCause());
			System.out.println(e.getClass());
			System.out.println(e.getMessage());
		}

		return content;
	}

	/**
	 * Copies src file to dst file. If the dst file does not exist, it is
	 * created. Locking is put onto the dst file during the copy. Uses the
	 * java.nio.channel package to implement the locking
	 *
	 * @return <Code>true</Code> if the copy had no exception<br>
	 *         <Code>false</Code> if the copoy had exception
	 */
	private boolean copy(String src_filename, String dst_filename) {
		try {
			// Create channel on the source
			File src_file = new File(src_filename);
			FileChannel srcChannel = new FileInputStream(src_file).getChannel();
			// Create channel on the destination
			@SuppressWarnings("unused")
			File dst_file = new File(dst_filename);
			// create a overwrite channel (with false)
			FileChannel dstChannel = new FileOutputStream(dst_filename, false)
					.getChannel();
			// FileLock lock = dstChannel.lock();
			try {
				// Copy file contents from source to destination
				dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
				// Close the channels
				srcChannel.close();
				dstChannel.close();
			} finally {
				// lock.release();
			}
		} catch (IOException e) {
		} catch (Exception e) {
		}
		return true;
	}

	private static void launchApplication() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					String full = DataViewer.class.getProtectionDomain()
							.getCodeSource().getLocation().toString();
					full = full.substring(0, full.lastIndexOf("/") + 1);
					URL url = new URL(full + "DataViewer.jar");
					URL[] urlA = new URL[] { url };
					URLClassLoader urlLoader = new URLClassLoader(urlA);
					urlLoader.loadClass("com.dv.ui.DVFrame").newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
