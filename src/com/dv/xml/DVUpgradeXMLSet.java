/*
 * DVUpgradeXMLSet.java  8/2/13 10:57 AM
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

package com.dv.xml;

import com.dv.prop.DVPropMain;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created with IntelliJ IDEA.
 * User: xyma
 * Date: 8/2/13
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class DVUpgradeXMLSet {

    private String path = null;//path
    private Element root = null;//root
    private Document doc = null;//document

    public DVUpgradeXMLSet(String fileName) {
        path = fileName;
        doc = XMLUtility.parseData(fileName);
        buildRoot();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public Element getRoot() {
        return root;
    }

    public void setRoot(Element root) {
        this.root = root;
    }

    private void buildRoot() {
        root = doc.getDocumentElement();
    }

    public void setUpgradeFileDetail() {
        NodeList nl = root.getChildNodes();
        String fileName = null;
        String location = null;
        for (int i = 0; i < nl.getLength(); i++) {
            Node nd = nl.item(i);
            if (!nd.getNodeName().equals("#text")) {
                NodeList nll = nd.getChildNodes();
                for (int k = 0; k < nll.getLength(); k++) {
                    if (nll.item(k).getNodeName().equals("file")) {
                        fileName = nll.item(k).getTextContent().trim();
                        DVPropMain.DV_UPGRADE_FILES.add(fileName);
                    } else if (nll.item(k).getNodeName().equals("location")) {
                        location = nll.item(k).getTextContent().trim();
                    }
                    DVPropMain.DV_UPGRADE_FILES_DETAIL.put(fileName,location);
                }
            }
        }
    }
}
