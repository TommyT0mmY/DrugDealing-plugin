package com.github.tommyt0mmy.drugdealing.events;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConsumedDrug implements Listener
{
    private final DrugDealing plugin = DrugDealing.getInstance();

    private Map<UUID, Boolean> timeout;

    public ConsumedDrug()
    {
        timeout = new HashMap<>();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent interactEvent)
    {
        Player p = interactEvent.getPlayer();
        UUID playerUUID = p.getUniqueId();
        if (!timeout.containsKey(playerUUID))
        {
            timeout.put(playerUUID, false);
        }

        ItemStack itemInHand = p.getInventory().getItemInMainHand();
        boolean isPlayerShifting = p.isSneaking();

        if (plugin.drugs.isCokeDrugItemStack(itemInHand) && isPlayerShifting)
        {
            if (p.hasPermission(Permissions.getPermission("consume_coke")))
            {
                if (timeout.get(playerUUID))
                    return;
                timeout.put(playerUUID, true);
                p.getInventory().getItemInMainHand().setAmount(itemInHand.getAmount() - 1);
                PotionEffect cokeEffect = new PotionEffect(PotionEffectType.SPEED, 600, 0);
                p.addPotionEffect(cokeEffect);
                p.sendMessage(plugin.messages.formattedChatMessage("consumed_coke"));

                //Starting the timeout
                new BukkitRunnable()
                {
                    public void run()
                    {
                        timeout.put(playerUUID, false);
                        cancel();
                    }
                }.runTaskTimer(plugin, 40, 1);

            } else
            {
                p.sendMessage(plugin.messages.formattedChatMessage("invalid_permission"));
            }
        }
    }
}
