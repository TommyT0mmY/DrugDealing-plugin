package drugdealing.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

import drugdealing.Main;

public class PreventSaplingGrowth implements Listener {
	
	private Main mainClass;
	public PreventSaplingGrowth(Main mainClass) {
		this.mainClass = mainClass;
	}
	
	@EventHandler
	public void onStructureGrow(StructureGrowEvent e) {
		Location loc = e.getLocation();
		if (mainClass.plantsreg.isDrugPlant(loc)) {
			e.setCancelled(true);
			if (e.isFromBonemeal()) {
				Player p = (Player) e.getPlayer();
				p.sendMessage(mainClass.messages.formattedMessage("§c", "cannot_grow_weed"));
			}
		}
	}

}
