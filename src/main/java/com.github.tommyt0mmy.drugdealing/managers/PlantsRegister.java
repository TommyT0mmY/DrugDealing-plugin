package com.github.tommyt0mmy.drugdealing.managers;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.DrugType;
import com.github.tommyt0mmy.drugdealing.utility.Helper;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Random;

public class PlantsRegister
{
    private final DrugDealing instance = DrugDealing.getInstance();
    private Random randomGenerator;

    public PlantsRegister()
    {
        randomGenerator = new Random();
    }

    private int randomGrowthTime()
    {
        int minTime = instance.settings.getFileConfiguration().getInt("growthTimeMin");
        int maxTime = instance.settings.getFileConfiguration().getInt("growthTimeMax");
        int randomTime = randomGenerator.nextInt(maxTime) + minTime;
        return Helper.getCurrentTimeSeconds() + randomTime;
    }

    public void addPlant(Location location, DrugType plantType)
    {
        try
        {
            DrugDealing.database.savePlant(plantType, randomGrowthTime(), location);
        } catch (SQLException e)
        {
            instance.console.severe("Couldn't store plant data to database");
            e.printStackTrace();
        }
    }

    public void removePlant(Location location)
    {
        try
        {
            int plantId = getPlantId(location);
            DrugDealing.database.removePlant(plantId);
        } catch (SQLException e)
        {
            instance.console.severe("Couldn't remove plant data from database");
            e.printStackTrace();
        }
    }

    public DrugType getDrugType(@NotNull Location location)
    {
        try
        {
            int plantId = getPlantId(location);
            return DrugDealing.database.getPlantType(plantId);
        } catch (SQLException e)
        {
            instance.console.severe("Couldn't get plant type from database");
            e.printStackTrace();
            return null;
        }
    }

    public int getGrowthTime(@NotNull Location location)
    {
        try
        {
            int plantId = getPlantId(location);
            return DrugDealing.database.getGrowthtime(plantId);
        } catch (SQLException e)
        {
            instance.console.severe("Couldn't get plant growth time from database");
            e.printStackTrace();
            return 0;
        }
    }

    public boolean isTimeGrown(@NotNull Location loc)
    {
        int growthTime = getGrowthTime(loc);
        int currentTime = Helper.getCurrentTimeSeconds();

        return growthTime - currentTime <= 0;
    }

    public boolean isPhysicallyGrown(@NotNull Location loc)
    {
        try
        {
            return DrugDealing.database.getPhysicallyGrown(getPlantId(loc));
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public void setPhysicallyGrown(int plantId, boolean physicallyGrown)
    {
        try
        {
            DrugDealing.database.setPhysicallyGrown(plantId, physicallyGrown);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public boolean isDrugPlant(Location loc)
    {
        try
        {
            return DrugDealing.database.findPlant(loc);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public Location getPlantLocation(int plantId)
    {
        try
        {
            return DrugDealing.database.getPlantLocation(plantId);
        } catch (SQLException e)
        {
            instance.console.severe("Couldn't get plant location from database");
            e.printStackTrace();
        }
        return null;
    }

    public int getPlantId(@NotNull Location location)
    {
        try
        {
            return DrugDealing.database.getPlantId(location);
        } catch (SQLException e)
        {
            instance.console.severe("Couldn't get plant id from database");
            instance.console.severe(location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " " + location.getWorld().getUID().toString());
            e.printStackTrace();
            return 0;
        }
    }
}
