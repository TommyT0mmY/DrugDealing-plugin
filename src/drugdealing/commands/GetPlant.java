package drugdealing.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import drugdealing.Main;
import drugdealing.utility.Permissions;

public class GetPlant implements CommandExecutor {
	
	private Main mainClass;
	public GetPlant(Main mainClass) {
		this.mainClass = mainClass;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player p = (Player) sender;
		String usage = mainClass.getCommand("getplant").getUsage().replaceAll("<command>", "getplant");
		
		if (args.length != 1) {
			p.sendMessage(String.format("§c%s %s", mainClass.messages.getMessage("ingame_prefix"), usage));
			return true;
		}
		
		switch (args[0].toLowerCase()) {
		case "coke":
			if (!p.hasPermission(Permissions.getPermission("getplant_coke"))) {
				p.sendMessage(mainClass.messages.getMessage("invalid_permission"));
				break;
			}
			
			p.getInventory().addItem(mainClass.drugs.getCokeItemStack());
			p.sendMessage(String.format("§a%s %s", mainClass.messages.getMessage("ingame_prefix"), mainClass.messages.getMessage("received_coke")));
			break;
		case "weed":	
			if (!p.hasPermission(Permissions.getPermission("getplant_weed"))) {
				p.sendMessage(mainClass.messages.getMessage("invalid_permission"));
				break;
			}
			
			p.getInventory().addItem(mainClass.drugs.getWeedItemStack());	
			p.sendMessage(String.format("§a%s %s", mainClass.messages.getMessage("ingame_prefix"), mainClass.messages.getMessage("received_weed")));
			break;
		default:
			p.sendMessage(String.format("§c%s %s", mainClass.messages.getMessage("ingame_prefix"), usage));
			break;
		}
		
		return true;
	}
	
	
	

}
