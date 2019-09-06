package com.github.tommyt0mmy.drugdealing.events;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.Permissions;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class PlantedDrug implements Listener {
	
	private DrugDealing mainClass;
    public PlantedDrug(DrugDealing mainClass) {
        this.mainClass = mainClass;
    }
	
	@EventHandler
	public void onPlantedDrug(BlockPlaceEvent e) {
		Player p = (Player) e.getPlayer();
		try {
			if (!(p.hasPermission(Permissions.getPermission("plant_coke")) || p.hasPermission(Permissions.getPermission("plant_weed")))) {
				e.setCancelled(true);
				//temporary fix, add a message feedback in future (TODO)
				return;
			}
			
			Block placed = (Block) e.getBlock();
			
			e.setCancelled(true);
			
			ItemStack placedIS= p.getInventory().getItemInMainHand();

			//Coke placed

			if (mainClass.drugs.isCokePlantItemStack(placedIS)) {
				if (mainClass.drugs.isPlantedOnFarmland(placed)) {
					p.sendMessage(mainClass.messages.formattedMessage("§a", "planted_coke"));
					mainClass.plantsreg.addPlant(placed, "coke");
				}else {
					p.sendMessage(mainClass.messages.formattedMessage("§c", "invalid_surface"));
					return;
				}
			}
			
			//Weed planted
			
			if (mainClass.drugs.isWeedPlantItemStack(placedIS)) {
				if (mainClass.drugs.isPlantedOnFarmland(placed)) {
					p.sendMessage(mainClass.messages.formattedMessage("§a", "planted_weed"));
					mainClass.plantsreg.addPlant(placed, "weed");
				}else {
					p.sendMessage(mainClass.messages.formattedMessage("§c", "invalid_surface"));
					return;
				}
			}
			
			e.setCancelled(false);			
		} catch (Exception exception) {
			e.setCancelled(true);
			p.sendMessage(mainClass.messages.formattedMessage("§c", "unexpected_error"));
			exception.printStackTrace();
			return;
		}
	}
}
