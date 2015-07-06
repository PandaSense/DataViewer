/*
 * ContentAssistable.java  2/6/13 1:04 PM
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
package com.dv.search;


/**
 * A component (such as a text field) that supports content assist.
 * Implementations will fire a property change event of type
 * {@link #ASSISTANCE_IMAGE} when content assist is enabled or disabled.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface ContentAssistable {

    /**
     * Property event fired when the image to use when the component is focused
     * changes.  This will either be <code>null</code> for "no image," or
     * a <code>java.awt.Image</code>.
     */
    public static final String ASSISTANCE_IMAGE = "AssistanceImage";


}