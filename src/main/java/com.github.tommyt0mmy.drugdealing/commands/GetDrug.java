//command 'getdrug'

package com.github.tommyt0mmy.drugdealing.commands;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.DrugType;
import com.github.tommyt0mmy.drugdealing.utility.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GetDrug implements CommandExecutor
{

    private final DrugDealing plugin = DrugDealing.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(plugin.language.getChatMessage("only_players_command"));
            return true;
        }
        Player p = (Player) sender;
        String usage = Objects.requireNonNull(plugin.getCommand("getdrug")).getUsage().replace("<command>", label);

        if (!p.hasPermission(Permissions.getPermission("getdrug_weed")) || !p.hasPermission(Permissions.getPermission("getdrug_coke")))
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
                if (!p.hasPermission(Permissions.getPermission("getdrug_coke")))
                {
                    p.sendMessage(plugin.language.formattedChatMessage("invalid_permission"));
                    break;
                }

                p.getInventory().addItem(plugin.drugs.getItemStack(DrugType.COKE_PRODUCT));
                p.sendMessage(plugin.language.formattedChatMessage("received_coke_drug"));
                break;
            case "weed":
                if (!p.hasPermission(Permissions.getPermission("getdrug_weed")))
                {
                    p.sendMessage(plugin.language.formattedChatMessage("invalid_permission"));
                    break;
                }

                p.getInventory().addItem(plugin.drugs.getItemStack(DrugType.WEED_PRODUCT));
                p.sendMessage(plugin.language.formattedChatMessage("received_weed_drug"));
                break;
            default:
                p.sendMessage(plugin.language.formattedText(ChatColor.RED, usage));
                break;
        }

        return true;
    }

}
