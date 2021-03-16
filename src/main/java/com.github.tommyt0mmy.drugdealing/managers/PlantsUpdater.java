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
    private final DrugDealing plugin = DrugDealing.getInstance();

    @Override
    public void run()
    {
        List<ConfigurationSection> plantsCS = plugin.plantsRegister.getPlants();
        for (ConfigurationSection currentPlant : plantsCS)
        {
            Location currLoc = plugin.plantsRegister.getPlantLocation(currentPlant);
            //playing particle
            currLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, currLoc.getBlockX() + 0.5, currLoc.getBlockY(), currLoc.getBlockZ() + 0.5, 2, 0.3, 0.3, 0.3);
            //check if still planted on farmland
            if (!plugin.drugs.isPlantedOnFarmland(currLoc.getBlock()))
            {
                plugin.drugs.destroyPlant(currLoc.getBlock(), true);
            }
            //checking if some plants need to grow up
            if (hasToGrow(currLoc))
            {
                plugin.drugs.growPlant(currLoc);
            }
        }
    }

    public boolean hasToGrow(Location loc)
    {
        int currentTime = (int) (System.currentTimeMillis() / 1000L);
        int plantsGrowthTime = plugin.plantsRegister.getGrowthTime(loc);
        return plantsGrowthTime - currentTime <= 0;
    }
}
