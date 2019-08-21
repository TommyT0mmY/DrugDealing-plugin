//Handles configs.yml file

package drugdealing.utility;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import drugdealing.Main;

public class Configs {
	
	private Main mainClass;
	public Configs(Main mainClass) {
		this.mainClass = mainClass;
		loadConfigs();
	}

	private File configsFile;
	private FileConfiguration configs;
	
    private void loadConfigs() { //loading plants.yml
    	configsFile = new File(mainClass.datafolder, "configs.yml");
        if (!configsFile.exists()) {
        	configsFile.getParentFile().mkdirs();
            mainClass.saveResource("configs.yml", false);
            mainClass.console.info("Created file configs.yml");
         }

        configs = new YamlConfiguration();
        try {
        	configs.load(configsFile);
        } catch (Exception e) {
            mainClass.console.severe("Couldn't load configs.yml file properly!");
        }
    }
    
    public FileConfiguration getConfigs() {
    	return configs;
    }
    
}
