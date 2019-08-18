package drugdealing.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
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
    
    public void addPlant(Block plant, String type) {
    	Location loc = plant.getLocation();
    	String plantName = "plants.plant" + registerConfig.getInt("plantsNumber");
    	int newPlantsNumber = registerConfig.getInt("plantsNumber") + 1;
    	registerConfig.set("plantsNumber", newPlantsNumber);
    	registerConfig.set(plantName + ".type", type);
    	registerConfig.set(plantName + ".x", loc.getBlockX());
    	registerConfig.set(plantName + ".y", loc.getBlockY());
    	registerConfig.set(plantName + ".z", loc.getBlockZ());
    	registerConfig.set(plantName + ".world", loc.getWorld().getName());
    	
    	//saving plants.yml
		try {
			registerConfig.save(registerConfigFile);
		} catch (IOException e) {mainClass.console.severe("Couldn't save plants.yml file properly!");}
    	return;
    }
    
    public boolean removePlant(Location loc) {
    	String plantName = findPlantPosition(loc);
    	if (plantName != null) {
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
    	String plantName = findPlantPosition(loc);
    	if (plantName != null) {
    		type = (String) registerConfig.get(plantName + ".type");
    	}
    	return type;
    }
    
    
	public boolean isDrugPlant(Location loc) {
		if (findPlantPosition(loc) != null) {
			return true;
		}
		
		return false;
	}
	
	private String findPlantPosition(Location loc) {
		String result = null;
		for (int i = 0; i < registerConfig.getInt("plantsNumber"); i++) {
			String currentPlant = "plants.plant" + i;
			if (registerConfig.get(currentPlant) == null) {
				continue;
			}
			
			int x = registerConfig.getInt(currentPlant + ".x");
			int y = registerConfig.getInt(currentPlant + ".y");
			int z = registerConfig.getInt(currentPlant + ".z");
			String worldName = (String) registerConfig.get(currentPlant + ".world");
			World w = Bukkit.getWorld(worldName);
			if (loc.distance(new Location(w, x, y, z)) < 0.2) {
				return currentPlant;
			}
		}
		
		return result;
	}
}
