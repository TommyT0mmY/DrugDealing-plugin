//command 'setnpc'

package com.github.tommyt0mmy.drugdealing.commands;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.CriminalRole;
import com.github.tommyt0mmy.drugdealing.utility.DrugType;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateNPC implements CommandExecutor
{
    private final DrugDealing instance = DrugDealing.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(instance.language.getChatMessage("only_players_command")); //sending player error message
            return true;
        }

        Player p = (Player) sender;

        String usage = Objects.requireNonNull(instance.getCommand("setnpc")).getUsage().replace("<command>", label); //usage message

        if (args.length < 2)
        {
            p.sendMessage(instance.language.formattedText(ChatColor.RED, usage)); //sending player usage message
            return true;
        }

        CriminalRole role;
        String name;
        List<DrugType> notAcceptedDrugTypes = new ArrayList<>();

        if (args[1].length() > 12)
        { //do not remove
            p.sendMessage(instance.language.formattedChatMessage("name_too_long")); //sending player error message
            return true;
        }

        switch (args[0].toLowerCase())
        {
            case "producer":
                role = CriminalRole.PRODUCER;
                p.sendMessage(instance.language.formattedChatMessage("spawned_producer"));
                name = ("§a" + args[1]).trim();
                break;
            case "dealer":
                role = CriminalRole.DEALER;
                p.sendMessage(instance.language.formattedChatMessage("spawned_dealer"));
                name = ("§6" + args[1]).trim();
                break;
            default:
                p.sendMessage(instance.language.formattedText(ChatColor.RED, usage)); //sending player usage message
                return true;
        }

        if (args.length > 2)
        {
            for (int i = 0; i < args.length - 2; i++)
            {
                String notAcceptedDrugType = args[i + 2];
                DrugType drugTypeEquivalent;
                try
                {
                    drugTypeEquivalent = DrugType.valueOf(notAcceptedDrugType.toUpperCase());
                } catch (IllegalArgumentException illegalArgExp)
                {
                    IllegalNotAcceptedDrugTypeMessage(p, usage, label);
                    return true;
                }
                notAcceptedDrugTypes.add(drugTypeEquivalent);
            }
        }

        //spawning
        NPC spawnedNpc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);

        //setting custom skin
        spawnedNpc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, ChatColor.stripColor(name));
        spawnedNpc.data().setPersistent(NPC.PLAYER_SKIN_USE_LATEST, false);
        Entity npcEntity = spawnedNpc.getEntity();
        if (npcEntity instanceof SkinnableEntity)
        {
            ((SkinnableEntity) npcEntity).getSkinTracker().notifySkinChange(spawnedNpc.data().get(NPC.PLAYER_SKIN_USE_LATEST));
        }

        spawnedNpc.spawn(p.getLocation());

        instance.npcRegister.saveNpc(spawnedNpc, role, notAcceptedDrugTypes.toArray(new DrugType[0]));

        return true;
    }

    private void IllegalNotAcceptedDrugTypeMessage(Player receiver, String usage, String label)
    { // long message repeated two times
        receiver.sendMessage(instance.language.formattedText(ChatColor.RED, usage)); //sending player usage message
        receiver.sendMessage("§cAccepted drug types: WEED_PLANT, WEED_PRODUCT, COKE_PLANT and COKE_PRODUCT");
        receiver.sendMessage("§c" + String.format("Command example: /%s Dealer Bob WEED_PRODUCT", label));
    }

}
