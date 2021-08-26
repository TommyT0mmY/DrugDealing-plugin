package com.github.tommyt0mmy.drugdealing.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DrugDealingSQLiteDatabase extends DrugDealingDatabase
{
    private String path;

    public DrugDealingSQLiteDatabase(String uri) throws SQLException
    {
        this.path = "jdbc:sqlite:" + uri;
    }

    @Override
    protected Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(path);
    }

    @Override
    public void closeDatabase()
    {

    }

    @Override
    public void createNpcTable() throws SQLException
    {
        Connection connection = getConnection();
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
        connection.close();
    }

    @Override
    public void createPlantsTable() throws SQLException
    {
        Connection connection = getConnection();
        String query =
                "create table if not exists dd_plant_data(" +
                        "id integer primary key autoincrement," +
                        "type_id integer not null," +
                        "growthtime integer not null," +
                        "physically_grown boolean not null," +
                        "x integer not null," +
                        "y integer not null," +
                        "z integer not null," +
                        "world_uuid binary(16) not null);";

        Statement statement = getConnection().createStatement();
        statement.execute(query);

        statement.close();
        connection.close();
    }
}
