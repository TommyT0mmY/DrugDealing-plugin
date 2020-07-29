//Handles the npcs.yml file

package com.github.tommyt0mmy.drugdealing.managers;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.CriminalRole;
import com.github.tommyt0mmy.drugdealing.utility.DrugType;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NpcRegister {

    private File registerConfigFile;
    private YamlConfiguration registerConfig;

    private DrugDealing mainClass = DrugDealing.getInstance();
    public NpcRegister() {
        loadRegister();
    }

    private void loadRegister() { //loading npcs.yml
        registerConfigFile = new File(mainClass.datafolder, "npcs.yml");
        if (!registerConfigFile.exists()) {
            registerConfigFile.getParentFile().mkdirs();
            mainClass.saveResource("npcs.yml", false);
            mainClass.console.info("Created file npcs.yml");
        }

        registerConfig = new YamlConfiguration();
        try {
            registerConfig.load(registerConfigFile);
        } catch (Exception e) {
            mainClass.console.severe("Couldn't load npcs.yml file properly!");
        }
    }

    private void loadConfigs() {
        try {
            registerConfig.load(registerConfigFile);
        } catch (Exception exception) {mainClass.console.severe("Couldn't load npcs.yml file properly!");}
    }

    private void saveConfigs() {
        try {
            registerConfig.save(registerConfigFile);
        } catch (Exception e) {mainClass.console.severe("Couldn't save to file npcs.yml");}
    }

    public void saveNpc (NPC npc, CriminalRole role, List<DrugType> notAcceptedDrugs) {
        loadConfigs();
        String name = npc.getName();
        Location loc = npc.getStoredLocation();
        int ID = npc.getId();
        int npcCount = registerConfig.getInt("npcNumber");
        npcCount++;
        registerConfig.set("npcNumber", npcCount);
        String newKey = "NPC" + npcCount;
        registerConfig.set(newKey + ".name", name);
        registerConfig.set(newKey + ".ID", ID);
        registerConfig.set(newKey + ".role", role.toString());
        registerConfig.set(newKey + ".notaccepted.WEED_PLANT", false);
        registerConfig.set(newKey + ".notaccepted.COKE_PLANT", false);
        registerConfig.set(newKey + ".notaccepted.WEED_PRODUCT", false);
        registerConfig.set(newKey + ".notaccepted.COKE_PRODUCT", false);
        if (notAcceptedDrugs != null) {
            for (DrugType notAcceptedDrug : notAcceptedDrugs) {
                registerConfig.set(newKey + ".notaccepted." + notAcceptedDrug.toString(), true);
            }
        }
        saveConfigs();
    }

    //if the NPC isn't registered returns null
    private ConfigurationSection getNpcCS (NPC npc) {
        return getNpcCS(npc.getId());
    }

    //if the NPC isn't registered returns null
    private ConfigurationSection getNpcCS(int ID) {
        List<ConfigurationSection> ConfSections = getConfigurationSections();
        for (ConfigurationSection cs : ConfSections) {
            if (cs.getInt("ID") == ID) {
                return cs;
            }
        }
        return null;
    }

    //checks if the clicked NPC is a NPC handled by this plugin
    public boolean isCriminalNpc (NPC npc) {
        return getNpcCS(npc) != null;
    }

    private List<ConfigurationSection> getConfigurationSections () {
        List<ConfigurationSection> CSlist = new ArrayList<ConfigurationSection>();
        Set<String> keys = registerConfig.getKeys(false);
        for (String key : keys) {
            if (key.startsWith("NPC") && registerConfig.isConfigurationSection(key)) {
                CSlist.add(registerConfig.getConfigurationSection(key));
            }
        }
        return CSlist;
    }

    public CriminalRole getRole (NPC npc) {
        ConfigurationSection cs = getNpcCS(npc);
        if (cs != null) {
            String role = cs.getString("role");
            if (role.equalsIgnoreCase(CriminalRole.DEALER.toString())) {
                return CriminalRole.DEALER;
            } else if (role.equalsIgnoreCase(CriminalRole.PRODUCER.toString())) {
                return CriminalRole.PRODUCER;
            }
        }
        return null;
    }

    public boolean acceptsDrugType (NPC npc, DrugType drugType) {
        ConfigurationSection cs = getNpcCS(npc);
        if (cs != null) {
            return cs.getBoolean("notaccepted." + drugType.toString());
        }
        return false;
    }

    public void removeNpc (NPC npc) {
        loadConfigs();
        ConfigurationSection npcCS = getNpcCS(npc);
        String sectionName = npcCS.getName();
        registerConfig.set(sectionName, null);
        saveConfigs();
    }

}
