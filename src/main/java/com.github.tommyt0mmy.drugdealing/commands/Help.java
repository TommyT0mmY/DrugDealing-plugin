package com.github.tommyt0mmy.drugdealing.commands;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help implements CommandExecutor {
	private DrugDealing mainClass = DrugDealing.getInstance();

	private final String beforeCommand = "§e§l>";
	private final String beforeParagraph = "§c§l>";
	private final String footer = "§c§l+ - - - - - - - - - - - - - - - - - +§r";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String usage = mainClass.getCommand("drugdealing").getUsage().replaceAll("<command>", label); //usage message

		Player p;

		if (!(sender instanceof Player)) {
			sender.sendMessage(mainClass.messages.formattedMessage("", "only_players_command"));
			return true;
		} else p = (Player) sender;

		if (!p.hasPermission(Permissions.getPermission("help_menu"))) {
			p.sendMessage(mainClass.messages.getMessage("invalid_permission"));
			return true;
		}

		String message = "";
		String pageTitle;
		Integer pageNumber = 1;

		if (args.length != 1 && args.length != 2) {
			p.sendMessage(mainClass.messages.formattedText("§c", usage));
			return true;
		} else if (args.length == 2) {
			pageNumber = Integer.parseInt(args[1]);
		}

		pageTitle = args[0];

		String pageIdentity = (pageTitle + pageNumber).toLowerCase();

		//Aliases support
		pageIdentity = pageIdentity.replaceAll("setcriminal", "setnpc");
		pageIdentity = pageIdentity.replaceAll("drugdealing", "dd");
		pageIdentity = pageIdentity.replaceAll("removecriminal", "removenpc");

		/* Pages

		-dd1
		-setnpc1
		.removenpc1
		-getplant1
		-getdrug1
		-permissions1
		-permissions2

		 */

		switch (pageIdentity) {
			case "dd1":
				message += getTopBar("/drugdealing");
				message += (beforeCommand + "§eUsage: §7/drugdealing <page> [page number]\n");
				message += (beforeCommand + "§eDescription: §7Opens the help menu\n");
				message += (beforeCommand + "§ePermission node: §7" + Permissions.getPermission("help_menu") + "\n");
				message += ("§aVersion §2" + mainClass.version + "\n");
				message += footer;

				break;
			case "setnpc1":
				message += getTopBar("/setnpc");
				message += (beforeCommand + "§eUsage: §7/setnpc <criminal type> <name> [not accepted items]...\n");
				message += (beforeCommand + "§eDescription: §7Spawns a criminal NPC\n");
				message += (beforeCommand + "§ePermission nodes: §7" + Permissions.getPermission("spawn_dealer") + "§e, §7" + Permissions.getPermission("spawn_producer") + "\n");
				message += (beforeCommand + "§eNPC types: ");
				message += ("§7Dealer§e, §7Producer\n");
				message += (beforeCommand + "§eNot accepted items: ");
				message += ("§7COKE_PRODUCT§e, §7WEED_PRODUCT§e, §7COKE_PLANT§e, §7WEED_PLANT\n");
				message += footer;
				break;
			case "removenpc1":
				message += getTopBar("/removenpc");
				message += (beforeCommand + "§eUsage: §7/removenpc\n");
				message += (beforeCommand + "§eDescription: §7Right clicking an NPC after that command is going to delete it§7\n");
				message += (beforeCommand + "§ePermission nodes: §7" + Permissions.getPermission("remove_dealer") + "§e, §7" + Permissions.getPermission("remove_producer") + "\n");
				message += footer;
				break;

			case "getplant1":
				message += getTopBar("/getplant");
				message += (beforeCommand + "§eUsage: §7/getplant <plant type>\n");
				message += (beforeCommand + "§eDescription: Gives the player a drug plant§7\n");
				message += (beforeCommand + "§ePermission nodes: \n-§7" + Permissions.getPermission("getplant_coke") + "\n§e-§7" + Permissions.getPermission("getplant_weed") + "\n");
				message += (beforeCommand + "§ePlant types:\n");
				message += ("§7coke§e, §7weed\n");
				message += footer;
				break;

			case "getdrug1":
				message += getTopBar("/getdrug");
				message += (beforeCommand + "§eUsage: §7/getdrug <drug type>\n");
				message += (beforeCommand + "§eDescription: Gives the player a drug§7\n");
				message += (beforeCommand + "§ePermission nodes: \n-§7" + Permissions.getPermission("getdrug_coke") + "\n§e-§7" + Permissions.getPermission("getdrug_weed") + "\n");
				message += (beforeCommand + "§eDrug types:\n");
				message += ("§7coke§e, §7weed\n");
				message += footer;
				break;

			case "permissions1":
				message += getTopBar("Permissions");
				message += (String.format("§7%s\n", Permissions.getPermission("plant_coke")));
				message += (String.format("§7%s\n", Permissions.getPermission("plant_weed")));
				message += (String.format("§7%s\n", Permissions.getPermission("harvest_coke")));
				message += (String.format("§7%s\n", Permissions.getPermission("harvest_weed")));
				message += (String.format("§7%s\n", Permissions.getPermission("getplant_coke")));
				message += (String.format("§7%s\n", Permissions.getPermission("getplant_weed")));
				message += (String.format("§7%s\n", Permissions.getPermission("getdrug_coke")));
				message += (String.format("§7%s\n", Permissions.getPermission("getdrug_weed")));
				message += footer;
				break;

			case "permissions2":
				message += getTopBar("Permissions");
				message += (String.format("§7%s\n", Permissions.getPermission("help_menu")));
				message += (String.format("§7%s\n", Permissions.getPermission("spawn_dealer")));
				message += (String.format("§7%s\n", Permissions.getPermission("spawn_producer")));
				message += (String.format("§7%s\n", Permissions.getPermission("remove_dealer")));
				message += (String.format("§7%s\n", Permissions.getPermission("remove_producer")));
				message += (String.format("§7%s\n", Permissions.getPermission("use_dealer")));
				message += (String.format("§7%s\n", Permissions.getPermission("use_producer")));
				message += (String.format("§7%s\n", Permissions.getPermission("consume_coke")));
				message += footer;
				break;

			default:
				p.sendMessage(mainClass.messages.formattedMessage("§c", "page_not_found"));
				return true;
		}

		p.sendMessage(message);
		return true;
	}
	
	private String getTopBar(String middleMessage) {
		String topbar = "";
		topbar += ("§c§l+ - - - §e§l§oDrugDealing help§c§l - - - +§r\n");
		topbar += (String.format("%s %s\n", beforeParagraph, middleMessage));

		return topbar;
	}
}
