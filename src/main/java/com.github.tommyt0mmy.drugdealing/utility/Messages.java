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
        	put("coke_name", "§aCoke Plant");
        	put("weed_name", "§aWeed Plant");
        	put("planted_coke", "§aYou planted a coke plant!");
        	put("planted_weed", "§aYou planted a weed plant!");
        	put("received_coke", "§aYou received a coke plant!");
        	put("received_weed", "§aYou received a weed plant!");
        	put("cannot_grow_weed", "§cYou cannot make a weed plant grow with bone meal!");
        	put("spawned_dealer", "§aYou spawned a drug dealer!");
        	put("spawned_producer", "§aYou spawned a drug producer!");
        	put("name_too_long", "§cThe selected name is too long!");
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
    }
    
    public String formattedMessage(String color, String messageName) {
    	return String.format("%s%s %s", color, getMessage("ingame_prefix"), getMessage(messageName));
    }
    
    public String formattedText(String color, String message) {
    	return String.format("%s%s %s", color, getMessage("ingame_prefix"), message);
    }
}
