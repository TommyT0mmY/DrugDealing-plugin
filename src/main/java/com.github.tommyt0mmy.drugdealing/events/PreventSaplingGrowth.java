package com.github.tommyt0mmy.drugdealing.events;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

public class PreventSaplingGrowth implements Listener {
	
	private DrugDealing mainClass;
	public PreventSaplingGrowth(DrugDealing mainClass) {
		this.mainClass = mainClass;
	}
	
	@EventHandler
	public void onStructureGrow(StructureGrowEvent e) {
		Location loc = e.getLocation();
		if (mainClass.plantsreg.isDrugPlant(loc)) {
			e.setCancelled(true);
			if (e.isFromBonemeal()) {
				Player p = e.getPlayer();
				p.sendMessage(mainClass.messages.formattedMessage("§c", "cannot_grow_weed"));
			}
		}
	}

}
