package com.github.tommyt0mmy.drugdealing.utility;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class Messages {

	public Messages() {
		loadMessagesFile();
	}

	private final DrugDealing instance = DrugDealing.getInstance();

	private FileConfiguration messagesConfig;
	private File messagesConfigFile;


	private final HashMap<String, String> messagesMap = new HashMap<String, String>() {
		{
			put("ingame_prefix", "[Drugs]");
			put("messages.only_players_command.text", "Only players can execute this command");
			put("messages.only_players_command.prefix_color", "");
			put("messages.unexpected_error.text", "Unexpected error occurred!");
			put("messages.unexpected_error.prefix_color", "&c");
			put("messages.invalid_permission.text", "You don't have the permission to do this");
			put("messages.invalid_permission.prefix_color", "&c");
			put("messages.page_not_found.text", "Page not found");
			put("messages.page_not_found.prefix_color", "&c");
			put("messages.invalid_surface.text", "Plants need to be placed on farmland");
			put("messages.invalid_surface.prefix_color", "&c");
			put("messages.consumed_coke.text", "You consumed cocaine");
			put("messages.consumed_coke.prefix_color", "&a");
			put("messages.planted_coke.text", "You planted a coke plant!");
			put("messages.planted_coke.prefix_color", "&a");
			put("messages.planted_weed.text", "You planted a weed plant!");
			put("messages.planted_weed.prefix_color", "&a");
			put("messages.received_coke_plant.text", "You received a coke plant!");
			put("messages.received_coke_plant.prefix_color", "&a");
			put("messages.received_weed_plant.text", "You received a weed plant!");
			put("messages.received_weed_plant.prefix_color", "&a");
			put("messages.received_coke_drug.text", "You received cocaine!");
			put("messages.received_coke_drug.prefix_color", "&a");
			put("messages.received_weed_drug.text", "You received weed!");
			put("messages.received_weed_drug.prefix_color", "&a");
			put("messages.cannot_grow_weed.text", "You cannot make a weed plant grow with bone meal!");
			put("messages.cannot_grow_weed.prefix_color", "&c");
			put("messages.spawned_dealer.text", "You spawned a drug dealer!");
			put("messages.spawned_dealer.prefix_color", "&a");
			put("messages.spawned_producer.text", "You spawned a drug producer!");
			put("messages.spawned_producer.prefix_color", "&a");
			put("messages.name_too_long.text", "The selected name is too long!");
			put("messages.name_too_long.prefix_color", "&c");
			put("messages.npc_drug_not_accepted.text", "I do not accept <DRUG-NAME>");
			put("messages.npc_drug_not_accepted.prefix_color", "&c");
			put("messages.cannot_sell_plants.text", "Plants cannot be sold! Sell me refined drugs");
			put("messages.cannot_sell_plants.prefix_color", "&c");
			put("messages.dealer_wrong_item.text", "To sell drugs right click me with drugs in the main hand");
			put("messages.dealer_wrong_item.prefix_color", "&a");
			put("messages.dealer_bought_item.text", "You sold one <DRUG-NAME> item for <PRICE>");
			put("messages.dealer_bought_item.prefix_color", "&a");
			put("messages.dealer_bought_item_plural.text", "You sold <AMOUNT> <DRUG-NAME> items for <PRICE>");
			put("messages.dealer_bought_item_plural.prefix_color", "&a");
			put("messages.producer_wrong_item.text", "To make me produce drugs for you right click me with drug plants in the main hand");
			put("messages.producer_wrong_item.prefix_color", "&a");
			put("messages.producer_invalid_balance.text", "You don't have enough money! You need <PRICE>");
			put("messages.producer_invalid_balance.prefix_color", "&c");
			put("messages.producer_converted_drug.text", "You converted one <PLANT-NAME> item to one <DRUG-NAME> item for <PRICE>");
			put("messages.producer_converted_drug.prefix_color", "&a");
			put("messages.producer_converted_drug_plural.text", "You converted <AMOUNT> <PLANT-NAME> items to <AMOUNT> <DRUG-NAME> items for <PRICE>");
			put("messages.producer_converted_drug_plural.prefix_color", "&a");
			put("messages.right_click_npc_to_delete.text", "Right click a criminal to remove him");
			put("messages.right_click_npc_to_delete.prefix_color", "&a");
			put("messages.remove_dealer_invalid_permission.text", "You don't have the permission to delete a Dealer NPC");
			put("messages.remove_dealer_invalid_permission.prefix_color", "&c");
			put("messages.remove_producer_invalid_permission.text", "You don't have the permission to delete a Producer NPC");
			put("messages.remove_producer_invalid_permission.prefix_color", "&c");
			put("messages.removenpc_time_is_up.text", "Cancelling /<COMMAND> command: Time is up");
			put("messages.removenpc_time_is_up.prefix_color", "&c");
			put("removenpc_success.text", "Criminal removed successfully");
			put("removenpc_success.prefix_color", "&a");
			put("keywords.coke_plant_name", "&aCoke Plant");
			put("keywords.weed_plant_name", "&aWeed Plant");
			put("keywords.coke_drug_name", "&aCocaine");
			put("keywords.weed_drug_name", "&aWeed");
		}
	};

	private void loadMessagesFile()
	{ //loading messages.yml
		String fileName = "messages.yml";
		messagesConfigFile = new File(instance.getDataFolder(), fileName);
		if (!messagesConfigFile.exists())
		{
			messagesConfigFile.getParentFile().mkdirs();
			instance.saveResource(fileName, false);
			instance.getLogger().info("Created messages.yml");
			instance.getLogger().info("To modify ingame messages edit messages.yml and reload the plugin");
		}

		messagesConfig = new YamlConfiguration();
		try
		{
			messagesConfig.load(messagesConfigFile);
			loadMessages();
		} catch (Exception e) {
			instance.getLogger().severe("Couldn't load messages.yml file properly!");
		}
	}

	private void loadMessages()
	{
		boolean needsRewrite = false; //rewrite needed when messages.yml is incomplete

		for (String messageKey : messagesMap.keySet())
		{
			boolean result = loadMessage(messageKey);
			needsRewrite = needsRewrite || result;
		}

		//if needsRewrite is true messages.yml gets closed, deleted, and rewritten with every message TODO find better way
		if (needsRewrite) {
			try {
				if (messagesConfigFile.delete()) { //deleting file
					messagesConfigFile.getParentFile().mkdirs(); //creating file
					messagesConfigFile.createNewFile();
					messagesConfig.load(messagesConfigFile);
					for (String messageKey : messagesMap.keySet()) { //writing file
						messagesConfig.set(messageKey, messagesMap.get(messageKey));
					}
					messagesConfig.save(messagesConfigFile);
				}
				else {
					instance.getLogger().severe("Couldn't load messages.yml file properly!");
				}
			}
			catch (Exception e) {
				instance.getLogger().severe("Couldn't load messages.yml file properly!");
			}
		}

		instance.getLogger().info("Loaded custom messages");
	}

	private boolean loadMessage (String messagePath) { //returns true if the message is not found, letting loadMessages() know if a rewrite of the file is needed or not
		boolean returnValue = false;

		if (messagesConfig.getString(messagePath, null) == null)
		{ //message not found, returns true
			returnValue = true;
		}

		if (messagesConfig.getString(messagePath) == null) {
			return true;
		}

		messagesMap.put(messagePath, messagesConfig.getString(messagePath)); //loading messages into messagesMap
		return returnValue;
	}

	public String getChatMessage(String messageName) {
		return translate(messagesMap.get("messages." + messageName + ".text"));
	}

	public String formattedChatMessage(String messageName) { //Puts the prefix and the color to the message
		String prefix_color = translate(messagesMap.get("messages." + messageName + ".prefix_color"));
		return translate(String.format("%s%s %s", prefix_color, getIngamePrefix(), getChatMessage(messageName)));
	}

	public String getKeyword(String keywordName) {
		return translate(messagesMap.get("keywords." + keywordName));
	}

	public String getIngamePrefix() {
		return translate(messagesMap.get("ingame_prefix"));
	}

	public String formattedText(ChatColor color, String message) {
		return String.format("%s%s %s", color, getIngamePrefix(), message);
	}

	private String translate(String in) {
		return ChatColor.translateAlternateColorCodes('&', in);
	}
}
