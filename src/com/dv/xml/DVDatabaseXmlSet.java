/*
 * DVDatabaseXmlSet.java  2/6/13 1:04 PM
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

import java.io.File;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.dv.util.DVLOG;
import com.dv.util.DataViewerDesEncrypter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author xyma
 */
public class DVDatabaseXmlSet {

    private String path = null;//path
    private Element root = null;//root
    private Document doc = null;//document

    public DVDatabaseXmlSet(String fileName) {
        path = fileName;
        doc = XMLUtility.parseData(fileName);
        buildRoot();
    }

    public DVDatabaseXmlSet() {
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

    public void addConnection(String[] arr, String dbtype) {
        Element connection = doc.createElement("database");
        root.appendChild(connection);

        Element connectionChild = doc.createElement("host");
        connectionChild.setTextContent(arr[0]);
        connection.appendChild(connectionChild);

        connectionChild = doc.createElement("sid");
        connectionChild.setTextContent(arr[1]);
        connection.appendChild(connectionChild);

        connectionChild = doc.createElement("user");
        connectionChild.setTextContent(arr[2]);
        connection.appendChild(connectionChild);

        connectionChild = doc.createElement("password");
        connectionChild.setAttribute("encrypted", "true");
        connectionChild.setTextContent(DataViewerDesEncrypter.encrypt(arr[3]));
        connection.appendChild(connectionChild);

        connectionChild = doc.createElement("schema");
        connectionChild.setTextContent(arr[4]);
        connection.appendChild(connectionChild);

        connectionChild = doc.createElement("name");
        connectionChild.setTextContent(arr[5]);
        connection.appendChild(connectionChild);

        connectionChild = doc.createElement("port");
        connectionChild.setTextContent(arr[6]);
        connection.appendChild(connectionChild);


        connectionChild = doc.createElement("type");
        connectionChild.setTextContent(dbtype);
        connection.appendChild(connectionChild);

        buildXmlFile();

        connection = null;
        connectionChild = null;
        DVPropMain.DV_SERVER_INSTANCE.put(arr[5], new DVServerInstance(arr));

    }


    public void removeConnection(String connectionName) {

        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node nd = nl.item(i);
            if (!nd.getNodeName().equals("#text")) {
                NodeList nll = nd.getChildNodes();
                for (int k = 0; k < nll.getLength(); k++) {
                    if (nll.item(k).getNodeName().equals("name")) {
                        if (nll.item(k).getTextContent().trim().equals(connectionName)) {
                            root.removeChild(nll.item(k).getParentNode());
                            buildXmlFile();
                            return;
                        }
                    }
                }
            }
        }
    }

    public void updateConnection(String connectionName, String[] newConnectionArr, String DBtype) {
        removeConnection(connectionName);
        addConnection(newConnectionArr, DBtype);
    }

    public void setDBInstance() {
        NodeList nl = root.getChildNodes();
        String[] arr = new String[8];
        for (int i = 0; i < nl.getLength(); i++) {
            Node nd = nl.item(i);
            if (!nd.getNodeName().equals("#text")) {
                NodeList nll = nd.getChildNodes();
                for (int k = 0; k < nll.getLength(); k++) {
                    if (nll.item(k).getNodeName().equals("host")) {
                        arr[0] = nll.item(k).getTextContent();
                    } else if (nll.item(k).getNodeName().equals("sid")) {
                        arr[1] = nll.item(k).getTextContent();
                    } else if (nll.item(k).getNodeName().equals("user")) {
                        arr[2] = nll.item(k).getTextContent();
                    } else if (nll.item(k).getNodeName().equals("password")) {
                        if (DVPropMain.DV_ENCRYPTED_DATABASE_PASSWORD.equals("0")) {
                            arr[3] = nll.item(k).getTextContent();
                        } else {
                            arr[3] = DataViewerDesEncrypter.decrypt(nll.item(k).getTextContent());
                        }
                    } else if (nll.item(k).getNodeName().equals("schema")) {
                        arr[4] = nll.item(k).getTextContent();
                    } else if (nll.item(k).getNodeName().equals("name")) {
                        arr[5] = nll.item(k).getTextContent();
                    } else if (nll.item(k).getNodeName().equals("port")) {
                        arr[6] = nll.item(k).getTextContent();
                    } else if (nll.item(k).getNodeName().equals("type")) {
                        arr[7] = nll.item(k).getTextContent();
                    }
//                    else if(nll.item(k).getNodeName().equals("security")){
//                        if(nll.item(k).getTextContent()==null||nll.item(k).getTextContent().equals("")){
//                            arr[8] = "";
//                        }else{
//                            arr[8] = nll.item(k).getTextContent();
//                        }
//                    }
                }
            }
            DVPropMain.DV_SERVER_INSTANCE.put(arr[5], new DVServerInstance(arr));
        }
    }

    public Vector<String> getDVRegServerNameList() {
        NodeList nl = root.getChildNodes();
        Vector dvPoolsNameList_ = new Vector();
        dvPoolsNameList_.addElement("Create new DV");
        for (int i = 0; i < nl.getLength(); i++) {
            Node nd = nl.item(i);
            if (!nd.getNodeName().equals("#text")) {
                NodeList nll = nd.getChildNodes();
                for (int k = 0; k < nll.getLength(); k++) {
                    if (nll.item(k).getNodeName().equals("name")) {
                        dvPoolsNameList_.addElement(nll.item(k).getTextContent());
                    }
                }
            }
        }
        return dvPoolsNameList_;
    }

    public boolean checksSecurityField() {
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node nd = nl.item(i);
            if (!nd.getNodeName().equals("#text")) {
                NodeList nll = nd.getChildNodes();
                for (int k = 0; k < nll.getLength(); k++) {
                    if (nll.item(k).getNodeName().equals("security")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void resetDBXML(){
        if(!checksSecurityField()){



        }
    }

    public void buildXmlFile() {
        removeBlankTextNode();
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
            DVLOG.setErrorLog(DVDatabaseXmlSet.class.getName(), e);
        } catch (TransformerException e) {
            DVLOG.setErrorLog(DVDatabaseXmlSet.class.getName(), e);
        }
    }

    private void removeBlankTextNode() {
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node nd = nl.item(i);
            if (nd.getNodeName().equals("#text")) {
                root.removeChild(nd);
            }
        }
    }

    public void setEncryptedPassWord() {
        DVPropMain.editorMain.put("DV_ENCRYPTED_DATABASE_PASSWORD", "1");
        Vector<String> serverNames = getDVRegServerNameList();
        DVServerInstance instance;
        if (serverNames.size() > 1) {
            buildNewXml();
            for (int i = 1; i < serverNames.size(); i++) {
                instance = DVPropMain.DV_SERVER_INSTANCE.get(serverNames.get(i));
                addConnection(instance.toString().split(","), instance.getType());
            }
            buildXmlFile();
        }
    }

    public void removeAllDatabaseNodes() {
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node nd = nl.item(i);
            if (nd.getNodeName().equals("database")) {
                root.removeChild(nd);
            }
        }
    }

    public void buildNewXml() {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            root = doc.createElement("dvdatabases");
            doc.appendChild(root);
        } catch (ParserConfigurationException e) {
            DVLOG.setErrorLog(DVDatabaseXmlSet.class.getName(), e);
        }
    }
}
