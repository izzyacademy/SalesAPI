package com.izzyacademy.sales.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

public class MySQLUtil {

    private static final String DEFAULT_PORT = "3306";
    private static final String DEFAULT_HOST = "mysql-external.mysql56.svc.cluster.local";

    private static final String DEFAULT_USER = "application";
    private static final String DEFAULT_PASS = "db3k4Cc";
    private static final String DEFAULT_DATABASE = "ecommerce";

    private final String hostname;

    private final int port;

    private final String username;

    private final String password;

    private final String database;

    private Connection conn;

    public MySQLUtil()
    {
        final Map<String, String> env = System.getenv();

        final String hostname = env.getOrDefault("MYSQL_HOST", DEFAULT_HOST);
        final String portNumber = env.getOrDefault("MYSQL_PORT", DEFAULT_PORT);
        final String mysqlUser = env.getOrDefault("MYSQL_USER", DEFAULT_USER);
        final String mysqlPass = env.getOrDefault("MYSQL_PASS", DEFAULT_PASS);
        final String mysqlBase = env.getOrDefault("MYSQL_DATABASE", DEFAULT_DATABASE);

        this.hostname = hostname;
        this.port = Integer.parseInt(portNumber);
        this.username = mysqlUser;
        this.password = mysqlPass;
        this.database = mysqlBase;

    }

    public Connection getConnection() {

        String jdbcURL = "jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database;

        try {
            conn = DriverManager.getConnection(jdbcURL, this.username, this.password);

        } catch (Exception e) {

            throw new RuntimeException("Database connection error", e);
        }

        return this.conn;
    }

    public void closeConnection() {

        try {

            this.conn.close();

            this.conn = null;

        } catch (Exception e) {

        }
    }
}
