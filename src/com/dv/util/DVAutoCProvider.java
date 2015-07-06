/*
 * DVAutoCProvider.java  2/6/13 1:04 PM
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

import java.util.ResourceBundle;
import java.util.Vector;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;


/**
 * @author xyma
 */
public class DVAutoCProvider {

    private static final String MSG = "com.dv.util.DVAutoShorthandCompletion";
    private static final ResourceBundle mainViewResource = ResourceBundle.getBundle(MSG);

    public static CompletionProvider buildAutoCProvider(Vector tableAndView, String schema) {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();

        for (int i = 0; i < tableAndView.size(); i++) {

            provider.addCompletion(new BasicCompletion(provider, tableAndView.get(i).toString()));

        }
        provider.addCompletion(new ShorthandCompletion(provider, mainViewResource.getString("sel.ShorHand"), mainViewResource.getString("sel.ShorHand.Text") + " " + schema, mainViewResource.getString("sel.ShorHand.Desc") + " " + schema.toUpperCase()));

        return provider;
    }

    public static CompletionProvider buildAutoCProviderForMethod(Vector tableAndView) {

        DefaultCompletionProvider provider = new DefaultCompletionProvider();

        for (int i = 0; i < tableAndView.size(); i++) {

            provider.addCompletion(new BasicCompletion(provider, tableAndView.get(i).toString()));

        }
        provider.addCompletion(new ShorthandCompletion(provider, "sysout", "System.out.println(", "System.out.println("));

        return provider;

    }
}
