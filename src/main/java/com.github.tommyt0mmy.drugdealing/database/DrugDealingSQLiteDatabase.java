package com.github.tommyt0mmy.drugdealing.database;

import java.sql.SQLException;
import java.sql.Statement;

public class DrugDealingSQLiteDatabase extends DrugDealingDatabase
{

    public DrugDealingSQLiteDatabase(String uri) throws SQLException
    {
        super("jdbc:sqlite:" + uri, null, null);
    }

    public void createNpcTable() throws SQLException
    {
        String query =
                "create table if not exists dd_npc_data(" +
                        "uuid binary(16) not null," +
                        "role_id tinyint not null," +
                        "name varchar(12) not null," +
                        "accepts smallint not null," +
                        "primary key (uuid));";

        Statement statement = connection.createStatement();
        statement.execute(query);
        statement.close();
    }

    @Override
    public void createPlantsTable() throws SQLException
    {
        String query =
                "create table if not exists dd_plant_data(" +
                        "id integer primary key autoincrement," +
                        "type_id integer not null," +
                        "growthtime integer not null," +
                        "x integer not null," +
                        "y integer not null," +
                        "z integer not null," +
                        "world_uuid binary(16) not null);";

        Statement statement = super.connection.createStatement();
        statement.execute(query);
        statement.close();
    }
}
