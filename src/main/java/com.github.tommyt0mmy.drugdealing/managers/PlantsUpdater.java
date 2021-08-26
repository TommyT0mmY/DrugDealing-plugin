//updates in game plants

package com.github.tommyt0mmy.drugdealing.managers;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.Optional;

public class PlantsUpdater extends BukkitRunnable
{
    private final DrugDealing instance = DrugDealing.getInstance();

    @Override
    public void run()
    {
        Integer[] plantIds = new Integer[0];
        try
        {
            plantIds = DrugDealing.database.getPlantIds();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        for (int currentPlantId : plantIds)
        {
            Location currLoc = instance.plantsRegister.getPlantLocation(currentPlantId);
            if (currLoc == null)
                continue;

            //playing particle
            Optional<World> worldOptional = Optional.ofNullable(currLoc.getWorld());
            worldOptional.ifPresent(world -> world.spawnParticle(Particle.VILLAGER_HAPPY, currLoc.getBlockX() + 0.5, currLoc.getBlockY(), currLoc.getBlockZ() + 0.5, 2, 0.3, 0.3, 0.3));

            //check if still planted on farmland
            if (!instance.drugs.isPlantedOnFarmland(currLoc.getBlock()))
            {
                instance.drugs.destroyPlant(currLoc.getBlock(), true);
                continue;
            }

            //checking if some plants need to grow up
            if (instance.plantsRegister.isTimeGrown(currLoc))
                instance.drugs.growPlant(currLoc, currentPlantId);
        }
    }
}
