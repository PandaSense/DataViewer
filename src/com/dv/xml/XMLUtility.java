/*
 * XMLUtility.java  2/6/13 1:04 PM
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author xyma
 */
public class XMLUtility {

    public static Document parseData(String filename) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            DocumentBuilder myDoc = factory.newDocumentBuilder();
            File xmlFile = new File(filename);
            document = myDoc.parse(xmlFile);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }
        return document;
    }

    /**
     * Searches parent node and returns first found matching node.
     *
     * @param node     The parent node
     * @param nodeName The child node's element name
     * @return The first matching child Node
     */
    public static Node getNode(Node node, String nodeName) {
        NodeList ch = node.getChildNodes();
        int l = ch.getLength();
        for (int i = 0; i < l; i++) {
            Node n = ch.item(i);
            if (n.getNodeName().equals(nodeName)) {
                return n;
            }
        }
        return null;
    }

    /**
     * Searches parent node for matching child nodes.
     *
     * @param node     The parent node
     * @param nodeName The child node's element name
     * @return A list of matching child Nodes
     */
    public static Iterator getNodes(Node node, String nodeName) {
        ArrayList nodes = new ArrayList();
        NodeList nl = node.getChildNodes();
        int nll = nl.getLength();
        for (int i = 0; i < nll; i++) {
            Node n = nl.item(i);
            if (n.getNodeName().equals(nodeName)) {
                nodes.add(n);
            }
        }
        return nodes.iterator();
    }

    /**
     * Returns the value of the given attribute.
     *
     * @param node     The current node
     * @param attrName The attribute name
     * @return The attribute's value
     */
    public static String getAttribute(Node node, String attrName) {
        // handle null node
        if (node == null) {
            return null;
        }
        Node attr = node.getAttributes().getNamedItem(attrName);
        if (attr != null) {
            return attr.getNodeValue();
        }
        return null;
    }

    /**
     * Searches parent node 2 levels deep and returns the first match node's
     * value. Useful when caller knows that there is a matching node 2 levels
     * deep.
     *
     * @param node        The parent node
     * @param nodeName    The 1st level child node element name
     * @param subNodeName The 2nd level child node element name
     * @return The child node's value
     */
    public static String getSubNodeValue(Node node, String nodeName, String subNodeName) {
        Node subNode = getNode(node, nodeName);
        if (subNode == null) {
            return null;
        }
        return getNodeValue(subNode, subNodeName);
    }

    /**
     * Searches parent node for matching child nodes, then collects and returns
     * the first match node's value. Useful when caller knows that there is only
     * one child node.
     * @param node     The parent node
     * @return The child node's value
     */
    public static String getNodeValue(Node node) {
        // sometimes jdom use more than one children nodes to represent text node 
        NodeList children = node.getChildNodes();
        String value = "";
        for (int i = 0; i < children.getLength(); i++) {
            value += children.item(i).getNodeValue();
        }
        return value;
    }

    /**
     * Searches parent node for matching child nodes and collects and returns
     * their values.
     *
     * @param node     The parent node
     * @param elemName The matching child node element name
     * @return List of node values
     */
    public static Enumeration getChildrenNodeValues(Node node, String elemName) {
        Vector vect = new Vector();
        NodeList nl = node.getChildNodes();
        int n = nl.getLength();
        for (int i = 0; i < n; i++) {
            Node nd = nl.item(i);
            if (nd.getNodeName().equals(elemName)) {
                Node child = nd.getFirstChild();
                if (child == null) {
                    vect.add("");
                } else {
                    vect.add(child.getNodeValue());
                }
            }
        }
        return vect.elements();
    }

    /**
     * Searches parent node for matching child nodes and collects and returns
     * their matching attribute String values.
     *
     * @param node     The parent node
     * @param elemName The matching child node element name
     * @param attr     The matching child node attribute name
     * @return List of attribute values
     */
    public static Enumeration getChildrenAttributeValues(Node node, String elemName, String attr) {
        Vector vect = new Vector();
        NodeList nl = node.getChildNodes();
        int n = nl.getLength();
        for (int i = 0; i < n; i++) {
            Node nd = nl.item(i);
            if (nd.getNodeName().equals(elemName)) {
                vect.add(getAttribute(nd, attr));
            }
        }
        return vect.elements();
    }

    /**
     * Replace all instances of &lt;, &gt;, &amp;, &apos; &quot; with the XML
     * safe strings.
     *
     * @param string to convert
     * @return converted String
     */
    public static String convertIllegalCharacters(String string) {
        String newString = new String();
        newString = string.replaceAll("<", "&lt;");
        newString = newString.replaceAll(">", "&gt;");
        newString = newString.replaceAll("&", "&amp;");
        newString = newString.replaceAll("'", "&apos;");
        newString = newString.replaceAll("\"", "&quot;");
        return newString;
    }

    public static NodeList getTestCases(String fileName) throws Exception {
        Document document = parseData(fileName);
        return document.getElementsByTagName("testcase");

    }

    /**
     * Searches parent node for matching child nodes, then collects and returns
     * the first match node's value. Useful when caller knows that there is only
     * one child node.
     *
     * @param node     The parent node
     * @param nodeName The child node element name
     * @return The child node's value
     */
    public static String getNodeValue(Node node, String nodeName) {
        Node n = getNode(node, nodeName);
        if (n == null) {
            return null;
        }
        Node ch = n.getFirstChild();
        if (ch != null) {
            return ch.getNodeValue();
        }
        return null;


    }

    /**
     * Method selectNodeByName.
     *
     * @param node
     * @param nodeName
     * @return Node
     *         <p/>
     *         Purpose: given a node & tag name, returns a single node that matches.
     *         Useful when it is known that a particular node will only have a single
     *         node of a particular tag name.
     */
    public static Node selectNodeByName(Node node, String nodeName) {
        Node nodeToReturn = null;
        int i = 0;
        // get all the child nodes
        NodeList currNodes = node.getChildNodes();
        int length = currNodes.getLength();
        // search for an occurence that matches nodeName provided
        while ((i < length) && (nodeToReturn == null)) {
            Node thisNode = currNodes.item(i);
            if (thisNode.getNodeName().equals(nodeName)) {
                nodeToReturn = thisNode;
            }
            i++;
        }
        return nodeToReturn;
    }

    /**
     * Method allNodesByName.
     *
     * @param node
     * @param nodeName
     * @return ArrayList
     *         <p/>
     *         Purpose: given a node & tag name, returns an ArrayList of ALL nodes that
     *         have that tag. Useful when there are multiple nodes of the same. For
     *         example, used to parse an entire XML document into an array of testcases.
     */
    public static ArrayList allNodesByName(Node node, String nodeName) {
        ArrayList nodes = new ArrayList();
        NodeList currNodes = node.getChildNodes();
        int length = currNodes.getLength();
        for (int i = 0; i < length; i++) {
            Node thisNode = currNodes.item(i);
            if (thisNode.getNodeName().equals(nodeName)) {
                nodes.add(thisNode);
            }
        }
        if (nodes.size() > 0) {
            return nodes;
        } else {
            return null;
        }
    }
}
