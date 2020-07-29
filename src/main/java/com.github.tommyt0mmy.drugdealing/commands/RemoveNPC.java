package com.github.tommyt0mmy.drugdealing.commands;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RemoveNPC implements CommandExecutor {
    private DrugDealing mainClass = DrugDealing.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String usage = mainClass.getCommand("removenpc").getUsage().replaceAll("<command>", label); //usage message
        Player p;

        if (!(sender instanceof Player)) {
            sender.sendMessage(mainClass.messages.formattedChatMessage("only_players_command"));
            return true;
        } else p = (Player) sender;

        if (p.hasPermission(Permissions.getPermission("remove_dealer")) || p.hasPermission(Permissions.getPermission("remove_producer"))) {
            if (args.length == 0) {
                p.sendMessage(mainClass.messages.formattedChatMessage("right_click_npc_to_delete"));
                if (mainClass.toRemoveNPCS.contains(p.getUniqueId())) {
                    return true;
                }

                mainClass.toRemoveNPCS.add(p.getUniqueId());

                //one minute timer
                new BukkitRunnable() {
                    public void run () {
                        if (mainClass.toRemoveNPCS.contains(p.getUniqueId())) {
                            mainClass.toRemoveNPCS.remove(p.getUniqueId());
                            p.sendMessage(mainClass.messages.formattedChatMessage("removenpc_time_is_up").replaceAll("<COMMAND>", label));
                        }
                        cancel();
                    }
                }.runTaskTimer(mainClass, 60 * 20, 1);

            } else {
                p.sendMessage(mainClass.messages.formattedText(ChatColor.RED, usage));
            }
        } else {
            p.sendMessage(mainClass.messages.formattedChatMessage("invalid_permission"));
        }
        return true;
    }
}
