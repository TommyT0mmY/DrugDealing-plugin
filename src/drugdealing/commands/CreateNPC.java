//command 'setnpc'

package drugdealing.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import drugdealing.Main;

public class CreateNPC implements CommandExecutor {
	private Main mainClass = Main.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) { //if the sender isn't a player
			sender.sendMessage(mainClass.messages.formattedMessage("", "only_players_command")); //sending player error message
			return true;
		}
		Player p = (Player) sender;
		String usage = mainClass.getCommand("setnpc").getUsage().replaceAll("<command>", label); //usage message
		
		if (args.length != 2) {
			p.sendMessage(mainClass.messages.formattedText("§c", usage)); //sending player usage message
			return true;
		}
		
		String role = args[0].toLowerCase();
		String name = "";

		if (args[1].length() > 12) { //do not remove
			p.sendMessage(mainClass.messages.formattedMessage("§c", "name_too_long")); //sending player error message
			return true;
		}

		switch(role) {
			case "producer":
				p.sendMessage(mainClass.messages.formattedMessage("§a", "spawned_producer"));
				name = ("§a" + args[1]).trim();
				break;
			case "dealer":
				p.sendMessage(mainClass.messages.formattedMessage("§a", "spawned_dealer"));
				name = ("§6" + args[1]).trim();
				break;
			default:
				p.sendMessage(mainClass.messages.formattedText("§c", usage)); //sending player usage message
				return true;
		}
		
		mainClass.npc.createNPC(p, name); //spawning ingame NPC
		
		return true;
	}

}
