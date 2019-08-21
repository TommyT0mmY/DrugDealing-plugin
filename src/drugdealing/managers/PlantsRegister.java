//Handles the plants.yml file

package drugdealing.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import drugdealing.Main;

public class PlantsRegister {
	
	private Main mainClass;
	public PlantsRegister(Main mainClass) {
		this.mainClass = mainClass;
		loadRegister();
	}
	
	private File registerConfigFile;
	private FileConfiguration registerConfig;
	
    private void loadRegister() { //loading plants.yml
    	registerConfigFile = new File(mainClass.datafolder, "plants.yml");
        if (!registerConfigFile.exists()) {
        	registerConfigFile.getParentFile().mkdirs();
            mainClass.saveResource("plants.yml", false);
            mainClass.console.info("Created file plants.yml");
         }

        registerConfig = new YamlConfiguration();
        try {
        	registerConfig.load(registerConfigFile);
        } catch (Exception e) {
            mainClass.console.severe("Couldn't load plants.yml file properly!");
        }
    }
    
    private int randomGrowthTime() {
    	int currentTime = (int) (System.currentTimeMillis() / 1000L);
    	int minTime = mainClass.configs.getConfigs().getInt("growthTimeMin");
    	int maxTime = mainClass.configs.getConfigs().getInt("growthTimeMax");
    	Random randomGenerator = new Random();
    	int randomTime = randomGenerator.nextInt(maxTime) + minTime;
    	return (int) (currentTime + randomTime);
    }
    
    public void addPlant(Block plant, String type) {
    	Location loc = plant.getLocation();
    	String plantName = "plants.plant" + registerConfig.getInt("plantsNumber");
    	int newPlantsNumber = registerConfig.getInt("plantsNumber") + 1;
    	registerConfig.set(".plantsNumber", newPlantsNumber);
    	registerConfig.set(plantName + ".type", type);
    	registerConfig.set(plantName + ".x", loc.getBlockX());
    	registerConfig.set(plantName + ".y", loc.getBlockY());
    	registerConfig.set(plantName + ".z", loc.getBlockZ());
    	registerConfig.set(plantName + ".world", loc.getWorld().getName());
    	registerConfig.set(plantName + ".grown", false);
    	registerConfig.set(plantName + ".growthTime", randomGrowthTime());
    	
    	//saving plants.yml
		try {
			registerConfig.save(registerConfigFile);
		} catch (IOException e) {mainClass.console.severe("Couldn't save plants.yml file properly!");}
    	return;
    }
    
    public boolean removePlant(Location loc) {
    	String plantName = getPlantPath(loc);
    	if (plantName != "") {
    		registerConfig.set(plantName, null);
    		
    		//saving plants.yml
    		try {
				registerConfig.save(registerConfigFile);
			} catch (IOException e) {mainClass.console.severe("Couldn't save plants.yml file properly!");}
    		return true;
    	}
    	return false;
    }

    public String getType(Location loc) {
    	String type = "";
    	String plantPath = getPlantPath(loc);
    	if (plantPath != null) {
    		type = (String) registerConfig.get(plantPath + ".type");
    	}
    	return type;
    }
    
    public int getGrowthTime(Location loc) {
    	int growthTime = 0;
    	String plantPath = getPlantPath(loc);
    	if (plantPath != null) {
    		growthTime = registerConfig.getInt(plantPath + ".growthTime");
    	}
    	return growthTime;
    }
    
    
	public boolean isDrugPlant(Location loc) {
		if (getPlantPath(loc) != "") {
			return true;
		}
		return false;
	}
	
	private String getPlantPath(Location loc) { //given a location returns the path of the plant in plants.ynl file
		int plantsCount = registerConfig.getInt("plantsNumber");
		for (int i = 0; i < plantsCount; i++) {
			String currentPlantPath = "plants.plant" + i;
			if (registerConfig.get(currentPlantPath) == null) {
				continue;
			}
			ConfigurationSection plant = registerConfig.getConfigurationSection(currentPlantPath);
			int x = plant.getInt("x");
			int y = plant.getInt("y");
			int z = plant.getInt("z");
			World w = Bukkit.getWorld(plant.getString("world"));
			if (loc.getWorld().getName().equals(w.getName()) && x == loc.getBlockX() && y == loc.getBlockY() && z == loc.getBlockZ()) {
				return currentPlantPath;
			}
		}
		return "";
	}
	
	public ConfigurationSection getPlant(String plantName) {
		return registerConfig.getConfigurationSection(plantName);
	}
	
	public ConfigurationSection getPlant(Location plantLocation) {
		return registerConfig.getConfigurationSection(getPlantPath(plantLocation));
	}
	
	public List<ConfigurationSection> getPlants() {
		List<ConfigurationSection> plants = new ArrayList<>();
		for (int i = 0; i < registerConfig.getInt("plantsNumber"); i++) {
			String currentPlant = "plants.plant" + i;
			if (registerConfig.get(currentPlant) != null) {
				plants.add(getPlant(currentPlant));
			}
		}
		
		return plants;
	}
	
	public Location getPlantLocation(ConfigurationSection plant) {
		World w = Bukkit.getWorld(plant.getString("world"));
		int x = plant.getInt("x");
		int y = plant.getInt("y");
		int z = plant.getInt("z");
		return new Location(w, x, y, z);
	}
}
