package drugdealing.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import drugdealing.Main;
import drugdealing.utility.Messages;
import drugdealing.utility.Permissions;

public class Help implements CommandExecutor {
	
	private Main mainClass;
	public Help(Main mainClass) {
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
