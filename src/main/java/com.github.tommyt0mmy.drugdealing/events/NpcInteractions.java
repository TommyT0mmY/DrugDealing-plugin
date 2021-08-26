package com.github.tommyt0mmy.drugdealing.events;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.CriminalRole;
import com.github.tommyt0mmy.drugdealing.utility.DrugType;
import com.github.tommyt0mmy.drugdealing.utility.Permissions;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class NpcInteractions implements Listener
{
    private final DrugDealing instance = DrugDealing.getInstance();

    @EventHandler
    public void onRightClick(NPCRightClickEvent e)
    {
        NPC npc = e.getNPC();
        Player p = e.getClicker();
        Optional<CriminalRole> role = instance.npcRegister.getCriminalRole(npc);

        //checks if the clicked NPC is a NPC handled by this plugin
        if (!role.isPresent() || !instance.npcRegister.isCriminalNpc(npc))
            return;

        //removing NPC if /removenpc command is executed
        if (instance.toRemoveNPCs.contains(p.getUniqueId()))
            removeNpc(p, role.get(), npc);

        //Getting the item held by the player and checking if it's a plugin's item
        ItemStack itemInHand = p.getInventory().getItemInMainHand();

        //Criminal NPC functionality
        switch (role.get())
        {
            case DEALER:
                dealerInteraction(p, npc, itemInHand);

                break;
            case PRODUCER:
                producerInteraction(p, npc, itemInHand);

                break;
        }
    }

    private void removeNpc(Player p, CriminalRole role, NPC npc)
    {
        switch (role) //todo redundancy
        {
            case DEALER:
                if (!p.hasPermission(Permissions.getPermission("remove_dealer")))
                {
                    p.sendMessage(instance.language.formattedChatMessage("remove_dealer_invalid_permission"));
                    instance.toRemoveNPCs.remove(p.getUniqueId());
                    return;
                }
            case PRODUCER:
                if (!p.hasPermission(Permissions.getPermission("remove_producer")))
                {
                    p.sendMessage(instance.language.formattedChatMessage("remove_producer_invalid_permission"));
                    instance.toRemoveNPCs.remove(p.getUniqueId());
                    return;
                }
        }

        instance.npcRegister.removeNpc(npc);
        npc.destroy();

        instance.toRemoveNPCs.remove(p.getUniqueId());
        p.sendMessage(instance.language.formattedChatMessage("removenpc_success"));
    }

    private void dealerInteraction(Player p, NPC npc, ItemStack itemInHand)
    {
        if (!p.hasPermission(Permissions.getPermission("use_dealer")))
        {
            p.sendMessage(instance.language.formattedChatMessage("invalid_permission"));
        }

        Optional<DrugType> optionalDrugType = instance.drugs.getDrugType(itemInHand);

        if (!optionalDrugType.isPresent()) //if the item held by the player isn't a drug
        {
            p.sendMessage(DealerFormattedMessage(instance.language.getChatMessage("dealer_wrong_item"), npc.getName(), null, null, null));
            return;
        }

        DrugType drugType = optionalDrugType.get();

        if (drugType.isPlant()) //checks if the player clicked with a plant, plants cannot be sold to dealers
        {
            p.sendMessage(DealerFormattedMessage(instance.language.getChatMessage("cannot_sell_plants"), npc.getName(), null, null, null));

        } else if (drugType.isAcceptedByDealer()) //more checks to find out if the item is being accepted
        {
            //Getting product price
            FileConfiguration configs = instance.settings.getFileConfiguration();

            if (isNotAccepted(npc, drugType, p))
                return;

            int amount = itemInHand.getAmount();
            double price = configs.getDouble(drugType.getKeywordSellingPrice()) * amount;

            //Removing item in hand
            p.getInventory().getItemInMainHand().setAmount(0);
            //Adding money to player's balance
            DrugDealing.economy.depositPlayer(p, price);

            if (amount == 1)
            {
                p.sendMessage(DealerFormattedMessage(instance.language.getChatMessage("dealer_bought_item"), npc.getName(), 1, drugType.getPrettyName(), price));
            } else
            {
                p.sendMessage(DealerFormattedMessage(instance.language.getChatMessage("dealer_bought_item_plural"), npc.getName(), amount, drugType.getPrettyName(), price));
            }
            //every other case
        } else
        {
            p.sendMessage(DealerFormattedMessage(instance.language.getChatMessage("dealer_wrong_item"), npc.getName(), null, null, null));
        }
    }

    private void producerInteraction(Player p, NPC npc, ItemStack itemInHand)
    {
        if (!p.hasPermission(Permissions.getPermission("use_producer")))
        {
            p.sendMessage(instance.language.formattedChatMessage("invalid_permission"));
        }

        Optional<DrugType> optionalDrugType = instance.drugs.getDrugType(itemInHand);

        if (!optionalDrugType.isPresent()) //if the item held by the player isn't a drug
        {
            p.sendMessage(ProducerFormattedMessage(instance.language.getChatMessage("producer_wrong_item"), npc.getName(), null, null, null, null));
            return;
        }

        DrugType drugType = optionalDrugType.get();

        //producers only accept plants
        if (!drugType.isPlant())
        {
            p.sendMessage(ProducerFormattedMessage(instance.language.getChatMessage("producer_wrong_item"), npc.getName(), null, null, null, null));
            return;
        }

        //Getting product price
        FileConfiguration configs = instance.settings.getFileConfiguration();

        if (isNotAccepted(npc, drugType, p))
            return;

        int amount = itemInHand.getAmount();

        //Checking player's balance
        double cost = configs.getDouble(drugType.getKeywordProductionPrice()) * amount;

        double balance = DrugDealing.economy.getBalance(p);
        if (balance < cost)
        {
            p.sendMessage(ProducerFormattedMessage(instance.language.getChatMessage("producer_invalid_balance"), npc.getName(), cost, null, null, null));
            return;
        }

        //Removing money
        DrugDealing.economy.withdrawPlayer(p, cost);

        //Changing plant to final product
        ItemStack producedDrug = instance.drugs.getItemStack(drugType.getOpposite());
        producedDrug.setAmount(amount);

        p.getInventory().setItemInMainHand(producedDrug);

        //Sending success message
        String plantName = drugType.getPrettyName();
        String productName = drugType.getOpposite().getPrettyName();
        String messageKeyword = (amount == 1) ? "producer_converted_drug" : "producer_converted_drug_plural";

        p.sendMessage(ProducerFormattedMessage(instance.language.getChatMessage(messageKeyword), npc.getName(), cost, amount, productName, plantName));
    }

    private boolean isNotAccepted(NPC npc, DrugType drugType, Player receiver)
    {
        Optional<CriminalRole> roleOptional = instance.npcRegister.getCriminalRole(npc);

        if (!roleOptional.isPresent())
            return false;

        if (instance.npcRegister.acceptsDrugType(npc, drugType))
        {
            switch (roleOptional.get())
            {
                case DEALER:
                    receiver.sendMessage(DealerFormattedMessage(instance.language.getChatMessage("npc_drug_not_accepted"), npc.getName(), null, drugType.getPrettyName(), null));
                    return true;
                case PRODUCER:
                    receiver.sendMessage(ProducerFormattedMessage(instance.language.getChatMessage("npc_drug_not_accepted"), npc.getName(), null, null, drugType.getPrettyName(), null));
                    return true;
            }
            return true;
        }
        return false;
    }

    private String DealerFormattedMessage(String message, String dealerName, Integer amount, String drugName, Double price)
    {
        //Keywords: <AMOUNT>, <DRUG-NAME>, <PRICE>

        //optional parameters
        amount = (amount != null ? amount : 0);
        price = (price != null ? price : 0.0);
        drugName = (drugName != null ? drugName : "");

        //adding color & prefix
        message = String.format("§6[%s] ", dealerName) + message;

        //adding keywords
        message = message.replace("<AMOUNT>", amount + "");
        message = message.replace("<DRUG-NAME>", drugName + "§6");
        if (price == 1)
        {
            message = message.replace("<PRICE>", price + " " + DrugDealing.economy.currencyNameSingular());
        } else
        {
            message = message.replace("<PRICE>", price + " " + DrugDealing.economy.currencyNamePlural());
        }

        return message;
    }

    private String ProducerFormattedMessage(String message, String producerName, Double price, Integer amount, String drugName, String plantName)
    {
        //Keywords: <PRICE>, <AMOUNT>, <PLANT-NAME>, <DRUG-NAME>

        //optional parameters
        amount = (amount != null ? amount : 0);
        price = (price != null ? price : 0.0);
        drugName = (drugName != null ? drugName : "").replace("§6", "§a");
        plantName = (plantName != null ? plantName : "").replace("§6", "§a");

        //adding color & prefix
        message = String.format("§a[%s] ", producerName) + message;

        //adding keywords
        message = message.replace("<AMOUNT>", amount + "");
        message = message.replace("<DRUG-NAME>", drugName + "§a");
        message = message.replace("<PLANT-NAME>", plantName + "§a");
        if (price == 1)
        {
            message = message.replace("<PRICE>", price + " " + DrugDealing.economy.currencyNameSingular());
        } else
        {
            message = message.replace("<PRICE>", price + " " + DrugDealing.economy.currencyNamePlural());
        }

        return message;
    }
}
