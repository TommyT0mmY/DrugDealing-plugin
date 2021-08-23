package com.github.tommyt0mmy.drugdealing.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HelpTabCompleter implements TabCompleter
{

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
    {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1)
        {
            String uncompletedArgument = args[0];
            if ("drugdealing".startsWith(uncompletedArgument))
            {
                suggestions.add("drugdealing");
            }
            if ("getplant".startsWith(uncompletedArgument))
            {
                suggestions.add("getplant");
            }
            if ("getdrug".startsWith(uncompletedArgument))
            {
                suggestions.add("getdrug");
            }
            if ("setnpc".startsWith(uncompletedArgument))
            {
                suggestions.add("setnpc");
            }
            if ("removenpc".startsWith(uncompletedArgument))
            {
                suggestions.add("removenpc");
            }
            if ("permissions".startsWith(uncompletedArgument))
            {
                suggestions.add("permissions");
            }
        } else if (args.length == 2)
        {
            String argBefore = args[0];
            String uncompletedArgument = args[1];

            //aliases (not in use rn)
            //argBefore = argBefore.replaceAll("", "");

            if (argBefore.equals("permissions"))
            {
                if ("1".startsWith(uncompletedArgument))
                {
                    suggestions.add("1");
                }
                if ("2".startsWith(uncompletedArgument))
                {
                    suggestions.add("2");
                }
            } else
            {
                if ("1".startsWith(uncompletedArgument))
                {
                    suggestions.add("1");
                }
            }
        }

        return suggestions;
    }

}
