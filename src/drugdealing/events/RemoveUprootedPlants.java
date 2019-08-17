package drugdealing.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import drugdealing.Main;
import drugdealing.utility.XMaterial;

public class RemoveUprootedPlants implements Listener {
	private Main mainClass;
	public RemoveUprootedPlants(Main mainClass) {
		this.mainClass = mainClass;
	}
	
	@EventHandler
	public void removeUprooted(PlayerInteractEvent event)
	{
	    if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType().equals(XMaterial.FARMLAND.parseItem().getType())) {
			if (event.getClickedBlock().getType().equals(XMaterial.FARMLAND.parseItem().getType())) {
				//TODO
			}
		}
    }
}
