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

    private final DrugDealing instance = DrugDealing.getInstance();

    @EventHandler
    public void onPlantedDrug(BlockPlaceEvent e)
    {
        Player p = (Player) e.getPlayer();
        try
        {

            Block placed = (Block) e.getBlock();

            e.setCancelled(true); //TODO THIS CAUSES TOO THE WORLDGUARD BUG MAYBE

            ItemStack placedIS = p.getInventory().getItemInMainHand();

            //Coke placed
            //TODO FIX REDUNDANCY
            if (instance.drugs.isCokePlantItemStack(placedIS))
            {
                //permissions check
                if (!(p.hasPermission(Permissions.getPermission("plant_coke")) || p.hasPermission(Permissions.getPermission("plant_weed"))))
                {
                    e.setCancelled(true);
                    p.sendMessage(instance.language.formattedChatMessage("invalid_permission"));
                    return;
                }

                if (instance.drugs.isPlantedOnFarmland(placed))
                {
                    p.sendMessage(instance.language.formattedChatMessage("planted_coke"));
                    instance.plantsRegister.addPlant(placed, "coke");
                } else
                {
                    p.sendMessage(instance.language.formattedChatMessage("invalid_surface"));
                    return;
                }
            }

            //Weed planted

            if (instance.drugs.isWeedPlantItemStack(placedIS))
            {
                if (instance.drugs.isPlantedOnFarmland(placed))
                {
                    p.sendMessage(instance.language.formattedChatMessage("planted_weed"));
                    instance.plantsRegister.addPlant(placed, "weed");
                } else
                {
                    p.sendMessage(instance.language.formattedChatMessage("invalid_surface"));
                    return;
                }
            }

            e.setCancelled(false); //TODO MAYBE THIS IS THE CAUSE OF THE WORLDGUARD BUG
        } catch (Exception exception)
        {
            e.setCancelled(true);
            p.sendMessage(instance.language.formattedChatMessage("unexpected_error"));
            exception.printStackTrace();
            return;
        }
    }
}
