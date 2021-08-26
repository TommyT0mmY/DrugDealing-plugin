package com.github.tommyt0mmy.drugdealing.database;

import java.sql.Connection;
import java.sql.SQLException;

public class DrugDealingMySQLDatabase extends DrugDealingDatabase
{
    private ConnectionPoolManager connectionPoolManager;

    public DrugDealingMySQLDatabase(String username, String password, String hostname, String port, String database) throws SQLException
    {
        connectionPoolManager = new ConnectionPoolManager("jdbc:mysql://" + hostname + ":" + port + "/" + database, username, password);
    }

    @Override
    protected Connection getConnection() throws SQLException
    {
        return connectionPoolManager.getConnection();
    }

    @Override
    public void closeDatabase()
    {
        connectionPoolManager.closePool();
    }
}
