package com.github.tommyt0mmy.drugdealing.utility;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Messages {
	
	private DrugDealing mainClass;
	public Messages(DrugDealing mainClass) {
		this.mainClass = mainClass;
		loadMessagesFile();
	}

	private FileConfiguration messagesConfig;
	
	@SuppressWarnings("serial")
	private static Map < String, String > messagesMap = new HashMap < String, String > () {
        {
        	put("only_players_command", "Only players can execute this command");
        	put("ingame_prefix", "[Drugs]");
        	put("unexpected_error", "§cUnexpected error occurred!");
        	put("invalid_permission", "§cYou don't have the permission to do this");
        	put("invalid_surface", "§cPlants need to be placed on farmland");
        	put("coke_plant_name", "§aCoke Plant");
        	put("weed_plant_name", "§aWeed Plant");
			put("coke_drug_name", "§aCocaine");
			put("weed_drug_name", "§aWeed");
			put("consumed_coke", "§aYou consumed cocaine");
        	put("planted_coke", "§aYou planted a coke plant!");
        	put("planted_weed", "§aYou planted a weed plant!");
        	put("received_coke_plant", "§aYou received a coke plant!");
        	put("received_weed_plant", "§aYou received a weed plant!");
			put("received_coke_drug", "§aYou received cocaine!");
			put("received_weed_drug", "§aYou received weed!");
        	put("cannot_grow_weed", "§cYou cannot make a weed plant grow with bone meal!");
        	put("spawned_dealer", "§aYou spawned a drug dealer!");
        	put("spawned_producer", "§aYou spawned a drug producer!");
        	put("name_too_long", "§cThe selected name is too long!");
        	put("cannot_sell_plants", "§cPlants cannot be sold!");
        	put("dealer_wrong_item", "To sell drugs right click me with drugs in the main hand");
        	put("producer_wrong_item", "To make me produce drugs for you right click me with drug plants in the main hand");
        	put("producer_invalid_balance", "You don't have enough money! You need <PRICE>");
        	put("dealer_bought_item", "You sold 1 <DRUG-NAME> item for <PRICE>");
        	put("dealer_bought_item_plural", "You sold <AMOUNT> <DRUG-NAME> items for <PRICE>");
        	put("npc_drug_not_accepted", "I do not accept <DRUG-NAME>");
        	put("producer_converted_drug_plural", "You converted <AMOUNT> <PLANT-NAME> items to <AMOUNT> <DRUG-NAME> items for <PRICE>");
			put("producer_converted_drug", "You converted one <PLANT-NAME> item to one <DRUG-NAME> item for <PRICE>");
			put("right_click_npc_to_delete", "§aRight click a criminal to remove him");
			put("remove_dealer_invalid_permission", "§cYou don't have the permission to delete a Dealer NPC");
			put("remove_producer_invalid_permission", "§cYou don't have the permission to delete a Producer NPC");
			put("removenpc_time_is_up", "§cCancelling <COMMAND> command: Time is up");
			put("removenpc_success", "§aCriminal removed successfully");
			put("page_not_found", "§cPage not found");
        }
	};
	
	public String getMessage(String messageName) {
		String msg = "";
		msg = messagesMap.get(messageName);
		return msg;
	}
	
    private void loadMessagesFile() { //loading messages.yml
		File messagesConfigFile = new File(mainClass.datafolder, "messages.yml");
        if (!messagesConfigFile.exists()) {
        	messagesConfigFile.getParentFile().mkdirs();
            mainClass.saveResource("messages.yml", false);
            mainClass.console.info("Created file messages.yml");
            mainClass.console.info("To modify ingame messages edit messages.yml and reload the server");
         }

        messagesConfig = new YamlConfiguration();
        try {
        	messagesConfig.load(messagesConfigFile);
        	loadMessages();
        } catch (Exception e) {
            mainClass.console.severe("Couldn't load messages.yml file properly!");
        }
    }
    
    private void loadMessages() {
		/*
    	messagesMap.put("only_players_command", (String) messagesConfig.get("messages.only_players_command"));
    	messagesMap.put("ingame_prefix", (String) messagesConfig.get("messages.ingame_prefix"));
    	messagesMap.put("unexpected_error", (String) messagesConfig.get("messages.unexpected_error"));
    	messagesMap.put("invalid_permission", (String) messagesConfig.get("messages.invalid_permission"));
    	messagesMap.put("invalid_surface", (String) messagesConfig.get("messages.invalid_surface"));
    	messagesMap.put("coke_name", (String) messagesConfig.get("messages.coke_name"));
    	messagesMap.put("weed_name", (String) messagesConfig.get("messages.weed_name"));
    	messagesMap.put("planted_coke", (String) messagesConfig.get("messages.planted_coke"));
    	messagesMap.put("planted_weed", (String) messagesConfig.get("messages.planted_weed"));
    	messagesMap.put("received_coke", (String) messagesConfig.get("messages.received_coke"));
    	messagesMap.put("received_weed", (String) messagesConfig.get("messages.received_weed"));
    	messagesMap.put("cannot_grow_weed", (String) messagesConfig.get("messages.cannot_grow_weed"));
    	messagesMap.put("spawned_dealer", (String) messagesConfig.get("messages.spawned_dealer"));
    	messagesMap.put("spawned_producer", (String) messagesConfig.get("messages.spawned_producer"));
    	messagesMap.put("name_too_long", (String) messagesConfig.get("messages.name_too_long"));
    	messagesMap.put("cannot_sell_plants", (String) messagesConfig.get("messages.cannot_sell_plants"));
		*/
		loadMessage("only_players_command");
		loadMessage("ingame_prefix");
		loadMessage("unexpected_error");
		loadMessage("invalid_permission");
		loadMessage("page_not_found");
		loadMessage("invalid_surface");
		loadMessage("coke_plant_name");
		loadMessage("weed_plant_name");
		loadMessage("coke_drug_name");
		loadMessage("weed_drug_name");
		loadMessage("consumed_coke");
		loadMessage("planted_coke");
		loadMessage("planted_weed");
    	loadMessage("received_coke_plant");
		loadMessage("received_weed_plant");
		loadMessage("received_coke_drug");
		loadMessage("received_weed_drug");
		loadMessage("cannot_grow_weed");
		loadMessage("spawned_dealer");
    	loadMessage("spawned_producer");
		loadMessage("name_too_long");
		loadMessage("cannot_sell_plants");
		loadMessage("dealer_wrong_item");
		loadMessage("producer_wrong_item");
		loadMessage("producer_invalid_balance");
		loadMessage("dealer_bought_item");
		loadMessage("dealer_bought_item_plural");
		loadMessage("npc_drug_not_accepted");
		loadMessage("producer_converted_drug");
		loadMessage("producer_converted_drug_plural");
		loadMessage("right_click_npc_to_delete");
		loadMessage("remove_dealer_invalid_permission");
		loadMessage("remove_producer_invalid_permission");
		loadMessage("removenpc_time_is_up");
		loadMessage("removenpc_success");

		mainClass.console.info("Loaded custom messages");
	}

	private void loadMessage (String messageName) { messagesMap.put(messageName, (String) messagesConfig.get("messages." + messageName)); }

    public String formattedMessage(String color, String messageName) {
    	return String.format("%s%s %s", color, getMessage("ingame_prefix"), getMessage(messageName));
    }
    
    public String formattedText(String color, String message) {
    	return String.format("%s%s %s", color, getMessage("ingame_prefix"), message);
    }
}
