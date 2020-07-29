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

public class NpcInteractions implements Listener {
    DrugDealing mainClass = DrugDealing.getInstance();

    @EventHandler
    public void onRightClick (NPCRightClickEvent e) {
        NPC npc = e.getNPC();
        Player p = e.getClicker();
        CriminalRole role = mainClass.npcsreg.getRole(npc);

        if (mainClass.npcsreg.isCriminalNpc(npc)) { //checks if the clicked NPC is a NPC handled by this plugin

            //REMOVING NPC AFTER /removenpc COMMAND
            if (mainClass.toRemoveNPCS.contains(p.getUniqueId())) {

                switch (role) {
                    case DEALER:
                        if (!p.hasPermission(Permissions.getPermission("remove_dealer"))) {
                            p.sendMessage(mainClass.messages.formattedChatMessage("remove_dealer_invalid_permission"));
                            mainClass.toRemoveNPCS.remove(p.getUniqueId());
                            return;
                        }
                    case PRODUCER:
                        if (!p.hasPermission(Permissions.getPermission("remove_producer"))) {
                            p.sendMessage(mainClass.messages.formattedChatMessage("remove_producer_invalid_permission"));
                            mainClass.toRemoveNPCS.remove(p.getUniqueId());
                            return;
                        }
                }

                mainClass.npcsreg.removeNpc(npc);
                npc.destroy();

                mainClass.toRemoveNPCS.remove(p.getUniqueId());
                p.sendMessage(mainClass.messages.formattedChatMessage("removenpc_success"));
                return;
            }

            ItemStack itemInHand = p.getInventory().getItemInMainHand();
            int amount = itemInHand.getAmount();
            String npcName = npc.getName();
            DrugType drugType = mainClass.drugs.getDrugType(itemInHand);

            //Criminal NPC functionality
            switch (role) {
                case DEALER:
                    if (!p.hasPermission("use_dealer")) {
                        p.sendMessage(mainClass.messages.formattedChatMessage("invalid_permission"));
                    }

                    if (drugType == null) { //if it isn't a drug item
                        p.sendMessage(DealerFormattedMessage(mainClass.messages.getChatMessage("dealer_wrong_item"), npcName, null, null, null));
                        return;
                    }
                    //checks if the player clicked with a plant, plants cannot be sold
                    if (drugType.isPlant()) {
                        p.sendMessage(DealerFormattedMessage(mainClass.messages.getChatMessage("cannot_sell_plants"), npcName, null, null, null));

                    //if sold item is accepted
                    } else if (drugType.isAcceptedByDealer()) {
                        //Getting product price
                        FileConfiguration configs = mainClass.configs.getConfigs();
                        double price = 0;
                        switch (drugType) {
                            case COKE_PRODUCT:
                                if (!isAcceptedDT(npc, drugType, p)) return;
                                price = configs.getDouble("cokeDrugSellingPrice") * amount;
                                break;
                            case WEED_PRODUCT:
                                if (!isAcceptedDT(npc, drugType, p)) return;
                                price = configs.getDouble("weedDrugSellingPrice") * amount;
                                break;
                        }
                        //Removing item in hand
                        p.getInventory().getItemInMainHand().setAmount(0);
                        //Adding money to player's balance
                        mainClass.economy.depositPlayer(p, price);

                        if (amount == 1) {
                            p.sendMessage(DealerFormattedMessage(mainClass.messages.getChatMessage("dealer_bought_item"), npcName, 1, drugType.getPrettyName(), price));
                        } else {
                            p.sendMessage(DealerFormattedMessage(mainClass.messages.getChatMessage("dealer_bought_item_plural"), npcName, amount, drugType.getPrettyName(), price));
                        }
                    //every other case
                    } else {
                        p.sendMessage(DealerFormattedMessage(mainClass.messages.getChatMessage("dealer_wrong_item"), npcName, null, null, null));
                    }

                    break;
                case PRODUCER:
                    if (!p.hasPermission("use_producer")) {
                        p.sendMessage(mainClass.messages.formattedChatMessage("invalid_permission"));
                    }

                    if (drugType == null) { //if it isn't a drug item
                        p.sendMessage(ProducerFormattedMessage(mainClass.messages.getChatMessage("producer_wrong_item"), npcName, null, null, null, null));
                        return;
                    }

                    //producers only accept plants
                    if (!drugType.isPlant()) {
                        p.sendMessage(ProducerFormattedMessage(mainClass.messages.getChatMessage("producer_wrong_item"), npcName, null, null, null, null));
                        return;
                    }

                    //Getting product price
                    FileConfiguration configs = mainClass.configs.getConfigs();
                    ItemStack result = null;
                    String plant_name = null;
                    String drug_name = null;
                    double cost = 0;

                    switch (drugType) {
                        case COKE_PLANT:
                            if (!isAcceptedDT(npc, drugType, p)) return;
                            cost = configs.getDouble("cokeProductionPrice") * amount;
                            result = mainClass.drugs.getCokeDrugItemStack();
                            result.setAmount(amount);
                            plant_name = DrugType.COKE_PLANT.getPrettyName();
                            drug_name = DrugType.COKE_PRODUCT.getPrettyName();
                            break;
                        case WEED_PLANT:
                            if (!isAcceptedDT(npc, drugType, p)) return;
                            cost = configs.getDouble("weedProductionPrice") * amount;
                            result = mainClass.drugs.getWeedDrugItemStack();
                            result.setAmount(amount);
                            plant_name = DrugType.WEED_PLANT.getPrettyName();
                            drug_name = DrugType.WEED_PRODUCT.getPrettyName();
                            break;
                    }

                    //Checking player's balance
                    double balance = mainClass.economy.getBalance(p);
                    if (balance < cost) {
                        p.sendMessage(ProducerFormattedMessage(mainClass.messages.getChatMessage("producer_invalid_balance"), npcName, cost, null, null, null));
                        return;
                    }

                    //Removing money
                    mainClass.economy.withdrawPlayer(p, cost);

                    //Changing plant to final product
                    p.getInventory().setItemInMainHand(result);

                    //Sending success message
                    if (amount == 1) {
                        p.sendMessage(ProducerFormattedMessage(mainClass.messages.getChatMessage("producer_converted_drug"), npcName, cost, amount, drug_name, plant_name));
                    } else {
                        p.sendMessage(ProducerFormattedMessage(mainClass.messages.getChatMessage("producer_converted_drug_plural"), npcName, cost, amount, drug_name, plant_name));
                    }

                    break;
            }
        }
    }

    private boolean isAcceptedDT(NPC npc, DrugType drugType, Player receiver) {
        CriminalRole role = mainClass.npcsreg.getRole(npc);
        if (mainClass.npcsreg.acceptsDrugType(npc, drugType)) {
            switch (role) {
                case DEALER:
                    receiver.sendMessage(DealerFormattedMessage(mainClass.messages.getChatMessage("npc_drug_not_accepted"), npc.getName(), null, drugType.getPrettyName(), null));
                    return false;
                case PRODUCER:
                    receiver.sendMessage(ProducerFormattedMessage(mainClass.messages.getChatMessage("npc_drug_not_accepted"), npc.getName(), null, null, drugType.getPrettyName(), null));
                    return false;
            }
            return false;
        }
        return true;
    }

    private String DealerFormattedMessage(String message, String dealerName, Integer amount, String drugName, Double price) {
        //Keywords: <AMOUNT>, <DRUG-NAME>, <PRICE>

        //optional parameters
        amount = (amount != null ? amount : 0);
        price = (price != null ? price : 0.0);
        drugName = (drugName != null ? drugName : "");

        //adding color & prefix
        message = String.format("§6[%s] ", dealerName) + message;

        //adding keywords
        message = message.replaceAll("<AMOUNT>", amount + "");
        message = message.replaceAll("<DRUG-NAME>", drugName + "§6");
        if (price == 1) {
            message = message.replaceAll("<PRICE>", price + " " + mainClass.economy.currencyNameSingular());
        }else {
            message = message.replaceAll("<PRICE>", price + " " + mainClass.economy.currencyNamePlural());
        }

        return message;
    }

    private String ProducerFormattedMessage(String message, String producerName, Double price, Integer amount, String drugName, String plantName) {
        //Keywords: <PRICE>, <AMOUNT>, <PLANT-NAME>, <DRUG-NAME>

        //optional parameters
        amount = (amount != null ? amount : 0);
        price = (price != null ? price : 0.0);
        drugName = (drugName != null ? drugName : "").replaceAll("§6", "§a");
        plantName = (plantName != null ? plantName : "").replaceAll("§6", "§a");

        //adding color & prefix
        message = String.format("§a[%s] ", producerName) + message;

        //adding keywords
        message = message.replaceAll("<AMOUNT>", amount + "");
        message = message.replaceAll("<DRUG-NAME>", drugName + "§a");
        message = message.replaceAll("<PLANT-NAME>", plantName + "§a");
        if (price == 1) {
            message = message.replaceAll("<PRICE>", price + " " + mainClass.economy.currencyNameSingular());
        } else {
            message = message.replaceAll("<PRICE>", price + " " + mainClass.economy.currencyNamePlural());
        }

        return message;
    }
}
