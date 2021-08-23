//Handles the npcs.yml file

package com.github.tommyt0mmy.drugdealing.managers;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.CriminalRole;
import com.github.tommyt0mmy.drugdealing.utility.DrugType;
import net.citizensnpcs.api.npc.NPC;

import java.sql.SQLException;
import java.util.Optional;

public class NpcRegister
{
    private final DrugDealing instance = DrugDealing.getInstance();

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
