package com.github.tommyt0mmy.drugdealing.database;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolManager
{
    private final DrugDealing instance = DrugDealing.getInstance();

    private HikariDataSource dataSource;

    private String path;
    private String username;
    private String password;

    private int minConnections;
    private int maxConnections;
    private long connectionTimeout;
    private String testQuery;

    public ConnectionPoolManager(String path, String username, String password)
    {
        this.path = path;
        this.username = username;
        this.password = password;

        setupPool();
    }

    private void setupPool()
    {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(path);
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setUsername(username);
        config.setPassword(password);
        config.setMinimumIdle(minConnections);
        config.setMaximumPoolSize(maxConnections);
        config.setConnectionTimeout(connectionTimeout);
        config.setConnectionTestQuery(testQuery);

        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException
    {
        return dataSource.getConnection();
    }

    public void closePool()
    {
        if (dataSource != null && !dataSource.isClosed())
            dataSource.close();
    }
}
