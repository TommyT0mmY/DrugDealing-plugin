package com.github.tommyt0mmy.drugdealing.commands;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help implements CommandExecutor {
	
	private DrugDealing mainClass;
	public Help(DrugDealing mainClass) {
		this.mainClass = mainClass;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player p = (Player) sender;
		if (!p.hasPermission(Permissions.getPermission("help_menu"))) {
			p.sendMessage(mainClass.messages.getMessage("invalid_permission"));
			return true;
		}
		
		//TODO
		
		
		return true;
	}
	
	
	
	
}
