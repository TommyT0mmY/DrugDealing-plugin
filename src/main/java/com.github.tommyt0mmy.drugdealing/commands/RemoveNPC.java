package com.github.tommyt0mmy.drugdealing.commands;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RemoveNPC implements CommandExecutor
{
    private final DrugDealing plugin = DrugDealing.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        String usage = Objects.requireNonNull(plugin.getCommand("removenpc")).getUsage().replace("<command>", label); //usage message
        Player p;

        if (!(sender instanceof Player))
        {
            sender.sendMessage(plugin.language.formattedChatMessage("only_players_command"));
            return true;
        } else
            p = (Player) sender;

        if (p.hasPermission(Permissions.getPermission("remove_dealer")) || p.hasPermission(Permissions.getPermission("remove_producer")))
        {
            if (args.length == 0)
            {
                p.sendMessage(plugin.language.formattedChatMessage("right_click_npc_to_delete"));
                if (plugin.toRemoveNPCs.contains(p.getUniqueId()))
                {
                    return true;
                }

                plugin.toRemoveNPCs.add(p.getUniqueId());

                //one minute timer
                new BukkitRunnable()
                {
                    public void run()
                    {
                        if (plugin.toRemoveNPCs.contains(p.getUniqueId()))
                        {
                            plugin.toRemoveNPCs.remove(p.getUniqueId());
                            p.sendMessage(plugin.language.formattedChatMessage("removenpc_time_is_up").replace("<COMMAND>", label));
                        }
                        cancel();
                    }
                }.runTaskLater(plugin, 60 * 20);

            } else
            {
                p.sendMessage(plugin.language.formattedText(ChatColor.RED, usage));
            }
        } else
        {
            p.sendMessage(plugin.language.formattedChatMessage("invalid_permission"));
        }
        return true;
    }
}
