package com.github.tommyt0mmy.drugdealing.tabcompleters;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class getDrugTabCompleter implements TabCompleter
{
    @SuppressWarnings("unused")
    private final DrugDealing plugin = DrugDealing.getInstance();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1)
        {
            String uncompletedArgument = args[0];
            if ("coke".startsWith(uncompletedArgument))
            {
                suggestions.add("coke");
            }
            if ("weed".startsWith(uncompletedArgument))
            {
                suggestions.add("weed");
            }
        }

        return suggestions;
    }

}
