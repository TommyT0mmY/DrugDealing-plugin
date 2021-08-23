package com.github.tommyt0mmy.drugdealing.database;

import com.github.tommyt0mmy.drugdealing.utility.CriminalRole;
import com.github.tommyt0mmy.drugdealing.utility.DrugType;
import com.github.tommyt0mmy.drugdealing.utility.Helper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
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
    protected Connection connection;

    DrugDealingDatabase(String path, String username, String password) throws SQLException
    {
        connection = DriverManager.getConnection(path, username, password);
    }

    public void closeConnection() throws SQLException
    {
        if (!connection.isClosed())
            connection.close();
    }

    public void createNpcTable() throws SQLException
    {
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
    }

    public void createPlantsTable() throws SQLException
    {
        String query =
                "create table if not exists dd_plant_data(" +
                        "id integer not null auto_increment," +
                        "type_id tinyint not null," +
                        "growthtime integer not null," +
                        "x integer not null," +
                        "y integer not null," +
                        "z integer not null," +
                        "world_uuid binary(16) not null," +
                        "primary key (id));";

        Statement statement = connection.createStatement();
        statement.execute(query);
        statement.close();
    }

    public void saveNpc(@NotNull UUID uuid, @NotNull CriminalRole role, @NotNull String name, @NotNull DrugType[] acceptedDrugTypes) throws SQLException
    {
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
        pstmt.close();
    }

    public void removeNpc(UUID uuid) throws SQLException
    {
        PreparedStatement pstmt = connection.prepareStatement("delete from dd_npc_data where uuid = ?;");

        pstmt.setObject(1, Helper.UuidToByte16(uuid), Types.BINARY);

        pstmt.executeUpdate();
        pstmt.close();
    }

    public boolean findNpc(UUID uuid) throws SQLException
    {
        PreparedStatement pstmt = connection.prepareStatement("select exists(select * from dd_npc_data where uuid = ?)");

        pstmt.setObject(1, Helper.UuidToByte16(uuid), Types.BINARY);

        ResultSet rs = pstmt.executeQuery();

        boolean foundNpc = rs.getBoolean(1);

        rs.close();
        pstmt.close();

        return foundNpc;
    }

    public CriminalRole getNpcRole(UUID uuid) throws SQLException
    {
        PreparedStatement pstmt = connection.prepareStatement("select role_id from dd_npc_data where uuid = ?;");

        pstmt.setObject(1, Helper.UuidToByte16(uuid), Types.BINARY);

        ResultSet rs = pstmt.executeQuery();

        byte roleId = rs.getByte(1);

        rs.close();
        pstmt.close();

        return Helper.getCriminalRole(roleId);
    }

    public String getNpcName(UUID uuid) throws SQLException
    {
        PreparedStatement pstmt = connection.prepareStatement("select name from dd_npc_data where uuid = ?;");
        pstmt.setObject(1, Helper.UuidToByte16(uuid), Types.BINARY);
        ResultSet rs = pstmt.executeQuery();

        rs.close();
        pstmt.close();

        return rs.getString(1);
    }

    public ArrayList<DrugType> getNpcAccepted(UUID uuid) throws SQLException
    {
        PreparedStatement pstmt = connection.prepareStatement("select accepts from dd_npc_data where uuid = ?;");

        pstmt.setObject(1, Helper.UuidToByte16(uuid), Types.BINARY);

        ResultSet rs = pstmt.executeQuery();

        short accepts = rs.getShort(1);
        ArrayList<DrugType> result = new ArrayList<>();

        for (DrugType drugType : DrugType.values())
            if ((accepts & (1 << drugType.getId())) > 0)
                result.add(drugType);

        rs.close();
        pstmt.close();

        return result;
    }

    public void savePlant(@NotNull DrugType drugType, int growthTime, @NotNull Location location) throws SQLException
    {
        PreparedStatement pstmt = connection.prepareStatement("insert into dd_plant_data values(null, ?, ?, ?, ?, ?, unhex(replace(?, '-','')));");

        pstmt.setByte(1, (byte) drugType.getId());
        pstmt.setInt(2, growthTime);
        pstmt.setInt(3, location.getBlockX());
        pstmt.setInt(4, location.getBlockY());
        pstmt.setInt(5, location.getBlockZ());
        pstmt.setObject(6, Objects.requireNonNull(location.getWorld()).getUID(), Types.BINARY);

        pstmt.executeUpdate();
        pstmt.close();
    }

    public void removePlant(int id) throws SQLException
    {
        PreparedStatement pstmt = connection.prepareStatement("delete from dd_plant_data where id = ?;");

        pstmt.setInt(1, id);

        pstmt.executeUpdate();
        pstmt.close();
    }

    public boolean findPlant(@NotNull Location loc) throws SQLException
    {
        PreparedStatement pstmt = connection.prepareStatement("select exists(select * from dd_plant_data dpd where x = ? and y = ? and z = ? and world_uuid = ?)");

        UUID worldUuid = Objects.requireNonNull(loc.getWorld()).getUID();

        pstmt.setInt(1, loc.getBlockX());
        pstmt.setInt(2, loc.getBlockY());
        pstmt.setInt(3, loc.getBlockZ());
        pstmt.setObject(4, Helper.UuidToByte16(worldUuid), Types.BINARY);

        ResultSet rs = pstmt.executeQuery();

        boolean foundPlant = rs.getBoolean(1);

        rs.close();
        pstmt.close();

        return foundPlant;
    }

    public int getPlantId(Location location) throws SQLException
    {
        PreparedStatement pstmt = connection.prepareStatement(
                "select id from dd_plant_data where x = ? and y = ? and z = ? and world_uuid = ?;");

        UUID worldUuid = Objects.requireNonNull(location.getWorld()).getUID();

        pstmt.setInt(1, location.getBlockX());
        pstmt.setInt(2, location.getBlockY());
        pstmt.setInt(3, location.getBlockZ());
        pstmt.setObject(4, Helper.UuidToByte16(worldUuid), Types.BINARY); //Using the byte[16] representation of the UUID

        ResultSet rs = pstmt.executeQuery();

        int id = rs.getInt("id");

        rs.close();
        pstmt.close();

        return id;
    }

    public DrugType getPlantType(int id) throws SQLException
    {
        PreparedStatement pstmt = connection.prepareStatement("select type_id from dd_plant_data where id = ?;");
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        byte plantType = rs.getByte(1);

        rs.close();
        pstmt.close();

        return Helper.getDrugType(plantType);
    }

    public int getGrowthtime(int id) throws SQLException
    {
        PreparedStatement pstmt = connection.prepareStatement("select growthtime from dd_plant_data where id = ?;");
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        rs.close();
        pstmt.close();

        return rs.getInt(1);
    }

    public Location getPlantLocation(int id) throws SQLException
    {
        PreparedStatement pstmt = connection.prepareStatement("select x, y, z, world_uuid from dd_plant_data where id = ?;");
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        int x = rs.getInt("x"), y = rs.getInt("y"), z = rs.getInt("z");
        UUID worldUUID = UUID.nameUUIDFromBytes(rs.getBytes("world_uuid"));

        rs.close();
        pstmt.close();

        return new Location(Bukkit.getWorld(worldUUID), x, y, z);
    }
}
