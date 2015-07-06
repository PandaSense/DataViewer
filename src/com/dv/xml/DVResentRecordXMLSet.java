/*
 * DVResentRecordXMLSet.java  2/6/13 1:04 PM
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

import com.dv.dbinstance.DVServerInstance;
import com.dv.prop.DVPropMain;
import com.dv.util.DateFormatFactory;
import com.dv.util.DvRegServerPools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author xyma
 */
public class DVResentRecordXMLSet {

    private String path = null;//path
    private Element root = null;//root
    private Document doc = null;//document

    public DVResentRecordXMLSet(String fileName) {
        path = fileName;
        doc = XMLUtility.parseData(fileName);
        buildRoot();
    }

    public DVResentRecordXMLSet() {
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

    public void addResentRecord() {

        for (int i = 1; i < DVPropMain.DV_RECENT_INSTANCE_NAME.size() + 1; i++) {

            String fname = DVPropMain.DV_RECENT_INSTANCE_NAME.get(String.valueOf(i)).trim();

            Element connection = doc.createElement("resentRecord");
            root.appendChild(connection);

            Element connectionChild = doc.createElement("Index");
            connectionChild.setTextContent(String.valueOf(i));
            connection.appendChild(connectionChild);

            connectionChild = doc.createElement("serverName");
            connectionChild.setTextContent(fname);
            connection.appendChild(connectionChild);

            connectionChild = doc.createElement("sqlFileFullPath");
            connectionChild.setTextContent(getDV_RECENT_INSTANCE_SQLFILEFULLPATH(fname));
            connection.appendChild(connectionChild);

            connectionChild = doc.createElement("count");
            connectionChild.setTextContent(DVPropMain.DV_RECENT_INSTANCE_COUNT.get(fname));
            connection.appendChild(connectionChild);

            connectionChild = doc.createElement("lastUpdateTime");
            connectionChild.setTextContent(DVPropMain.DV_RECENT_INSTANCE_LASTTIME.get(fname));
            connection.appendChild(connectionChild);

            connectionChild = doc.createElement("lastExecuteSqlLine");
            connectionChild.setTextContent(getSQLLine(fname));
            connection.appendChild(connectionChild);

            connection = null;
            connectionChild = null;
        }
    }

    public String getSQLLine(String key) {
        String line = null;
        try {
            line = DVPropMain.DV_RECENT_INSTANCE_LAST_EXECUTE_SQL_LINE.get(key);
            if (line.equals("")) {
                line = "-99";
            }
        } catch (Exception eee) {
            return "-99";
        }

        return line;
    }

    private String getDV_RECENT_INSTANCE_SQLFILEFULLPATH(String fname) {

        String propvValue;
        try {
            propvValue = DVPropMain.DV_RECENT_INSTANCE_SQLFILEFULLPATH.get(fname);
        } catch (Exception e) {
            return "";
        }
        return propvValue;
    }

    public void buildNewRecentXML() {

        buildNewXml();
        addResentRecord();
        buildXmlFile();
    }

    public void setRecentRecords() {
        NodeList nl = root.getChildNodes();
        String index = null;
        String serverName = null;
        for (int i = 0; i < nl.getLength(); i++) {
            Node nd = nl.item(i);
            if (!nd.getNodeName().equals("#text")) {
                NodeList nll = nd.getChildNodes();
                for (int k = 0; k < nll.getLength(); k++) {
                    if (nll.item(k).getNodeName().equals("Index")) {
                        index = nll.item(k).getTextContent().trim();
                    } else if (nll.item(k).getNodeName().equals("serverName")) {
                        DVPropMain.DV_RECENT_INSTANCE_SEQ.put(nll.item(k).getTextContent().trim(), String.valueOf(index));
                        DVPropMain.DV_RECENT_INSTANCE_NAME.put(String.valueOf(index), nll.item(k).getTextContent().trim());
                        serverName = nll.item(k).getTextContent().trim();
                    } else if (nll.item(k).getNodeName().equals("sqlFileFullPath")) {
                        DVPropMain.DV_RECENT_INSTANCE_SQLFILEFULLPATH.put(serverName, nll.item(k).getTextContent().trim());
                    } else if (nll.item(k).getNodeName().equals("count")) {
                        DVPropMain.DV_RECENT_INSTANCE_COUNT.put(serverName, nll.item(k).getTextContent().trim());
                    } else if (nll.item(k).getNodeName().equals("lastUpdateTime")) {
                        DVPropMain.DV_RECENT_INSTANCE_LASTTIME.put(serverName, nll.item(k).getTextContent().trim());
                    } else if (nll.item(k).getNodeName().equals("lastExecuteSqlLine")) {
                        DVPropMain.DV_RECENT_INSTANCE_LAST_EXECUTE_SQL_LINE.put(serverName, nll.item(k).getTextContent().trim());
                    }
                }
            }
        }

    }

    public void buildXmlFile() {

        TransformerFactory tfactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = tfactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path));
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        }
    }

    public void buildNewXml() {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            root = doc.createElement("dvResentRecords");
            doc.appendChild(root);
            path = DVPropMain.DV_CONFIG_FOLDER + "dvrecentserver.xml";
            File xmlFile = new File(path);
            if (xmlFile.exists()) {
                xmlFile.delete();
            }

        } catch (ParserConfigurationException e) {
        }
    }
}
