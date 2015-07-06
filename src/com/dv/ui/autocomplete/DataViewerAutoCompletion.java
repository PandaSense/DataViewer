/*
 * DataViewerAutoCompletion.java  11/8/13 5:28 PM
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

package com.dv.ui.autocomplete;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;

/**
 * Created with IntelliJ IDEA.
 * User: xyma
 * Date: 11/8/13
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataViewerAutoCompletion extends AutoCompletion {

    public DataViewerAutoCompletion(CompletionProvider provider){
        super(provider);
        this.setListCellRenderer(new DataViewerACListCellRender());

    }
}
