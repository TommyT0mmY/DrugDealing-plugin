package com.github.tommyt0mmy.drugdealing.database;

import com.github.tommyt0mmy.drugdealing.utility.CriminalRole;
import com.github.tommyt0mmy.drugdealing.utility.DrugType;
import com.github.tommyt0mmy.drugdealing.utility.Helper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public abstract class DrugDealingDatabase
{
    protected abstract Connection getConnection() throws SQLException;

    public abstract void closeDatabase();

    protected void close(Connection conn, PreparedStatement pstmt, ResultSet rs)
    {
        try
        {
            if (rs != null && !rs.isClosed())
                rs.close();
        } catch (SQLException ignored) {}
        try
        {
            if (pstmt != null && !pstmt.isClosed())
                pstmt.close();
        } catch (SQLException ignored) {}
        try
        {
            if (conn != null && !conn.isClosed())
                conn.close();
        } catch (SQLException ignored) {}
    }

    public void createNpcTable() throws SQLException
    {
        Connection connection = getConnection();
        String query =
                "create table if not exists dd_npc_data(" +
                        "uuid binary(16) not null," +
                        "role_id tinyint not null," +
                        "name varchar(12) not null," +
                        "accepts smallint not null," + //"smallint" in mysql is only 2 bytes! this method works if there are less than 16 drug types
                        "primary key (uuid));";

        Statement statement = connection.createStatement();
        statement.execute(query);

        statement.close();
        connection.close();
    }

    public void createPlantsTable() throws SQLException
    {
        Connection connection = getConnection();
        String query =
                "create table if not exists dd_plant_data(" +
                        "id integer not null auto_increment," +
                        "type_id tinyint not null," +
                        "growthtime integer not null," +
                        "physically_grown boolean not null," +
                        "x integer not null," +
                        "y integer not null," +
                        "z integer not null," +
                        "world_uuid binary(16) not null," +
                        "primary key (id));";

        Statement statement = connection.createStatement();
        statement.execute(query);

        statement.close();
        connection.close();
    }

    public void saveNpc(@NotNull UUID uuid, @NotNull CriminalRole role, @NotNull String name, @NotNull DrugType[] acceptedDrugTypes) throws SQLException
    {
        Connection connection = getConnection();
        //this method works if there are less than 32 drug types.
        int accepted = 0;
        for (DrugType drugType : acceptedDrugTypes)
            accepted |= (1 << drugType.getId());

        PreparedStatement pstmt = connection.prepareStatement("insert into dd_npc_data values(?, ?, ?, ?);");
        pstmt.setObject(1, Helper.UuidToByte16(uuid), Types.BINARY);
        pstmt.setByte(2, (byte) role.getId());
        pstmt.setString(3, name);
        pstmt.setShort(4, (short) accepted);

        pstmt.executeUpdate();

        close(connection, pstmt, null);
    }

    public void removeNpc(UUID uuid) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("delete from dd_npc_data where uuid = ?;");

        pstmt.setObject(1, Helper.UuidToByte16(uuid), Types.BINARY);

        pstmt.executeUpdate();

        close(connection, pstmt, null);
    }

    public boolean findNpc(UUID uuid) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("select exists(select * from dd_npc_data where uuid = ?)");

        pstmt.setObject(1, Helper.UuidToByte16(uuid), Types.BINARY);

        ResultSet rs = pstmt.executeQuery();

        boolean foundNpc = rs.getBoolean(1);

        close(connection, pstmt, rs);

        return foundNpc;
    }

    public CriminalRole getNpcRole(UUID uuid) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("select role_id from dd_npc_data where uuid = ?;");

        pstmt.setObject(1, Helper.UuidToByte16(uuid), Types.BINARY);

        ResultSet rs = pstmt.executeQuery();

        byte roleId = rs.getByte(1);

        close(connection, pstmt, rs);

        return Helper.getCriminalRole(roleId);
    }

    public String getNpcName(UUID uuid) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("select name from dd_npc_data where uuid = ?;");
        pstmt.setObject(1, Helper.UuidToByte16(uuid), Types.BINARY);
        ResultSet rs = pstmt.executeQuery();

        close(connection, pstmt, rs);

        return rs.getString(1);
    }

    public ArrayList<DrugType> getNpcAccepted(UUID uuid) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("select accepts from dd_npc_data where uuid = ?;");

        pstmt.setObject(1, Helper.UuidToByte16(uuid), Types.BINARY);

        ResultSet rs = pstmt.executeQuery();

        short accepts = rs.getShort(1);
        ArrayList<DrugType> result = new ArrayList<>();

        for (DrugType drugType : DrugType.values())
            if ((accepts & (1 << drugType.getId())) > 0)
                result.add(drugType);

        close(connection, pstmt, rs);

        return result;
    }

    public void savePlant(@NotNull DrugType drugType, int growthTime, @NotNull Location location) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("insert into dd_plant_data values(null, ?, ?, ?, ?, ?, ?, ?);");

        UUID worldUuid = Objects.requireNonNull(location.getWorld()).getUID();

        pstmt.setByte(1, (byte) drugType.getId());      //type_id
        pstmt.setInt(2, growthTime);                    //growth_time
        pstmt.setBoolean(3, false);                   //physically_grown
        pstmt.setInt(4, location.getBlockX());          //x
        pstmt.setInt(5, location.getBlockY());          //y
        pstmt.setInt(6, location.getBlockZ());          //z
        pstmt.setObject(7, Helper.UuidToByte16(worldUuid), Types.BINARY);

        pstmt.executeUpdate();

        close(connection, pstmt, null);
    }

    public void removePlant(int id) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("delete from dd_plant_data where id = ?;");

        pstmt.setInt(1, id);

        pstmt.executeUpdate();

        close(connection, pstmt, null);
    }

    public boolean findPlant(@NotNull Location loc) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("select exists(select * from dd_plant_data dpd where x = ? and y = ? and z = ? and world_uuid = ?)");

        UUID worldUuid = Objects.requireNonNull(loc.getWorld()).getUID();

        pstmt.setInt(1, loc.getBlockX());
        pstmt.setInt(2, loc.getBlockY());
        pstmt.setInt(3, loc.getBlockZ());
        pstmt.setObject(4, Helper.UuidToByte16(worldUuid), Types.BINARY);

        ResultSet rs = pstmt.executeQuery();

        boolean foundPlant = rs.getBoolean(1);

        close(connection, pstmt, rs);

        return foundPlant;
    }

    public int getPlantId(Location location) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement(
                "SELECT id FROM dd_plant_data WHERE x = ? AND y = ? AND z = ? AND world_uuid = ?;");

        UUID worldUuid = Objects.requireNonNull(location.getWorld()).getUID();

        pstmt.setInt(1, location.getBlockX());
        pstmt.setInt(2, location.getBlockY());
        pstmt.setInt(3, location.getBlockZ());
        pstmt.setObject(4, Helper.UuidToByte16(worldUuid), Types.BINARY); //Using the byte[16] representation of the UUID

        ResultSet rs = pstmt.executeQuery();

        int id = rs.getInt("id");

        close(connection, pstmt, rs);

        return id;
    }

    public DrugType getPlantType(int id) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("select type_id from dd_plant_data where id = ?;");
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        byte plantType = rs.getByte(1);

        close(connection, pstmt, rs);

        return Helper.getDrugType(plantType);
    }

    public int getGrowthtime(int id) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("select growthtime from dd_plant_data where id = ?;");
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        int growthTime = rs.getInt(1);

        close(connection, pstmt, rs);

        return growthTime;
    }

    public boolean getPhysicallyGrown(int id) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("select physically_grown from dd_plant_data where id = ?");
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        boolean pysicallyGrown = rs.getBoolean(1);

        close(connection, pstmt, rs);

        return pysicallyGrown;
    }

    public void setPhysicallyGrown(int id, boolean physicallyGrown) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("update dd_plant_data set physically_grown = ? where id = ?");

        pstmt.setBoolean(1, physicallyGrown);
        pstmt.setInt(2, id);

        pstmt.executeUpdate();

        close(connection, pstmt, null);

    }

    public Location getPlantLocation(int id) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("select x, y, z, world_uuid from dd_plant_data where id = ?;");
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        int x = rs.getInt("x"), y = rs.getInt("y"), z = rs.getInt("z");

        byte[] uuidBytes = rs.getBytes("world_uuid");
        UUID worldUuid = Helper.byte16ToUuid(uuidBytes);

        close(connection, pstmt, rs);

        return new Location(Bukkit.getWorld(worldUuid), x, y, z);
    }

    //todo this is a temporary fix until the new updater is getting implemented
    public Integer[] getPlantIds() throws SQLException
    {
        Connection connection = getConnection();
        ArrayList<Integer> resultsAL = new ArrayList<>();

        String query = "select id from dd_plant_data";

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        while (rs.next())
        {
            resultsAL.add(rs.getInt(1));
        }

        Integer[] results = resultsAL.toArray(new Integer[0]);

        rs.close();
        statement.close();
        close(connection, null, null);

        return results;
    }
}
