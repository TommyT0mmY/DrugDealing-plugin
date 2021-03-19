//command 'getplant'

package com.github.tommyt0mmy.drugdealing.commands;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetPlant implements CommandExecutor
{

    private final DrugDealing plugin = DrugDealing.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(plugin.language.getChatMessage("only_players_command"));
            return true;
        }
        Player p = (Player) sender;
        String usage = plugin.getCommand("getplant").getUsage().replace("<command>", label);

        if (!p.hasPermission(Permissions.getPermission("getplant_weed")) || !p.hasPermission(Permissions.getPermission("getplant_coke")))
        {
            p.sendMessage(plugin.language.formattedChatMessage("invalid_permission"));
            return true;
        }

        if (args.length != 1)
        {
            p.sendMessage(plugin.language.formattedText(ChatColor.RED, usage));
            return true;
        }

        switch (args[0].toLowerCase())
        {
            case "coke":
                if (!p.hasPermission(Permissions.getPermission("getplant_coke")))
                {
                    p.sendMessage(plugin.language.formattedChatMessage("invalid_permission"));
                    break;
                }

                p.getInventory().addItem(plugin.drugs.getCokePlantItemStack());
                p.sendMessage(plugin.language.formattedChatMessage("received_coke_plant"));
                break;
            case "weed":
                if (!p.hasPermission(Permissions.getPermission("getplant_weed")))
                {
                    p.sendMessage(plugin.language.formattedChatMessage("invalid_permission"));
                    break;
                }

                p.getInventory().addItem(plugin.drugs.getWeedPlantItemStack());
                p.sendMessage(plugin.language.formattedChatMessage("received_weed_plant"));
                break;
            default:
                p.sendMessage(plugin.language.formattedText(ChatColor.RED, usage));
                break;
        }

        return true;
    }

}
