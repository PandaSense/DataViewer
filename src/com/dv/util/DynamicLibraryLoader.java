/*
 * DynamicLibraryLoader.java  5/29/13 3:53 PM
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

package com.dv.util;

import java.net.URL;
import java.net.URLClassLoader;

public class DynamicLibraryLoader extends URLClassLoader {

    private ClassLoader parent = null;

    public DynamicLibraryLoader(URL[] urls) {
        super(urls, ClassLoader.getSystemClassLoader());
        parent = ClassLoader.getSystemClassLoader();
    }

    public Class<?> loadLibrary(String clazz)
            throws ClassNotFoundException {
        return loadClass(clazz, true);
    }

    public Class<?> loadLibrary(String clazz, boolean resolve)
            throws ClassNotFoundException {
        return loadClass(clazz, resolve);
    }

    protected synchronized Class<?> loadClass(String classname, boolean resolve)
            throws ClassNotFoundException {

        Class<?> theClass = findLoadedClass(classname);

        if (theClass != null) {
            return theClass;
        }

        try {

            theClass = findBaseClass(classname);

        } catch (ClassNotFoundException cnfe) {

            theClass = findClass(classname);
        }

        if (resolve) {
            resolveClass(theClass);
        }

        return theClass;

    }

    private Class<?> findBaseClass(String name) throws ClassNotFoundException {

        if (parent == null) {
            return findSystemClass(name);
        } else {
            return parent.loadClass(name);
        }

    }

}