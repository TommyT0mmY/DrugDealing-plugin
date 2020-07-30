package com.github.tommyt0mmy.drugdealing.events;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.managers.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onPlayerJoin implements Listener
{
    DrugDealing mainClass = DrugDealing.getInstance();
    UpdateChecker updateChecker = new UpdateChecker();

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();

        if (p.isOp())
        {
            if (updateChecker.needsUpdate())
            {
                p.sendMessage("§aAn update for §6DrugDealing§a is available at");
                p.sendMessage("§6" + mainClass.getSpigotResourceUrl());
                p.sendMessage(String.format("§aInstalled version: §e%s§a Lastest version: §e%s§r", updateChecker.getCurrent_version(), updateChecker.getLastest_version()));
            }
        }
    }


}
