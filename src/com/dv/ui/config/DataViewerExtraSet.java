/*
 * DataViewerExtraSet.java  5/7/13 4:36 PM
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

/*
 * Created by JFormDesigner on Tue May 07 16:36:24 CST 2013
 */

package com.dv.ui.config;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;

/**
 * @author Nick
 */
public class DataViewerExtraSet extends JPanel {
    public DataViewerExtraSet() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		databseSynPanel = new JPanel();

		//======== this ========
		setPreferredSize(new Dimension(623, 456));
		setBorder(null);

		//======== databseSynPanel ========
		{
			databseSynPanel.setBorder(new TitledBorder("Backup Setting"));

			GroupLayout databseSynPanelLayout = new GroupLayout(databseSynPanel);
			databseSynPanel.setLayout(databseSynPanelLayout);
			databseSynPanelLayout.setHorizontalGroup(
				databseSynPanelLayout.createParallelGroup()
					.addGap(0, 634, Short.MAX_VALUE)
			);
			databseSynPanelLayout.setVerticalGroup(
				databseSynPanelLayout.createParallelGroup()
					.addGap(0, 444, Short.MAX_VALUE)
			);
		}

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addComponent(databseSynPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addComponent(databseSynPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addContainerGap())
		);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel databseSynPanel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
