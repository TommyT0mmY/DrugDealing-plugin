package com.github.tommyt0mmy.drugdealing.events;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.DrugType;
import com.github.tommyt0mmy.drugdealing.utility.Permissions;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class PlantedDrug implements Listener
{

    private final DrugDealing instance = DrugDealing.getInstance();

    // TODO || THIS CLASS CAUSES A BUG WITH WORLDGUARD TURNED ON, BLOCKS CAN BE PLACED EVERYWHERE, TESTS NEED TO BE DONE BEFORE RELEASE

    @EventHandler(priority = EventPriority.LOW) //todo maybe priority helps with the worldguard bug, needs testing
    public void onPlantedDrug(BlockPlaceEvent e)
    {
        Player p = e.getPlayer();
        try
        {
            Block placedBlock = e.getBlock();

            ItemStack placedIS = p.getInventory().getItemInMainHand();
            Optional<DrugType> drugTypeOptional = instance.drugs.getDrugType(placedIS);

            //todo fix redundant code

            //Coke planted

            if (drugTypeOptional.isPresent() && drugTypeOptional.get() == DrugType.COKE_PLANT)
            {
                //permissions check
                if (!p.hasPermission(Permissions.getPermission("plant_coke")))
                {
                    p.sendMessage(instance.language.formattedChatMessage("invalid_permission"));

                    e.setCancelled(true);
                    return;
                }

                //plants need to be planted on farmland
                if (instance.drugs.isPlantedOnFarmland(placedBlock))
                {
                    p.sendMessage(instance.language.formattedChatMessage("planted_coke"));
                    instance.plantsRegister.addPlant(placedBlock.getLocation(), DrugType.COKE_PLANT);
                } else
                {
                    p.sendMessage(instance.language.formattedChatMessage("invalid_surface"));

                    e.setCancelled(true);
                    return;
                }
            }

            //Weed planted

            if (drugTypeOptional.isPresent() && drugTypeOptional.get() == DrugType.WEED_PLANT)
            {
                //permissions check
                if (!p.hasPermission(Permissions.getPermission("plant_weed")))
                {
                    p.sendMessage(instance.language.formattedChatMessage("invalid_permission"));

                    e.setCancelled(true);
                    return;
                }

                //plants need to be planted on farmland
                if (instance.drugs.isPlantedOnFarmland(placedBlock))
                {
                    p.sendMessage(instance.language.formattedChatMessage("planted_weed"));
                    instance.plantsRegister.addPlant(placedBlock.getLocation(), DrugType.WEED_PLANT);
                } else
                {
                    p.sendMessage(instance.language.formattedChatMessage("invalid_surface"));

                    e.setCancelled(true);
                    return;
                }
            }

        } catch (Exception exception)
        {
            p.sendMessage(instance.language.formattedChatMessage("unexpected_error"));
            exception.printStackTrace();
        }
    }
}
