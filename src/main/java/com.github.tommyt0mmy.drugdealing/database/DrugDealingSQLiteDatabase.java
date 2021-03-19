package com.github.tommyt0mmy.drugdealing.database;

import java.sql.SQLException;

public class DrugDealingSQLiteDatabase extends DrugDealingDatabase
{

    public DrugDealingSQLiteDatabase(String uri) throws SQLException
    {
        super("jdbc::sqlite://" + uri, null, null);
    }
}
