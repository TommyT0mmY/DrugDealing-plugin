package com.github.tommyt0mmy.drugdealing.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CreateNPCTabCompleter implements TabCompleter
{

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1)
        {
            String uncompleteArg = args[0];
            if ("producer".startsWith(uncompleteArg))
            {
                suggestions.add("producer");
            }
            if ("dealer".startsWith(uncompleteArg))
            {
                suggestions.add("dealer");
            }
        }

        if (args.length >= 3)
        {
            String uncompleteArg = args[args.length - 1].toUpperCase();
            if ("COKE_PRODUCT".startsWith(uncompleteArg))
            {
                suggestions.add("COKE_PRODUCT");
            }
            if ("COKE_PLANT".startsWith(uncompleteArg))
            {
                suggestions.add("COKE_PLANT");
            }
            if ("WEED_PRODUCT".startsWith(uncompleteArg))
            {
                suggestions.add("WEED_PRODUCT");
            }
            if ("WEED_PLANT".startsWith(uncompleteArg))
            {
                suggestions.add("WEED_PLANT");
            }
        }

        return suggestions;
    }

}
