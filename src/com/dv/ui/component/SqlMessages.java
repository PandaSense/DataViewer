/*
 * SqlMessages.java  2/6/13 1:04 PM
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

package com.dv.ui.component;

public class SqlMessages {

    // output message types

    /**
     * Indicates a executing message
     */
    public static final int ACTION_MESSAGE = 0;

    /**
     * Indicates an error message
     */
    public static final int ERROR_MESSAGE = 1;

    /**
     * Indicates a normal output message
     */
    public static final int PLAIN_MESSAGE = 2;

    /**
     * Indicates a normal output message
     */
    public static final int WARNING_MESSAGE = 3;

    /**
     * Indicates a executing message
     */
    public static final int ACTION_MESSAGE_PREFORMAT = 4;

    /**
     * Indicates an error message
     */
    public static final int ERROR_MESSAGE_PREFORMAT = 5;

    /**
     * Indicates a normal output message
     */
    public static final int PLAIN_MESSAGE_PREFORMAT = 6;

    /**
     * Indicates a normal output message
     */
    public static final int WARNING_MESSAGE_PREFORMAT = 7;

    /**
     * The string for block comment substitution
     */
    public static final String BLOCK_COMMENT_PLACER = "{block_comment}";

    /**
     * The regex for block comment substitution
     */
    public static final String BLOCK_COMMENT_REGEX = "\\{block_comment\\}";

    /**
     * Indicates text insert mode
     */
    public static final int INSERT_MODE = 0;

    /**
     * Indicates text overwrite mode
     */
    public static final int OVERWRITE_MODE = 1;

    private SqlMessages() {
    }

}
