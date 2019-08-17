package drugdealing.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import drugdealing.Main;
import drugdealing.utility.Permissions;

public class PlantedDrug implements Listener {
	private Main mainClass;
    public PlantedDrug(Main mainClass) {
        this.mainClass = mainClass;
    }
	
	@EventHandler
	public void onPlantedDrug(BlockPlaceEvent e) {
		Player p = (Player) e.getPlayer();
		try {
			if (!(p.hasPermission(Permissions.getPermission("plant_coke")) || p.hasPermission(Permissions.getPermission("plant_weed")))) {
				return;
			}
			
			Block placed = (Block) e.getBlock();
			
			e.setCancelled(true);
			
			ItemStack placedIS= p.getInventory().getItemInMainHand();

			//Coke placed

			if (mainClass.drugs.isCokeItemStack(placedIS)) {
				if (mainClass.drugs.isPlantedOnFarmland(placed)) {
					p.sendMessage(mainClass.messages.formattedMessage("§a", "planted_coke"));							
				}else {
					p.sendMessage(mainClass.messages.formattedMessage("§c", "invalid_surface"));		
					return;
				}
			}
			
			//Weed planted
			
			if (mainClass.drugs.isWeedItemStack(placedIS)) {
				if (mainClass.drugs.isPlantedOnFarmland(placed)) {
					p.sendMessage(mainClass.messages.formattedMessage("§a", "planted_weed"));								
				}else {
					p.sendMessage(mainClass.messages.formattedMessage("§c", "invalid_surface"));
					return;
				}
			}
			
			e.setCancelled(false);			
		} catch (Exception exception) {
			e.setCancelled(true);
			p.sendMessage(mainClass.messages.getMessage("unexpected_error"));
			exception.printStackTrace();
			return;
		}
	}
}
