package com.github.tommyt0mmy.drugdealing.commands;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetPlant implements CommandExecutor {
	
	private DrugDealing mainClass;
	public GetPlant(DrugDealing mainClass) {
		this.mainClass = mainClass;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(mainClass.messages.formattedMessage("", "only_players_command"));
			return true;
		}
		Player p = (Player) sender;
		String usage = mainClass.getCommand("getplant").getUsage().replaceAll("<command>", label);
		
		if (!p.hasPermission(Permissions.getPermission("getplant_weed")) || !p.hasPermission(Permissions.getPermission("getplant_coke"))) {
			p.sendMessage(mainClass.messages.formattedMessage("§c", "invalid_permission"));
			return true;
		}
		
		if (args.length != 1) {
			p.sendMessage(mainClass.messages.formattedText("§c", usage));
			return true;
		}
		
		switch (args[0].toLowerCase()) {
		case "coke":
			if (!p.hasPermission(Permissions.getPermission("getplant_coke"))) {
				p.sendMessage(mainClass.messages.formattedMessage("§c", "invalid_permission"));
				break;
			}
			
			p.getInventory().addItem(mainClass.drugs.getCokePlantItemStack());
			p.sendMessage(mainClass.messages.formattedMessage("§a", "received_coke_plant"));
			break;
		case "weed":	
			if (!p.hasPermission(Permissions.getPermission("getplant_weed"))) {
				p.sendMessage(mainClass.messages.formattedMessage("§c", "invalid_permission"));
				break;
			}
			
			p.getInventory().addItem(mainClass.drugs.getWeedPlantItemStack());
			p.sendMessage(mainClass.messages.formattedMessage("§a", "received_weed_plant"));
			break;
		default:
			p.sendMessage(mainClass.messages.formattedText("§c", usage));
			break;
		}
		
		return true;
	}

}
