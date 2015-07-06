/*
 * DVServerInstance.java  2/6/13 1:04 PM
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

package com.dv.dbinstance;

/**
 * @author Cypress
 */
public class DVServerInstance {

    String[] server = null;
    String host;
    String sid;
    String user;
    String password;
    String schama;
    String instanceName;
    String port;
    String type;

    String security;

    public DVServerInstance(String[] server) {

        this.server = server;
        init();
    }

    public void init() {

        host = server[0];
        sid = server[1];
        user = server[2];
        password = server[3];
        schama = server[4];
        instanceName = server[5];
        port = server[6];
        type = server[7];
//        security=server[8];
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }


    public String getHost() {
        return host.trim();
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSchama() {
        return schama;
    }

    public void setSchama(String schama) {
        this.schama = schama;
    }

    public String getSid() {
        return sid.trim();
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String toString() {
        return getHost() + "," + getSid() + "," + getUser() + "," + getPassword() + "," + getSchama() + ","+getInstanceName()+"," + getPort() + "," + this.getType();
    }

}
