package drugdealing.managers;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import drugdealing.Main;

public class PlantsUpdater extends BukkitRunnable {
	private Main mainClass;
	public PlantsUpdater(Main mainClass) {
		this.mainClass = mainClass;
	}
	
	@Override
	public void run() {
		for (ConfigurationSection currentPlant : mainClass.plantsreg.getPlants()) {
			Location currLoc = mainClass.plantsreg.getPlantLocation(currentPlant);
			//playing particle
			currLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, currLoc.getBlockX() + 0.5, currLoc.getBlockY(), currLoc.getBlockZ() + 0.5, 2, 0.3, 0.3, 0.3);				
			//checking if some plants need to grow up
			if (hasToGrow(currLoc)) {
				mainClass.drugs.growPlant(currLoc);
			}
		}
	}
	
	public boolean hasToGrow(Location loc) {
		int currentTime = (int) (System.currentTimeMillis() / 1000L);
		int plantsGrowthTime = mainClass.plantsreg.getGrowthTime(loc);
		if (plantsGrowthTime - currentTime <= 0) {
			return true;
		}
		return false;
	}
}
