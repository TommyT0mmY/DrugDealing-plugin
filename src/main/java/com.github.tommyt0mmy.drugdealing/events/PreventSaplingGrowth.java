package com.github.tommyt0mmy.drugdealing.events;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

public class PreventSaplingGrowth implements Listener
{

    private final DrugDealing plugin = DrugDealing.getInstance();

    @EventHandler
    public void onStructureGrow(StructureGrowEvent e)
    {
        Location loc = e.getLocation();
        if (plugin.plantsRegister.isDrugPlant(loc))
        {
            e.setCancelled(true);
            if (e.isFromBonemeal())
            {
                Player p = e.getPlayer();
                p.sendMessage(plugin.messages.formattedChatMessage("cannot_grow_weed"));
            }
        }
    }

}
