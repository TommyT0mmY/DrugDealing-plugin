package com.github.tommyt0mmy.drugdealing.configuration;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import org.bukkit.ChatColor;

public class Language extends Configurable
{

    public Language(DrugDealing plugin)
    {
        super(plugin, "messages.yml");
    }

    public String getChatMessage(String messageName)
    {
        return translate(super.getFileConfiguration().getString("messages." + messageName + ".text"));
    }

    public String formattedChatMessage(String messageName)
    { //Puts the prefix and the color to the message
        String prefix_color = translate(super.getFileConfiguration().getString("messages." + messageName + ".prefix_color"));
        return translate(String.format("%s%s %s", prefix_color, getIngamePrefix(), getChatMessage(messageName)));
    }

    public String getKeyword(String keywordName)
    {
        return translate(super.getFileConfiguration().getString("keywords." + keywordName));
    }

    public String getIngamePrefix()
    {
        return translate(super.getFileConfiguration().getString("ingame_prefix"));
    }

    public String formattedText(ChatColor color, String message)
    {
        return String.format("%s%s %s", color, getIngamePrefix(), message);
    }

    private String translate(String in)
    {
        return ChatColor.translateAlternateColorCodes('&', in);
    }
}
