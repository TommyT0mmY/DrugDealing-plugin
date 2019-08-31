package com.github.tommyt0mmy.drugdealing.events;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.XMaterial;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class RemoveUprootedPlants implements Listener {
	
	private DrugDealing mainClass;
	public RemoveUprootedPlants(DrugDealing mainClass) {
		this.mainClass = mainClass;
	}
	
	@EventHandler
	public void removeUprooted(PlayerInteractEvent event) {
		if(event.getAction() == Action.PHYSICAL) {
			Block plantBase = event.getClickedBlock().getLocation().add(0, 1, 0).getBlock();
			removeProcedure(plantBase, true, true);
		}
	}
	
	@EventHandler
	public void removeDestroyed(BlockBreakEvent event) {
		//if the hitten block is the plant
		Block plantBase = event.getBlock();
		GameMode gamemode = event.getPlayer().getGameMode();
		boolean dropItem = true; //drops will be produced only if not in adventure or creative
		if (gamemode.equals(GameMode.ADVENTURE) || gamemode.equals(GameMode.CREATIVE)) {
			dropItem = false;
		}
		removeProcedure(plantBase, false, dropItem);
		
		//if the hitten block is the base of the plant
		if (plantBase.getType().equals(XMaterial.FARMLAND.parseItem().getType())) {
			plantBase = plantBase.getLocation().add(0, 1, 0).getBlock(); //setting plantBase to the block above
			removeProcedure(plantBase, false, dropItem);
		}

		if (mainClass.plantsreg.isDrugPlant(plantBase.getLocation())) {
			removeProcedure(plantBase, false, dropItem);
		}
	}
	
	@EventHandler
	public void removeDestroyedByLiquid(BlockFromToEvent event) {
		Block destinationBlock = event.getToBlock();
		if (mainClass.plantsreg.isDrugPlant(destinationBlock.getLocation())) {
			removeProcedure(destinationBlock, false, true);
		}
	}
	
	@SuppressWarnings("deprecation")
	private boolean removeProcedure(Block plantBase, boolean checkFarmland, boolean dropItems) {
		//getting farm land block and plant base location
		Location plantBaseLocation = plantBase.getLocation(); //getting the plantBase location
		Block farmland = plantBaseLocation.subtract(0, 1, 0).getBlock();
		plantBaseLocation.add(0, 1, 0);
		
		//checking farm land
		if (checkFarmland) {
			if (!farmland.getType().equals(XMaterial.FARMLAND.parseItem().getType())) { //if the block below the plant isn't made of farm land
				return false;
			}
		}
		
		//checking plant
		if (mainClass.plantsreg.isDrugPlant(plantBaseLocation)) {
			mainClass.drugs.destroyPlant(plantBase, dropItems); //removing the plant
			return true;
		}
		return false;
	}
}
