package com.dv.util;

/**
 * Created with IntelliJ IDEA.
 * User: Cypress
 * Date: 9/1/14
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class DVQueryType {

    public static final int ALL_UPDATES = 80;

    /** An SQL INSERT statement */
    public static final int INSERT = 80;

    /** An SQL UPDATE statement */
    public static final int UPDATE = 81;

    /** An SQL DELETE statement */
    public static final int DELETE = 82;

    /** An SQL SELECT statement */
    public static final int SELECT = 10;

    /** A DESCRIBE statement - table meta data */
    public static final int DESCRIBE = 16;

    /** An SQL EXPLAIN statement */
    public static final int EXPLAIN = 15;

    /** An SQL EXECUTE statement (procedure) */
    public static final int EXECUTE = 11;

    /** An SQL DROP TABLE statement */
    public static final int DROP_TABLE = 20;

    /** An SQL CREATE TABLE statement */
    public static final int CREATE_TABLE = 21;

    /** An SQL ALTER TABLE statement */
    public static final int ALTER_TABLE = 22;

    /** An SQL CREATE SEQUENCE statement */
    public static final int CREATE_SEQUENCE = 23;

    /** An SQL CREATE FUNCTION statement */
    public static final int CREATE_FUNCTION = 26;

    /** An SQL CREATE PROCEDURE statement */
    public static final int CREATE_PROCEDURE = 25;

    /** An SQL GRANT statement */
    public static final int GRANT = 27;

    /** An SQL GRANT statement */
    public static final int CREATE_SYNONYM = 28;

    /** An unknown SQL statement */
    public static final int UNKNOWN = 99;

    /** A commit statement */
    public static final int COMMIT = 12;

    /** A rollback statement */
    public static final int ROLLBACK = 13;

    /** A connect statement */
    public static final int CONNECT = 14;

    /** A SQL SELECT ... INTO ... statement */
    public static final int SELECT_INTO = 17;

    /** show table */
    public static final int SHOW_TABLES = 30;


    public static boolean isNotSelectSQL(String sql){

        boolean isSelect = false;

        String query = sql.replaceAll("\n", " ").toUpperCase();

        if (query.indexOf("SELECT ") == 0 && query.indexOf(" INTO ") != -1) {

            isSelect = false;

        } else if (query.indexOf("SELECT ") == 0) {

            isSelect = false;

        } else if (query.indexOf("INSERT ") == 0) {

            isSelect =true;

        } else if (query.indexOf("UPDATE ") == 0) {

            isSelect = true;

        } else if (query.indexOf("DELETE ") == 0) {

            isSelect = true;

        } else if (query.indexOf("CREATE TABLE ") == 0) {

            isSelect =true;

        } else if (query.indexOf("CREATE ") == 0 && (query.indexOf("PROCEDURE ") != -1 ||
                query.indexOf("PACKAGE ") != -1)) {

            isSelect = true;

        } else if (query.indexOf("CREATE ") == 0 && query.indexOf("FUNCTION ") != -1) {

            isSelect = true;

        } else if (query.indexOf("DROP TABLE ") == 0) {

            isSelect = true;

        } else if (query.indexOf("ALTER TABLE ") == 0) {

            isSelect = true;

        } else if (query.indexOf("CREATE SEQUENCE ") == 0) {

            isSelect = true;

        } else if (query.indexOf("CREATE SYNONYM ") == 0) {

            isSelect = true;

        } else if (query.indexOf("GRANT ") == 0) {

            isSelect = true;

        } else if (query.indexOf("EXECUTE ") == 0 || query.indexOf("CALL ") == 0) {

            isSelect = true;

        } else if (query.indexOf("COMMIT") == 0) {

            isSelect = true;

        } else if (query.indexOf("ROLLBACK") == 0) {

            isSelect = true;

        } else if(query.indexOf("EXPLAIN ") == 0) {

            isSelect = false;

        } else if(query.indexOf("DESC ") == 0 || query.indexOf("DESCRIBE ") == 0) {

            isSelect = true;

        } else if (query.indexOf("SHOW TABLES") == 0) {

            isSelect = false;

        }
        return isSelect;
    }

}
