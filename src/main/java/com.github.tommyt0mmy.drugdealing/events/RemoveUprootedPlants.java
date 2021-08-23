package com.github.tommyt0mmy.drugdealing.events;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class RemoveUprootedPlants implements Listener
{

    private final DrugDealing plugin = DrugDealing.getInstance();

    @EventHandler
    public void removeUprooted(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.PHYSICAL)
        {
            //todo fix may produce npe
            Block plantBase = event.getClickedBlock().getLocation().add(0, 1, 0).getBlock();
            removeProcedure(plantBase, true, true);
        }
    }

    @EventHandler
    public void removeDestroyed(BlockBreakEvent event)
    {
        Block destroyedBlock = event.getBlock();
        Location loc = destroyedBlock.getLocation();
        Block aboveBlock = loc.add(0, 1, 0).getBlock();
        Block belowBlock = loc.subtract(0, 2, 0).getBlock();
        loc.add(0, 1, 0);
        boolean dropItems = true; //drops will be produced only if not in adventure or creative
        GameMode gamemode = event.getPlayer().getGameMode();
        if (gamemode.equals(GameMode.ADVENTURE) || gamemode.equals(GameMode.CREATIVE))
        {
            dropItems = false;
        }

        if (!removeProcedure(destroyedBlock, false, dropItems))
        { //if broken block isn't a plant base
            if (!removeProcedure(aboveBlock, false, dropItems))
            { //if the broken block isn't the soil of the plant
                if (plugin.plantsRegister.isDrugPlant(belowBlock.getLocation()))
                { //if the broken block isn't the second part of a grown plant
                    if (plugin.plantsRegister.isTimeGrown(belowBlock.getLocation()))
                    {
                        removeProcedure(belowBlock, false, dropItems);
                    }
                }
            }
        }
    }

    @EventHandler
    public void removeDestroyedByLiquid(BlockFromToEvent event)
    {
        Block destinationBlock = event.getToBlock();
        if (plugin.plantsRegister.isDrugPlant(destinationBlock.getLocation()))
        {
            removeProcedure(destinationBlock, false, true);
        }
    }

    @SuppressWarnings("deprecation")
    private boolean removeProcedure(Block plantBase, boolean checkFarmland, boolean dropItems)
    { //returns true if plantBase is the base of a drug plant and gets removed successfukky
        //getting farm land block and plant base location
        Location plantBaseLocation = plantBase.getLocation(); //getting the plantBase location
        Block farmland = plantBaseLocation.subtract(0, 1, 0).getBlock();
        plantBaseLocation.add(0, 1, 0);

        //checking farm land
        if (checkFarmland)
        {
            if (!farmland.getType().equals(Material.FARMLAND))
            { //if the block below the plant isn't made of farm land
                return false;
            }
        }

        //checking plant
        if (plugin.plantsRegister.isDrugPlantOld(plantBaseLocation))
        {
            plugin.drugs.destroyPlant(plantBase, dropItems); //removing the plant
            return true;
        }
        return false;
    }
}
