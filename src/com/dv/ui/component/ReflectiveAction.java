/*
 * ReflectiveAction.java  6/7/13 2:31 PM
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

import java.awt.event.ActionEvent;
import java.lang.reflect.Method;

import javax.swing.AbstractAction;

/**
 * @author Takis Diakoumis
 * @version $Revision: 1460 $
 * @date $Date: 2009-01-25 11:06:46 +1100 (Sun, 25 Jan 2009) $
 */
public class ReflectiveAction extends AbstractAction {

    private Object target;

    public ReflectiveAction() {
    }

    public ReflectiveAction(Object target) {
        this.target = target;
    }

    public final void actionPerformed(ActionEvent event) {

        preActionPerformed(event);

        String command = event.getActionCommand();

        try {

            Class<?>[] argTypes = {event.getClass()};

            Method method = null;

            if (target == null) {

                target = this;
                method = getClass().getMethod(command, argTypes);

            } else {

                method = target.getClass().getMethod(command, argTypes);
            }

            if (method == null) {

                return;
            }

            Object[] args = {event};

            method.invoke(target, args);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

        postActionPerformed(event);
    }

    public final Object getTarget() {

        return target;
    }

    public final void setTarget(Object target) {

        this.target = target;
    }

    protected void postActionPerformed(ActionEvent e) {

        // default do nothing
    }

    protected void preActionPerformed(ActionEvent e) {

        // default do nothing
    }

}
