//updates in game plants

package com.github.tommyt0mmy.drugdealing.managers;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PlantsUpdater extends BukkitRunnable
{
    private DrugDealing mainClass;

    public PlantsUpdater(DrugDealing mainClass)
    {
        this.mainClass = mainClass;
    }

    @Override
    public void run()
    {
        List<ConfigurationSection> plantsCS = mainClass.plantsreg.getPlants();
        for (ConfigurationSection currentPlant : plantsCS)
        {
            Location currLoc = mainClass.plantsreg.getPlantLocation(currentPlant);
            //playing particle
            currLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, currLoc.getBlockX() + 0.5, currLoc.getBlockY(), currLoc.getBlockZ() + 0.5, 2, 0.3, 0.3, 0.3);
            //check if still planted on farmland
            if (!mainClass.drugs.isPlantedOnFarmland(currLoc.getBlock()))
            {
                mainClass.drugs.destroyPlant(currLoc.getBlock(), true);
            }
            //checking if some plants need to grow up
            if (hasToGrow(currLoc))
            {
                mainClass.drugs.growPlant(currLoc);
            }
        }
    }

    public boolean hasToGrow(Location loc)
    {
        int currentTime = (int) (System.currentTimeMillis() / 1000L);
        int plantsGrowthTime = mainClass.plantsreg.getGrowthTime(loc);
        if (plantsGrowthTime - currentTime <= 0)
        {
            return true;
        }
        return false;
    }
}
