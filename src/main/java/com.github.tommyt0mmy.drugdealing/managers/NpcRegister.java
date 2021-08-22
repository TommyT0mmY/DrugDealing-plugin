//Handles the npcs.yml file

package com.github.tommyt0mmy.drugdealing.managers;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.CriminalRole;
import com.github.tommyt0mmy.drugdealing.utility.DrugType;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class NpcRegister
{

    private final DrugDealing instance = DrugDealing.getInstance();

    //TODO STOP USING THIS VARIABLE
    private File registerConfigFile;
    //TODO STOP USING THIS VARIABLE
    private YamlConfiguration registerConfig;

    public NpcRegister()
    {}

    @Deprecated
    private void loadRegister()
    { //loading npcs.yml
        registerConfigFile = new File(instance.dataFolder, "npcs.yml");
        if (!registerConfigFile.exists())
        {
            registerConfigFile.getParentFile().mkdirs();
            instance.saveResource("npcs.yml", false);
            instance.console.info("Created file npcs.yml");
        }

        registerConfig = new YamlConfiguration();
        try
        {
            registerConfig.load(registerConfigFile);
        } catch (Exception e)
        {
            instance.console.severe("Couldn't load npcs.yml file properly!");
        }
    }

    @Deprecated
    private void loadConfigs()
    {
        try
        {
            registerConfig.load(registerConfigFile);
        } catch (Exception exception) {instance.console.severe("Couldn't load npcs.yml file properly!");}
    }

    @Deprecated
    private void saveConfigs()
    {
        try
        {
            registerConfig.save(registerConfigFile);
        } catch (Exception e) {instance.console.severe("Couldn't save to file npcs.yml");}
    }

    @Deprecated
    public void saveNpcOld(NPC npc, CriminalRole role, List<DrugType> notAcceptedDrugs)
    {
        loadConfigs();
        String name = npc.getName();
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
        if (notAcceptedDrugs != null)
        {
            for (DrugType notAcceptedDrug : notAcceptedDrugs)
            {
                registerConfig.set(newKey + ".notaccepted." + notAcceptedDrug.toString(), true);
            }
        }
        saveConfigs();
    }

    public void saveNpc(NPC npc, CriminalRole role, DrugType[] accepts)
    {
        try
        {
            DrugDealing.database.saveNpc(npc.getUniqueId(), role, npc.getName(), accepts);
        } catch (SQLException e)
        {
            instance.console.severe("Couldn't save npc information to database");
            e.printStackTrace();
        }
    }

    //if the NPC isn't registered returns null

    @Deprecated
    private ConfigurationSection getNpcCS(NPC npc)
    {
        return getNpcCS(npc.getId());
    }

    //if the NPC isn't registered returns null

    @Deprecated
    private ConfigurationSection getNpcCS(int ID)
    {
        List<ConfigurationSection> ConfSections = getConfigurationSections();
        for (ConfigurationSection cs : ConfSections)
        {
            if (cs.getInt("ID") == ID)
            {
                return cs;
            }
        }
        return null;
    }

    //checks if the clicked NPC is a NPC handled by this plugin

    @Deprecated
    public boolean isCriminalNpcOld(NPC npc)
    {
        return getNpcCS(npc) != null;
    }

    public boolean isCriminalNpc(NPC npc)
    {
        try
        {
            return DrugDealing.database.findNpc(npc.getUniqueId());
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Deprecated
    private List<ConfigurationSection> getConfigurationSections()
    {
        List<ConfigurationSection> CSlist = new ArrayList<>();
        Set<String> keys = registerConfig.getKeys(false);
        for (String key : keys)
        {
            if (key.startsWith("NPC") && registerConfig.isConfigurationSection(key))
            {
                CSlist.add(registerConfig.getConfigurationSection(key));
            }
        }
        return CSlist;
    }

    @Deprecated
    public CriminalRole getRoleOld(NPC npc)
    {
        ConfigurationSection cs = getNpcCS(npc);
        if (cs != null)
        {
            String role = cs.getString("role");
            if (role.equalsIgnoreCase(CriminalRole.DEALER.toString()))
            {
                return CriminalRole.DEALER;
            } else if (role.equalsIgnoreCase(CriminalRole.PRODUCER.toString()))
            {
                return CriminalRole.PRODUCER;
            }
        }
        return null;
    }

    public Optional<CriminalRole> getCriminalRole(NPC npc)
    {
        try
        {
            CriminalRole criminalRole = DrugDealing.database.getNpcRole(npc.getUniqueId());
            return Optional.of(criminalRole);
        } catch (SQLException e)
        {
            instance.console.severe("Couldn't get NPC role");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Deprecated
    public boolean acceptsDrugTypeOld(NPC npc, DrugType drugType)
    {
        ConfigurationSection cs = getNpcCS(npc);
        if (cs != null)
        {
            return cs.getBoolean("notaccepted." + drugType.toString());
        }
        return false;
    }

    public boolean acceptsDrugType(NPC npc, DrugType drugType)
    {
        try
        {
            return DrugDealing.database.getNpcAccepted(npc.getUniqueId()).contains(drugType);
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Deprecated
    public void removeNpcOld(NPC npc)
    {
        loadConfigs();
        ConfigurationSection npcCS = getNpcCS(npc);
        String sectionName = npcCS.getName();
        registerConfig.set(sectionName, null);
        saveConfigs();
    }

    public void removeNpc(NPC npc)
    {
        try
        {
            DrugDealing.database.removeNpc(npc.getUniqueId());
        } catch (SQLException e)
        {
            instance.console.severe("Couldn't remove NPC from database");
            e.printStackTrace();
        }
    }

}
