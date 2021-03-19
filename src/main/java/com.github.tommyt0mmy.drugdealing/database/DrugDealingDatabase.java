package com.github.tommyt0mmy.drugdealing.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DrugDealingDatabase
{
    private Connection connection;

    DrugDealingDatabase(String path, String username, String password) throws SQLException
    {
        connection = DriverManager.getConnection(path, username, password);
    }
}
