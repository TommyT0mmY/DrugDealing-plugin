package drugdealing.events;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import drugdealing.Main;
import drugdealing.utility.XMaterial;

public class RemoveUprootedPlants implements Listener {
	
	private Main mainClass;
	public RemoveUprootedPlants(Main mainClass) {
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
			removeProcedure(plantBase, false, true);
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
	private void removeProcedure(Block plantBase, boolean checkFarmland, boolean dropItems) {
		//getting farm land block and plant base location
		Location plantBaseLocation = plantBase.getLocation(); //getting the plantBase location
		Block farmland = plantBaseLocation.subtract(0, 1, 0).getBlock();
		plantBaseLocation.add(0, 1, 0);	
		
		//checking if the destroyed part is the bottom of the plant
		byte plantBaseData = plantBase.getData(); //getting plant's block data
		if (plantBaseData == (byte) 10) { //top parts of plants have block data equal to 10
			plantBaseLocation.subtract(0, 1, 0); //setting the new location to the below block
			plantBase = plantBaseLocation.getBlock(); //setting plantBase to the actual base block
		}
		
		//checking farm land
		if (checkFarmland) {
			if (!farmland.getType().equals(XMaterial.FARMLAND.parseItem().getType())) { //if the block below the plant isn't made of farm land
				return;
			}
		}
		
		//checking plant
		if (mainClass.plantsreg.isDrugPlant(plantBaseLocation)) {
			mainClass.drugs.destroyPlant(plantBase, dropItems); //removing the plant
		}
	}
}
