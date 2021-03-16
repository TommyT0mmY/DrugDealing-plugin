package com.github.tommyt0mmy.drugdealing.commands;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetDrug implements CommandExecutor
{

    private final DrugDealing plugin = DrugDealing.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(plugin.messages.getChatMessage("only_players_command"));
            return true;
        }
        Player p = (Player) sender;
        String usage = plugin.getCommand("getdrug").getUsage().replaceAll("<command>", label);

        if (!p.hasPermission(Permissions.getPermission("getdrug_weed")) || !p.hasPermission(Permissions.getPermission("getdrug_coke")))
        {
            p.sendMessage(plugin.messages.formattedChatMessage("invalid_permission"));
            return true;
        }

        if (args.length != 1)
        {
            p.sendMessage(plugin.messages.formattedText(ChatColor.RED, usage));
            return true;
        }

        switch (args[0].toLowerCase())
        {
            case "coke":
                if (!p.hasPermission(Permissions.getPermission("getdrug_coke")))
                {
                    p.sendMessage(plugin.messages.formattedChatMessage("invalid_permission"));
                    break;
                }

                p.getInventory().addItem(plugin.drugs.getCokeDrugItemStack());
                p.sendMessage(plugin.messages.formattedChatMessage("received_coke_drug"));
                break;
            case "weed":
                if (!p.hasPermission(Permissions.getPermission("getdrug_weed")))
                {
                    p.sendMessage(plugin.messages.formattedChatMessage("invalid_permission"));
                    break;
                }

                p.getInventory().addItem(plugin.drugs.getWeedDrugItemStack());
                p.sendMessage(plugin.messages.formattedChatMessage("received_weed_drug"));
                break;
            default:
                p.sendMessage(plugin.messages.formattedText(ChatColor.RED, usage));
                break;
        }

        return true;
    }

}
