package com.github.tommyt0mmy.drugdealing.events;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.Permissions;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class PlantedDrug implements Listener
{

    private final DrugDealing plugin = DrugDealing.getInstance();

    @EventHandler
    public void onPlantedDrug(BlockPlaceEvent e)
    {
        Player p = (Player) e.getPlayer();
        try
        {

            Block placed = (Block) e.getBlock();

            e.setCancelled(true);

            ItemStack placedIS = p.getInventory().getItemInMainHand();

            //Coke placed

            if (plugin.drugs.isCokePlantItemStack(placedIS))
            {
                //permissions check
                if (!(p.hasPermission(Permissions.getPermission("plant_coke")) || p.hasPermission(Permissions.getPermission("plant_weed"))))
                {
                    e.setCancelled(true);
                    p.sendMessage(plugin.messages.formattedChatMessage("invalid_permission"));
                    return;
                }

                if (plugin.drugs.isPlantedOnFarmland(placed))
                {
                    p.sendMessage(plugin.messages.formattedChatMessage("planted_coke"));
                    plugin.plantsRegister.addPlant(placed, "coke");
                } else
                {
                    p.sendMessage(plugin.messages.formattedChatMessage("invalid_surface"));
                    return;
                }
            }

            //Weed planted

            if (plugin.drugs.isWeedPlantItemStack(placedIS))
            {
                if (plugin.drugs.isPlantedOnFarmland(placed))
                {
                    p.sendMessage(plugin.messages.formattedChatMessage("planted_weed"));
                    plugin.plantsRegister.addPlant(placed, "weed");
                } else
                {
                    p.sendMessage(plugin.messages.formattedChatMessage("invalid_surface"));
                    return;
                }
            }

            e.setCancelled(false);
        } catch (Exception exception)
        {
            e.setCancelled(true);
            p.sendMessage(plugin.messages.formattedChatMessage("unexpected_error"));
            exception.printStackTrace();
            return;
        }
    }
}
