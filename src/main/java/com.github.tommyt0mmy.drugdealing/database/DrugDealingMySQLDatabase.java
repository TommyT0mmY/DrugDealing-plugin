package com.github.tommyt0mmy.drugdealing.database;

import java.sql.SQLException;

public class DrugDealingMySQLDatabase extends DrugDealingDatabase
{
    public DrugDealingMySQLDatabase(String username, String password, String hostname, String port, String database) throws SQLException
    {
        super("jdbc:mysql://" + hostname + ":" + port + "/" + database, username, password);
    }
}
